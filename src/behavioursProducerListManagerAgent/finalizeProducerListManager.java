package behavioursProducerListManagerAgent;

import agents.ProducerListManagerAgent;
import jade.core.behaviours.OneShotBehaviour;

public class finalizeProducerListManager extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	ProducerListManagerAgent agent;
	
	public finalizeProducerListManager(ProducerListManagerAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
