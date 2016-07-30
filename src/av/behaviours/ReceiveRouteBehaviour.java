package av.behaviours;

import start.AutonomousVehicle;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveRouteBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	MessageTemplate inform = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	
	public ReceiveRouteBehaviour(Agent a) {
		super(a);
	}
	
	public void action() {
		AutonomousVehicle av = (AutonomousVehicle) myAgent;
		ACLMessage msg = av.receive(inform);
		if (msg != null){
			
			String rota = msg.getContent();
			
			// Rota injectada
			av.pickRoute(rota);
			
			System.out.println(av.getAID().getLocalName() + " received " + rota);
		}
	}
}