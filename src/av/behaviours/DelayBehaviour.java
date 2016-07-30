package av.behaviours;
/*****************************************************************
	DelayBehaviour:  subclass of SimpleBehaviour that waits DT ms
	                 before executing 'action'.  We also provide a 
	                 default 'action' method that does nothing.
	                 
2 examples of use:
	                 
	  addBehaviour( new DelayBehaviour( this, 1000) );
	  addBehaviour( new DelayBehaviour( this, 1000) {
			{
				protected void <b>action</b>() {
					System.out.println( "... Message1" );
				}
			});
	  
*****************************************************************/

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class DelayBehaviour extends SimpleBehaviour {
	private static final long serialVersionUID = 1L;
	private long    timeout, 
	                wakeupTime;
	private boolean finished;
	
	public DelayBehaviour(Agent a, long timeout) 
	{
		super(a);
		this.timeout = timeout;
		finished = false;
	}
	
	public void onStart() {
		wakeupTime = System.currentTimeMillis() + timeout;
	}
		
	public void action() 
	{
		long dt = wakeupTime - System.currentTimeMillis();
		if (dt <= 0) {
			finished = true;
			handleElapsedTimeout();
		} else 
			block(dt);
			
	} //end of action
	
	protected void handleElapsedTimeout() {}
	
	public void reset(long timeout) {
	  wakeupTime = System.currentTimeMillis() + timeout ;
	  finished = false;
	}
	
	public boolean done() {
	  return finished;
	}
}