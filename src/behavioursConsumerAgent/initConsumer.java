package behavioursConsumerAgent;

import agents.ConsumerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class initConsumer extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	ConsumerAgent agent;

	public initConsumer(ConsumerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {

		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(agent.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("consumer");
		serviceDescription.setName(agent.getLocalName() + "-consumer");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(agent, dfDescription);
			System.out.println("ConsumerAgent " + agent.getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		System.out.println("ConsumerAgent " + agent.getAID().getName() + " is ready.");
		
		agent.doWait(15000);

	}
}
