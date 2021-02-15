package behavioursProducerSelectorAgent;

import agents.ProducerSelectorAgent;
import jade.core.behaviours.OneShotBehaviour;

public class processPS extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	int retour;
	
	public processPS(ProducerSelectorAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		this.retour = agent.ProcessAllRequirements();
	}
	
	public int onEnd() {
		return this.retour;
	}
}
