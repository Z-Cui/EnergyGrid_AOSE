package behavioursProducerSelectorAgent;

import agents.ProducerSelectorAgent;
import jade.core.behaviours.OneShotBehaviour;

public class finalizePS extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	
	public finalizePS(ProducerSelectorAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
