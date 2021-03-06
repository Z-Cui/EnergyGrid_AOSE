package behavioursProducerSelectorAgent;

import agents.ProducerSelectorAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class initProducerSelector extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	
	public initProducerSelector(ProducerSelectorAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(agent.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerSelector");
		serviceDescription.setName(agent.getLocalName() + "-producerSelector");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(agent, dfDescription);
			System.out.println("ProducerSelectorAgent " + agent.getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ProducerSelectorAgent " + agent.getAID().getName() + " is ready.");
		
		agent.doWait(15000);
	}
}
