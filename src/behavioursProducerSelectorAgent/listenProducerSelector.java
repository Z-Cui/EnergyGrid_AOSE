package behavioursProducerSelectorAgent;

import java.util.ArrayList;
import java.util.Iterator;

import agents.ProducerSelectorAgent;
import concepts.HourlyConsumptionRequirement;
import concepts.Profile;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class listenProducerSelector extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	ProducerSelectorAgent agent;
	Profile _profile = null;
	ArrayList<HourlyConsumptionRequirement> _conReqList;
	int flag;

	public listenProducerSelector(ProducerSelectorAgent a) {
		this.agent = a;
	}

	@SuppressWarnings("unchecked")
	public void action() {
		// agent.doWait(300);
		ACLMessage msg = agent.receive();
		if (msg != null) {
			try {

				if (msg.getConversationId() == "sendConsumerProfileInfo") {

					this._profile = new Profile((Profile) msg.getContentObject());
					agent.addProfileToHashMap(this._profile);
					System.out.println("-- Received profile from " + msg.getSender().getName());

				} else if (msg.getConversationId() == "sendConsumerConsumptionRequirementInfo") {

					this._conReqList = (ArrayList<HourlyConsumptionRequirement>) msg.getContentObject();

					Iterator<HourlyConsumptionRequirement> it = this._conReqList.iterator();
					while (it.hasNext()) {
						HourlyConsumptionRequirement hcr = it.next();
						agent.addConsumptionRequirementToQueue(hcr);
					}
					System.out.println("-- Received " + this._conReqList.size() + " Consumption Requirement from "
							+ msg.getSender().getName());
				} else
					System.out.println("-- ProducerSelectorAgent listened an unrecognizable message");

			} catch (UnreadableException e) {
				System.err.println("Cannot get profile and conReqList from message");
				e.printStackTrace();
			}
		}
		flag = agent.NoExistconsumptionRequirementQueue();
		// System.out.println("--- listen ProducerSelector: Flag " + flag);
	}

	public int onEnd() {

		// return this.flag;
		return 0;
	}

}
