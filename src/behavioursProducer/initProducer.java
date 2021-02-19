package behavioursProducer;

import agents.ProducerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class initProducer extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	ProducerAgent agent;
	
	public initProducer(ProducerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(agent.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producer");
		serviceDescription.setName(agent.getLocalName() + "-producer");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(agent, dfDescription);
			System.out.println("ProducerAgent " + agent.getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		System.out.println("ProducerAgent " + agent.getAID().getName() + " is ready.");
		
		agent.doWait(5000);
		
	}
}
