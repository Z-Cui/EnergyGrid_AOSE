package behavioursProducerSelectorAgent;

import agents.ProducerSelectorAgent;
import jade.core.behaviours.OneShotBehaviour;

public class processProducerSelector extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	int state_flag;
	
	public processProducerSelector(ProducerSelectorAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		//System.out.println("== process ProducerSelector");
		agent.doWait(1000);
		
		this.state_flag = agent.ProcessAllRequirements();
	}
	
	public int onEnd() {
		return this.state_flag;
	}
}
