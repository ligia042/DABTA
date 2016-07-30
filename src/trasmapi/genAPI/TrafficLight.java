package trasmapi.genAPI;

import trasmapi.genAPI.exceptions.UnimplementedMethod;

public class TrafficLight {
	protected String id;
	
	/**
	 * method used to retrieve the String id of the traffic light
	 * @return the traffic light id
	 */
	public String getId() {
		return id;
	}
	
	
	/**
	 * method used to force the update of the Traffic Light
	 */
	public void updateTrafficLight() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to retrieve all the ids from all the traffic lights in the loaded network
	 * @return String[] - a String array with all the ids (String)
	 */
	public static TrafficLight[] getTrafficLightsList() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * Method used to retrieve a Lane[] with all the lanes controlled by the Traffic Light 
	 * @return a list of the lanes controlled by the Traffic Light
	 */
	public Lane[] getTrafficLightsLaneList() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to change the state of a Traffic Light lane without update
	 * @param lane to be changed
	 * @param state character representing the new state of the Traffic Light -> G - green  |  g - green (without priority) | r - red | y - yellow	
	 */
	public void setTrafficLightLane(Lane lane, char state) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to change the state of a Traffic Light lane and update it
	 * @param lane to be updated
	 * @param state character representing the new state of the Traffic Light -> G - green  |  g - green (without priority) | R - red | Y - yellow	
	 */
	public void changeTrafficLightLane(Lane lane, char state) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to change the state of a set of Traffic Light lanes without update
	 * @param laneList array with the lanes to be changed
	 * @param state character representing the new state of the Traffic Light -> G - green  |  g - green (without priority) | r - red | y - yellow	
	 */
	public void setTrafficLightLanes(Lane[] laneList, char state) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to change the state of a set of Traffic Light lanes and update it
	 * @param laneList array with the lanes to be updated
	 * @param state character representing the new state of the Traffic Light -> G - green  |  g - green (without priority) | R - red | Y - yellow	
	 */
	public void changeTrafficLightLanes(Lane[] laneList, char state) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * basic method to change the state of the traffic light
	 * @param greenBS green light bitset
	 * @param brakeBS red light bitset
	 * @param yellowBS yellow light bitset
	 */
	public void setTrafficLight(String greenBS, String brakeBS, String yellowBS) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}	
	
	/** 
	 * basic method to change the state of the traffic light
	 * @param state string representing the new state of the Traffic Light (each character is a lane) -> G - green  |  g - green (without priority) | r - red | y - yellow
	 */
	public void setTrafficLight(String state) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * Method used to load all the controlled links by the Traffic Light 
	 * 
	 */
	public void loadControlledLinks() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	/**
	 * Method used to load all the controlled lanes by the Traffic Light 
	 * 
	 */
	public void loadTrafficLightsLaneList() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * Method used to print all the controlled links by the Traffic Light 
	 * 
	 */
	public void printLinks() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public void printLanes() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public void printPhases() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public void loadLanesLength() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public Link[] getLinks() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public String[] getPhases() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public Lane[] getInLanes() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}


	public void loadOutLanes() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}


	public Lane[] getOutLanes() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

	public int getnumCarrosParados(String key) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}


	public int getnumCarros(String key) throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}


	public void loadPhases() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
}
