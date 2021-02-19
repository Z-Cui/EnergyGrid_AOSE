package behavioursProducerListManagerAgent;

import agents.ProducerListManagerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class initProducerListManager extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	ProducerListManagerAgent agent;

	public initProducerListManager(ProducerListManagerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {

		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(agent.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerListManager");
		serviceDescription.setName(agent.getLocalName() + "-producerListManager");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(agent, dfDescription);
			System.out.println("ProducerListManager " + agent.getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		System.out.println("ProducerListManager " + agent.getAID().getName() + " is ready.");
		
		agent.doWait(5000);

	}
}
