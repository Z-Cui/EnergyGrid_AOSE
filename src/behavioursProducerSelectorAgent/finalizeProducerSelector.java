package behavioursProducerSelectorAgent;

import agents.ProducerSelectorAgent;
import jade.core.behaviours.OneShotBehaviour;

public class finalizeProducerSelector extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	
	public finalizeProducerSelector(ProducerSelectorAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
