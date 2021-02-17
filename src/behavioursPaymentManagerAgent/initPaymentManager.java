package behavioursPaymentManagerAgent;

import agents.PaymentManagerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class initPaymentManager extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	PaymentManagerAgent agent;

	public initPaymentManager(PaymentManagerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {

		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(agent.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("paymentManager");
		serviceDescription.setName(agent.getLocalName() + "-paymentManager");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(agent, dfDescription);
			System.out.println("PaymentManagerAgent " + agent.getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		System.out.println("PaymentManagerAgent " + agent.getAID().getName() + " is ready.");
		
		agent.doWait(200);
	}
}
