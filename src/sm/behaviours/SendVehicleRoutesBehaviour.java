package sm.behaviours;

import java.util.Map.Entry;
import av.misc.BidTable;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import sm.SimulationManager;
import start.AutonomousVehicle;

public class SendVehicleRoutesBehaviour extends OneShotBehaviour{
	private static final long serialVersionUID = 1L;
	
	public SendVehicleRoutesBehaviour() {
		super();
	}
	
	public void action() {		
		SimulationManager sm = (SimulationManager)this.myAgent;
		
		// todos os veiculos: atenção que pode ter havido veiculos que por algum motivo nao enviaram bids. 
		// pensar o que fazer nesses casos...
		
		for(Entry<String, AutonomousVehicle> entry: sm.autonomousVehiclesTable.entrySet()){
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			String avname = entry.getValue().getLocalName();
			BidTable bt = sm.tabelaBids.get(avname);
			
			// achar a Entry com o maior valor de bid
			Entry<String, Double> maxEntry = null;
			for (Entry<String, Double> entryRoute : bt.routeBidTable.entrySet())
			{
			    if (maxEntry == null || entryRoute.getValue().compareTo(maxEntry.getValue()) > 0)
			    {
			        maxEntry = entryRoute;
			    }
			}
			
			// enviar a Key da Entry calculada atras
			msg.setContent(maxEntry.getKey());
			msg.addReceiver(entry.getValue().getAID());
			sm.send(msg);
		}
	}
}