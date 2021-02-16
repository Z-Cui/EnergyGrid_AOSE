package behavioursProducerSelectorAgent;

import agents.ProducerSelectorAgent;
import jade.core.behaviours.OneShotBehaviour;

public class processProducerSelector extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	int retour;
	
	public processProducerSelector(ProducerSelectorAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		System.out.println("In process status - Producer Selector");
		this.retour = agent.ProcessAllRequirements();
	}
	
	public int onEnd() {
		return this.retour;
	}
}
