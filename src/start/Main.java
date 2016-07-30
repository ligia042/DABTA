package start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import sm.SimulationManager;
import trasmapi.genAPI.exceptions.TimeoutException;
import trasmapi.genAPI.exceptions.UnimplementedMethod;

public class Main
{
	private static int nCycles = 10;
	
	static boolean JADE_GUI = false;
	private static ProfileImpl profile;
	private static ContainerController mainContainer;
	
	public static void main(String[] args) throws UnimplementedMethod, InterruptedException, IOException, TimeoutException
	{	
		if(JADE_GUI)
		{
			List<String> params = new ArrayList<String>();
			params.add("-gui");
			profile = new BootProfileImpl(params.toArray(new String[0]));
		}
		else
			profile = new ProfileImpl();

		Runtime rt = Runtime.instance();
		mainContainer = rt.createMainContainer(profile);
		
		SimulationManager simManager = new SimulationManager(mainContainer);

		try {
			mainContainer.acceptNewAgent("SIMMANAGER", simManager).start();
		}
		catch (StaleProxyException e){
			e.printStackTrace();
			return;
		}
		
		simManager.init();
		
		for(int i=0 ; i < nCycles; i++){
			Thread.sleep(100);

			System.out.println("Start Cycle " + i);
			simManager.initSimulation();
			System.out.println("--Start Auction" );
			simManager.startAuction();
			System.out.println("--Simulating");
			simManager.startSimulation();
		}
	}
}























