package sm.behaviours;

import com.google.gson.Gson;

import av.misc.BidTable;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sm.SimulationManager;
import av.behaviours.DelayBehaviour;

public class ReceiveVehiclesBidsBehaviour extends ParallelBehaviour {

	private static final long serialVersionUID = 1L;
	Gson gson = new Gson();

	MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

	public ReceiveVehiclesBidsBehaviour(int timeout) {
		super(ParallelBehaviour.WHEN_ANY);
		
		// behaviour para delay
		addSubBehaviour(new DelayBehaviour(this.myAgent, timeout));
		
		// Bids a chegar
		addSubBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = 1L;
			public void action() {
				SimulationManager sm = (SimulationManager) myAgent;
				ACLMessage msg = sm.receive(query);
				if (msg!=null) 
				{
					BidTable bidTable = gson.fromJson(msg.getContent(), BidTable.class);
					System.out.println("SimulationManager received bid from " + msg.getSender().getLocalName());
					
					if(bidTable != null){
						sm.tabelaBids.put(msg.getSender().getLocalName(), bidTable);
					}
				}
			}
		});
		
		// verifica quando é que a tabela de bids tem o mesmo tamanho que a tabela de veiculos
		addSubBehaviour(new SimpleBehaviour(myAgent) {
			private static final long serialVersionUID = 1L;

			int nVehicles = Integer.MAX_VALUE;
			int nBids = 0;

			public boolean done() {
				return nVehicles == nBids;
			}

			public void action() {
				SimulationManager sm = (SimulationManager) myAgent;
				nVehicles = sm.autonomousVehiclesTable.size();
				nBids = sm.tabelaBids.size();
			}
		});
	}

	public int onEnd() {
		System.out.println("Bids recebidas com sucesso ou timeout");
		this.myAgent.removeBehaviour(this);
		return super.onEnd();
	}
}