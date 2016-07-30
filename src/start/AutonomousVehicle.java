package start;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import av.behaviours.AVArrivalBehaviour;
import av.behaviours.ReceiveRouteBehaviour;
import av.behaviours.SendBidTableBehaviour;
import av.misc.AverageTT;
import graph.SumoGraph;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import trasmapi.genAPI.Vehicle;
import trasmapi.sumo.SumoCom;
import trasmapi.sumo.SumoVehicle;

public class AutonomousVehicle extends Agent{

	private static final long serialVersionUID = 6095960260125307076L;

	public static Random rand;
	public int id;

	public Vehicle vehicle;

	private String vehicleType;
	private String routeID;
	
	private Map<Integer, String> tabelaRotas = new HashMap<Integer, String>();
	public Map<String, AverageTT> mediasTTPorRotaId = new HashMap<String, AverageTT>();
	public Map<String, AverageTT> mediasTTPorEdgeId = new HashMap<String, AverageTT>();
	
	public Map<String,Integer> currentTripTTByEdge = new HashMap<String, Integer>();

	public boolean endOfTrip = false;
	
	public SumoGraph graph;
	
	public AutonomousVehicle(int idDriver) {
		super();
		this.id= idDriver;
		
		this.graph = new SumoGraph("SimpleMap/sumo_nodes.txt", "SimpleMap/sumo_edges.txt");
		
		//escolher tipo de vehiculo à mão tambem. (como são poucos é escusado questionar o simulador de quais os tipos que existem)
		//os ids do tipo de vehiculo seguem este esquema: vehicleType#, onde o # é um número de 0 a 3 (ver ficheiro map.vtypes.xml)
		vehicleType = "vehicleType" + rand.nextInt(4); 
		
		//carregar as rotas à mão (tÊm de ser iguais às que estão no ficheiro routes.rou.xml, que são as que vão entrar no simulador)
		tabelaRotas.put(0, "route0");
		tabelaRotas.put(1, "route1");
		tabelaRotas.put(2, "route2");
	}


	protected void setup()
	{
		try{
			DFAgentDescription ad = new DFAgentDescription();
			ad.setName(getAID()); //agentID
			System.out.println("AID: "+ad.getName());

			ServiceDescription sd = new ServiceDescription();
			sd.setName(getName()); //nome do agente    
			System.out.println("Nome: "+sd.getName());

			sd.setType("AutonomousVehicle");
			System.out.println("Tipo: "+sd.getType()+"\n\n\n");

			ad.addServices(sd); 

			DFService.register(this, ad);
			
			// Behaviour para receber o pedido de Bids e envia-las
			addBehaviour(new SendBidTableBehaviour(this));
			
			// Behaviour para receber a escolha da rota a efectuar
			addBehaviour(new ReceiveRouteBehaviour(this));

			// Behaviour que vai correr sempre que o veículo chegar ao fim da sua rota para atualizar TTs
			addBehaviour(new AVArrivalBehaviour(this));
			
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		super.setup();
	}

	protected void takeDown()
	{
		try{
			DFService.deregister(this);  
		}
		catch(FIPAException e){
			e.printStackTrace();
		}
		super.takeDown();
	}

	public void pickRoute() {
		
		//para já vai ser uma escolha aleatória da tabela que está lá em cima (tabelaRotas)
		//mais tarde terá de consultar uma outra table com pesos para saber qual a rota a escolher.
		
		//inteiro aleatório entre 0 inclusive e tabelarotas.size exclusive
		int routeIndex = rand.nextInt(tabelaRotas.size());
		
		routeID = tabelaRotas.get(routeIndex);
	}
	
	public void pickRoute(String routeId) {
		routeID = routeId;
	}

	public void addVehicleToSimulation() {
		
		int departureTime = 0;
		double departPosition = 0;
		double departSpeed = 0;
		byte departLane = 0;

		vehicle = new SumoVehicle(id, vehicleType, routeID, departureTime, departPosition, departSpeed, departLane);
		
		endOfTrip = false;

		SumoCom.addVehicle((SumoVehicle)vehicle);

		SumoCom.addVehicleToSimulation((SumoVehicle)vehicle);
	}
}