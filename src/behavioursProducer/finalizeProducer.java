package behavioursProducer;

import agents.ProducerAgent;
import jade.core.behaviours.OneShotBehaviour;

public class finalizeProducer extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	ProducerAgent agent;
	
	public finalizeProducer(ProducerAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
