package av.behaviours;

import start.AutonomousVehicle;
import av.misc.AverageTT;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class AVArrivalBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;

	//como isto é um cyclic behaviour, sempre depois do veiculo chegar ao fim, sempre que ele passa na action (como é cyclic é sempre que pode), 
	//ia estar sempre a ver quanto tempo é que tinha demorado na viagem que terminou. assim com este boolean fazemos com que ele avalie isso só uma vez.
	//dentro desta action ele vai fazer as avaliaçoes que quiser à viagem que acabou de terminar, e preencher as tabelas que tiver de preencher, actualizar 
	//dados que tiver de actualizar. (por exemplo, caso seja preciso, actualizar a média de TT para a rota acabada de fazer

	public AVArrivalBehaviour(Agent agent) {
		super(agent);
	}

	public void action() {
		AutonomousVehicle av = (AutonomousVehicle) myAgent;
		if(av.vehicle != null && av.vehicle.arrived && av.endOfTrip == false) {
			String rotaEfectuada = av.vehicle.routeId;
		
			double tt = (av.vehicle.arrivalTime - av.vehicle.departTime)/1000;
			
			if(av.mediasTTPorRotaId.containsKey(rotaEfectuada)){
				AverageTT att = av.mediasTTPorRotaId.get(rotaEfectuada);
				att.add(tt);
			}
			else{
				av.mediasTTPorRotaId.put(rotaEfectuada, new AverageTT(tt));
			}
			
			for(String edgeID: av.currentTripTTByEdge.keySet()) {
				if(av.mediasTTPorEdgeId.containsKey(edgeID)){
					AverageTT att = av.mediasTTPorEdgeId.get(edgeID);
					att.add(av.currentTripTTByEdge.get(edgeID));
				}
				else{
					av.mediasTTPorEdgeId.put(edgeID, new AverageTT(av.currentTripTTByEdge.get(edgeID)));
				}
			}
			
			av.endOfTrip = true;
		}
		else
			block();
	}
}