package behavioursBookingManagerAgent;

import agents.BookingManagerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class initBookingManager extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	BookingManagerAgent agent;

	public initBookingManager(BookingManagerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {

		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(agent.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("bookingManager");
		serviceDescription.setName(agent.getLocalName() + "-bookingManager");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(agent, dfDescription);
			System.out.println("BookingManagerAgent " + agent.getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		System.out.println("BookingManagerAgent " + agent.getAID().getName() + " is ready.");
		
		agent.doWait(200);
	}
}
