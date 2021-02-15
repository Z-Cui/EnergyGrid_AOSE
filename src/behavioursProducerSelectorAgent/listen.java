package behavioursProducerSelectorAgent;

import agents.ProducerSelectorAgent;
import jade.core.behaviours.OneShotBehaviour;

public class listen extends OneShotBehaviour{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	int flag;
	
	public listen(ProducerSelectorAgent a) {
		this.agent = a;
	}
	
	public void action() {
		agent.doWait(300);
		flag = agent.NoExistconsumptionRequirementQueue();
	}
	public int onEnd() {
		
		//return this.flag;
		return 0;
	}
}
