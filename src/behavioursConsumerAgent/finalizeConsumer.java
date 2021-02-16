package behavioursConsumerAgent;

import agents.ConsumerAgent;
import jade.core.behaviours.OneShotBehaviour;

public class finalizeConsumer extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	ConsumerAgent agent;
	
	public finalizeConsumer(ConsumerAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
