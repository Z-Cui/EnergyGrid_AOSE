package behavioursProducerSelectorAgent;

import java.util.ArrayList;
import java.util.Iterator;

import agents.ProducerSelectorAgent;
import concepts.HourlyConsumptionRequirement;
import concepts.Profile;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class listenPS extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	Profile _profile = null;
	ArrayList<HourlyConsumptionRequirement> _conReqList;
	int flag;
	
	public listenPS(ProducerSelectorAgent a) {
		this.agent = a;
	}
	
	public void action() {
		agent.doWait(300);
		ACLMessage message = agent.receive();
		if (message != null) {
			try {
				
				System.out.println("!! ListenPS" +message.getContentObject().toString());
				System.out.println();
				
				this._profile = (Profile) message.getContentObject();
				
				System.out.println("!! ListenPS" +this._profile.toString());
				
				agent.addProfileToHashMap(this._profile);
				Iterator<HourlyConsumptionRequirement> it = this._conReqList.iterator();
				while (it.hasNext()) {
					HourlyConsumptionRequirement hcr = it.next();
					agent.addConsumptionRequirementToQueue(hcr);
				}
			} catch (UnreadableException e) {
				System.err.println("Cannot get profile and conReqList to message");
				e.printStackTrace();
			}
		}
		flag = agent.NoExistconsumptionRequirementQueue();
	}
	public int onEnd() {
		
		//return this.flag;
		return 0;
	}

}
