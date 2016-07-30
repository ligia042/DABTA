package trasmapi.sumo;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


import trasmapi.genAPI.Lane;
import trasmapi.genAPI.Link;
import trasmapi.genAPI.TrafficLight;
import trasmapi.genAPI.exceptions.UnimplementedMethod;

public class SumoTrafficLight extends TrafficLight {
	Map<SumoLane, ArrayList<Integer>> lan3es;
	String greenBS, brakeBS, yellowBS;
	
	SumoLink[] links;
	SumoLane[] inlanes;
	SumoLane[] outlanes;
	String[] phases;
	
	/**
	 * Traffic lights are initialized with all lanes green.
	 * @param name id of the Traffic Light
	 */
	public SumoTrafficLight(String name) {
		id = name;
		lan3es = new TreeMap<SumoLane,  ArrayList<Integer>>();
		SumoLane[] s = getTrafficLightsLaneList();
		
		for(int i=0; i<s.length; i++) {
			ArrayList<Integer> tbu = lan3es.get(s[i]);
			if (tbu == null) {
				tbu = new ArrayList<Integer>();
				lan3es.put(s[i], tbu);
			}
			tbu.add(s.length - 1 - i);
		}
		
		greenBS = new String("");
		brakeBS = new String("");
		yellowBS = new String("");
		
		for(int i=0; i<s.length; i++) {
			greenBS+="1";
			brakeBS+="0";
			yellowBS+="0";
		}
	}
	
	/**
	 * method used to force the update of the Traffic Light
	 */
	public void updateTrafficLight() {
		setTrafficLight(greenBS, brakeBS, yellowBS);
	}
	
	/**
	 * method used to retrieve all the traffic light ids in the loaded network
	 * @return String[] - a Id array of all the traffic lights
	 */
	public static String[] getTrafficLightsIDList() {
		return null;
	
	}
	
	/**
	 * method used to retrieve all the traffic lights in the loaded network
	 * @return SumoTrafficLight[] - a SumoTrafficLight array with all the traffic lights
	 */
	public static SumoTrafficLight[] getTrafficLightsList() {
		String[] tlids = getTrafficLightsIDList();
		SumoTrafficLight[] real = new SumoTrafficLight[tlids.length];
		for (int i=0;i<tlids.length;i++)
			real[i] = new SumoTrafficLight(tlids[i]);
		return real;
	}

	/**
	 * method used to change the state of a Traffic Light lane without update
	 * @param lane id of the lane to be changed
	 * @param state character representing the new state of the Traffic Light -> G - green  |  g - green (without priority) | r - red | y - yellow	
	 */
	public void setTrafficLightLane(Lane lane, char state) {
		char[]tempG = greenBS.toCharArray();
		char[]tempR = brakeBS.toCharArray();
		char[]tempY = yellowBS.toCharArray();
		
		ArrayList<Integer> ids = lan3es.get(lane);
		for (int i=0; i<ids.size();i++) {
			switch(state) {
			case 'G':
				tempG[ids.get(i)] = '1';
				tempR[ids.get(i)] = '0';
				tempY[ids.get(i)] = '0';
				break;
			case 'g':
				tempG[ids.get(i)] = '1';
				tempR[ids.get(i)] = '0';
				tempY[ids.get(i)] = '1';
				break;
			case 'R':
			case 'r':
				tempG[ids.get(i)] = '0';
				tempR[ids.get(i)] = '1';
				tempY[ids.get(i)] = '0';
				break;
			case 'Y':
			case 'y':
				tempG[ids.get(i)] = '0';
				tempR[ids.get(i)] = '1';
				tempY[ids.get(i)] = '1';
				break;	
			}
		}
		greenBS = String.copyValueOf(tempG);
		brakeBS = String.copyValueOf(tempR);
		yellowBS = String.copyValueOf(tempY);
	}
	
	/**
	 * method used to change the state of a Traffic Light lane and update it
	 * @param lane id of the lane to be updated
	 * @param state character representing the new state of the Traffic Light -> G - green  |  g - green (without priority) | R - red | Y - yellow	
	 */
	public void changeTrafficLightLane(Lane lane, char state) {
		setTrafficLightLane(lane, state);
		updateTrafficLight();
	}
	
	/**
	 * method used to change the state of a set of Traffic Light lanes without update
	 * @param laneList array with the id of the lanes to be changed
	 * @param state character representing the new state of the Traffic Light -> G - green  |  g - green (without priority) | r - red | y - yellow	
	 */
	public void setTrafficLightLanes(Lane[] laneList, char state) {
		for(int i=0; i<laneList.length; i++)
			setTrafficLightLane(laneList[i], state);
	}
	
