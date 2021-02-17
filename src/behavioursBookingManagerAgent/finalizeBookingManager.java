package behavioursBookingManagerAgent;

import agents.BookingManagerAgent;
import jade.core.behaviours.OneShotBehaviour;

public class finalizeBookingManager extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	BookingManagerAgent agent;
	
	public finalizeBookingManager(BookingManagerAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
