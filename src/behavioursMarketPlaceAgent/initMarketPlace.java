package behavioursMarketPlaceAgent;

import agents.MarketPlaceAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class initMarketPlace extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	MarketPlaceAgent agent;

	public initMarketPlace(MarketPlaceAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {

		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(agent.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("marketPlace");
		serviceDescription.setName(agent.getLocalName() + "-marketPlace");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(agent, dfDescription);
			System.out.println("MarketPlaceAgent " + agent.getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		System.out.println("MarketPlaceAgent " + agent.getAID().getName() + " is ready.");
		
		agent.doWait(5000);
	}
}
