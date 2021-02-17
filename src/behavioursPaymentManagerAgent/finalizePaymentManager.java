package behavioursPaymentManagerAgent;

import agents.PaymentManagerAgent;
import jade.core.behaviours.OneShotBehaviour;

public class finalizePaymentManager extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	PaymentManagerAgent agent;
	
	public finalizePaymentManager(PaymentManagerAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