	/**
	 * method used to change the state of a set of Traffic Light lanes and update it
	 * @param laneList array with the id of the lanes to be updated
	 * @param state character representing the new state of the Traffic Light -> G - green  |  g - green (without priority) | R - red | Y - yellow	
	 */
	public void changeTrafficLightLanes(Lane[] laneList, char state) {
		setTrafficLightLanes(laneList, state);
		updateTrafficLight();
	}
	
	/**
	 * basic method to change the state of the traffic light
	 * @param greenBS green light bitset
	 * @param brakeBS red light bitset
	 * @param yellowBS yellow light bitset
	 */
	public void setTrafficLight(String greenBS, String brakeBS, String yellowBS) {
	
	}
	
	/** 
	 * basic method to change the state of the traffic light
	 * @param state string representing the new state of the Traffic Light (each character is a lane) -> G - green  |  g - green (without priority) | r - red | y - yellow
	 */
	public void setTrafficLight(String state) {
		String green = new String();
		String brake = new String(); 
		String yellow = new String();
		
		for(int i=0; i<state.length(); i++) {
			switch(state.charAt(i)) {
			case 'G':
				green += '1';
				brake += '0';
				yellow += '0';
				break;
			case 'g':
				green += '1';
				brake += '0';
				yellow += '1';
				break;
			case 'R':
			case 'r':
				green += '0';
				brake += '1';
				yellow += '0';
				break;
			case 'Y':
			case 'y':
				green += '0';
				brake += '1';
				yellow += '1';
				break;	
			}
		}
		setTrafficLight(green, brake, yellow);
	}
	
	/**
	 * Method used to retrieve a String[] with the ids (String) of all the lanes controlled by the Traffic Light 
	 * @return a list of the ids(String) controlled by the Traffic Light
	 */
	public SumoLane[] getTrafficLightsLaneList() {
		return inlanes;
	
	}

	/**
	 * Method used to load a String[] with the ids (String) of all the lanes controlled by the Traffic Light 
	 * 
	 */
	public void loadTrafficLightsLaneList() {
		
	}
	
	public String[] getTrafficLightState() {
		return phases;
		
	}
	
	/**
	 * Used to retrieve the number of vehicles waiting in the traffic light. 
	 * Considers only the lane imediatly before the traffic light 
	 * @param minVel threshold velocity to be considered 'waiting'
	 * @return number of vehicles waiting
	 */
	public Integer getNumVehiclesWaiting(Double minVel) {
		SumoLane[] lanelist = getTrafficLightsLaneList();
		Integer sum = new Integer(0);
		for(SumoLane l: lanelist) {
			sum += l.getNumVehiclesStopped(minVel);
		}
		return sum;
	}
	
	public void loadControlledLinks(){
		
	}

	public void loadPhases(){
		
	}

	public void loadOutLanes(){
		
		ArrayList<String> ls = new ArrayList<String>();
		
		for(int i=0; i< links.length; i++){
			if(!ls.contains(links[i].to))
				ls.add(links[i].to);
		}
		outlanes = new SumoLane[ls.size()];
		for(int i=0; i<ls.size();i++)
			outlanes[i] = new SumoLane(ls.get(i));
		
	}

	public void loadLanesLength(){

		for(SumoLane l:inlanes){
			l.loadLength();
		}
	}
	
	public void printLinks(){
		
		for(SumoLink l:links){
			System.out.println("FROM: "+l.getFrom()+"\tTO: "+l.getTo()+"\tACROSS: "+l.getAcross());
		}
	}
	
	public void printPhases(){
		
		for(String s: phases){
			System.out.println(s);
		}
	}
	
	public void printLanes(){

		for(SumoLane l:inlanes){
			System.out.println("lane: "+l.getId() + "     "+ l.length);
		}
	}

	public Link[] getLinks() {
		return links;
	}

	public String[] getPhases() {
		return phases;
	}

	public void setPhases(String[] phases) {
		this.phases = phases;
	}

	public Lane[] getInLanes() {
		return inlanes;
	}

	public Lane[] getOutLanes() {
		return outlanes;
	}

	public int getnumCarrosParados(String key){
		
		try {
			for(Lane l : inlanes)
				if(l.getId().equals(key))
						return l.getNumVehiclesStopped(3.0);
		} catch (UnimplementedMethod e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getnumCarros(String key){
		
		try {
			for(Lane l : inlanes)
				if(l.getId().equals(key))
						return l.getNumVehicles();
		} catch (UnimplementedMethod e) {
			e.printStackTrace();
		}
		return 0;
	}
}
