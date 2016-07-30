package av.behaviours;

import com.google.gson.Gson;

import start.AutonomousVehicle;
import av.misc.AverageTT;
import av.misc.BidTable;
import graph.Edge;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SendBidTableBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);
	Gson gson = new Gson();

	public SendBidTableBehaviour(Agent agent) {
		super(agent);
	}

	public void action() {
		AutonomousVehicle av = (AutonomousVehicle)this.myAgent;
		
		ACLMessage msg = av.receive(query);
		
		if (msg != null){
			
			double toll1 = 0.0;
			for(Edge e : av.graph.routes.get("route0"))
				if(e.toll)
					toll1 += e.extension / 1000.0 * 0.08;
			
			double toll2 = 0.0;
			for(Edge e : av.graph.routes.get("route1"))
				if(e.toll)
					toll2 += e.extension / 1000.0 * 0.08;
			
			double toll3 = 0.0;
			for(Edge e : av.graph.routes.get("route2"))
				if(e.toll)
					toll3 += e.extension / 1000.0 * 0.08;

			AverageTT tt1 = av.mediasTTPorRotaId.get("route0");
			if(tt1 == null){
				double route1maxTT = 0.0;
				for(Edge e : av.graph.routes.get("route0"))
					route1maxTT += e.extension / e.maxSpeed;
				tt1 = new AverageTT(route1maxTT);
			}
			
			AverageTT tt2 = av.mediasTTPorRotaId.get("route1");
			if(tt2 == null){
				double route2maxTT = 0.0;
				for(Edge e : av.graph.routes.get("route1"))
					route2maxTT += e.extension / e.maxSpeed;
				tt2 = new AverageTT(route2maxTT);
			}
			
			AverageTT tt3 = av.mediasTTPorRotaId.get("route2");
			if(tt3 == null){
				double route3maxTT = 0.0;
				for(Edge e : av.graph.routes.get("route2"))
					route3maxTT += e.extension / e.maxSpeed;
				tt3 = new AverageTT(route3maxTT);
			}
			
			double dtt12 = (tt1.value - tt2.value) * -1 * 7 / 60.0;
			double dtt13 = (tt1.value - tt3.value) * -1 * 7 / 60.0;
			double valueOfTime1 = dtt12 > dtt13 ? dtt12 : dtt13;
			valueOfTime1 = valueOfTime1 < 0 ? 0.0 : valueOfTime1;

			double dtt21 = (tt2.value - tt1.value) * -1 * 7 / 60.0;
			double dtt23 = (tt2.value - tt3.value) * -1 * 7 / 60.0;
			double valueOfTime2 = dtt21 > dtt23 ? dtt21 : dtt23;
			valueOfTime2 = valueOfTime2 < 0 ? 0.0 : valueOfTime2;

			double dtt31 = (tt3.value - tt1.value) * -1 * 7 / 60.0;
			double dtt32 = (tt3.value - tt2.value) * -1 * 7 / 60.0;
			double valueOfTime3 = dtt31 > dtt32 ? dtt31 : dtt32;
			valueOfTime3 = valueOfTime3 < 0 ? 0.0 : valueOfTime3;
						
			
			BidTable bidTable = new BidTable();
			bidTable.routeBidTable.put("route0", toll1 + valueOfTime1);
			bidTable.routeBidTable.put("route1", toll2 + valueOfTime2);
			bidTable.routeBidTable.put("route2", toll3 + valueOfTime3);
			

			String msgContent = gson.toJson(bidTable);
			ACLMessage reply = msg.createReply();
			reply.setContent(msgContent);
			reply.setPerformative(ACLMessage.INFORM);
			av.send(reply);
			
			// System.out.println(myAgent.getAID().getLocalName() + " : Mandei BidsTable");
		}
		else{
			block();
		}
	}
}