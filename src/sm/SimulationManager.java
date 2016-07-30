package sm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import av.misc.BidTable;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import sm.behaviours.ReceiveVehiclesBidsBehaviour;
import sm.behaviours.SendVehicleRoutesBehaviour;
import start.AutonomousVehicle;
import trasmapi.genAPI.TraSMAPI;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.Sumo;
import trasmapi.sumo.SumoEdge;

public class SimulationManager extends Agent{

	private static final long serialVersionUID = 2724654562866411374L;
	private ContainerController mainContainer;

	private final int numDrivers = 15;
	private TraSMAPI api;
	private Sumo sumo;

	public Map<String, BidTable> tabelaBids = new HashMap<String, BidTable>();
	
	public Map<String,AutonomousVehicle> autonomousVehiclesTable = new HashMap<String,AutonomousVehicle>();

	ReceiveVehiclesBidsBehaviour receiveVehiclesBids;
	SendVehicleRoutesBehaviour sendVehicleRoutes;
	
	ACLMessage msg;
	ACLMessage bestOffer;
	private SequentialBehaviour sequence;

	public SimulationManager(ContainerController mainContainer) {
		this.mainContainer = mainContainer;
	}

	protected void setup() {

		DFAgentDescription ad = new DFAgentDescription();
		ad.setName(getAID()); //agentID
		System.out.println("AID: "+ad.getName());

		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName()); //nome do agente    
		System.out.println("Nome: "+sd.getName());

		sd.setType("Manager");
		System.out.println("Tipo: "+sd.getType()+"\n\n\n");

		ad.addServices(sd); 

		try {
			DFService.register(this, ad);
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		super.setup();
	}

	protected void takeDown() {
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		super.takeDown();
	}

	public void init() {
		try {
			
			AutonomousVehicle.rand = new Random(System.currentTimeMillis());

			for(int i = 0 ; i< numDrivers ; i++){
				AutonomousVehicle autonomousVehicle = new AutonomousVehicle(i);
				autonomousVehiclesTable.put(""+i, autonomousVehicle);
				mainContainer.acceptNewAgent("AUTONOMOUSVEHICLE#" + i, autonomousVehicle).start();
			}
			
		} catch (StaleProxyException e) {
			System.out.println("Erro no Init do SimManager \"\n\n\"");
		}
	}

	public void initSimulation() {
		try {
			this.api = new TraSMAPI(); 

			//Create SUMO
			this.sumo = new Sumo("guisim");

			List<String> params = new ArrayList<String>();
			params.add("-c=SimpleMap/map.sumo.cfg");
			sumo.addParameters(params);

			sumo.addConnections("localhost", 8820);

			//Add Sumo to TraSMAPI
			api.addSimulator(sumo);

			//Launch and Connect all the simulators added
			api.launch();

			api.connect();

			api.start();

			sumo.comm.subscribeEdgesVehicleIDs();
			
		} catch (UnimplementedMethod e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error in initSimulation: \n\n\"" + e.getStackTrace() + "\"");
			e.printStackTrace();
		}
	}

	public void startSimulation() {
		
		int simSeconds = 200; 
		int i=0;

		for(AutonomousVehicle vehicle: autonomousVehiclesTable.values())
			vehicle.addVehicleToSimulation();
		
		while(i < simSeconds){
			try {
				api.simulationStep(0);
				// para cada presenca em cada edge a cada timestep, vai adicionar um segundo em cada veiculo nessa edge.
				addTimestepsToVehicles();
			} catch (UnimplementedMethod e) {
				e.printStackTrace();
			}
			i++;
		}
		try {
			this.api.close();
		} catch (UnimplementedMethod e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addTimestepsToVehicles() {
		for( SumoEdge edge : sumo.getEdges()){
			for(String vehicleID: edge.vehicleIdList){
				AutonomousVehicle av = autonomousVehiclesTable.get(vehicleID);
				if(av == null)
					continue;
				Integer previousTime = av.currentTripTTByEdge.get(edge.id);
				if(previousTime == null)
					av.currentTripTTByEdge.put(edge.id, new Integer(1));
				else
					av.currentTripTTByEdge.put(edge.id, ++previousTime);
			}
		}
	}

	public void startAuction() {
		this.sequence = new SequentialBehaviour();
		this.receiveVehiclesBids = new ReceiveVehiclesBidsBehaviour(10000);
		this.sendVehicleRoutes = new SendVehicleRoutesBehaviour();
		this.sequence.addSubBehaviour(receiveVehiclesBids);
		this.sequence.addSubBehaviour(sendVehicleRoutes);
		
		this.addBehaviour(this.sequence);
		
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
		for(AutonomousVehicle av: autonomousVehiclesTable.values())
		{
			msg.addReceiver(av.getAID());
		}
		this.send(msg);
		
		while(!this.sequence.done()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.removeBehaviour(sequence);
	}
}