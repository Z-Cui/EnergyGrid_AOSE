package behavioursProducerSelectorAgent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import agents.ProducerSelectorAgent;
import concepts.HourlyConsumptionRequirement;
import concepts.HourlyEnergyProductivity;
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
		//System.out.println("== listen ProducerSelector");
		agent.doWait(1000);

		ACLMessage msg = agent.receive();
		if (msg != null) {
			try {
				//System.out.println(msg.getConversationId());
				
				// received a profile from consumer
				if (msg.getConversationId() == "consumerProfileInfo") {

					this._profile = new Profile((Profile) msg.getContentObject());
					agent.addProfileToHashMap(this._profile);
					System.out.println("-- ProducerSelectorAgent: Received profile from " + msg.getSender().getName());

				}
				// received a list of consumption requirement from consumer
				else if (msg.getConversationId() == "consumerConsumptionRequirementInfo") {

					this._conReqList = (ArrayList<HourlyConsumptionRequirement>) msg.getContentObject();

					Iterator<HourlyConsumptionRequirement> it = this._conReqList.iterator();
					while (it.hasNext()) {
						HourlyConsumptionRequirement r = it.next();
						agent.addConsumptionRequirementToQueue(r);
					}
					System.out.println("-- ProducerSelectorAgent: Received " + this._conReqList.size()
							+ " Consumption Requirement from " + msg.getSender().getName());
				}
				// received info of producers from ProducerListManager
				else if (msg.getConversationId() == "producerInfoQueue") {

					agent.set_energyProductivityQueue((PriorityQueue<HourlyEnergyProductivity>) msg.getContentObject());

					System.out.println("-- ProducerSelectorAgent: Received ProductivityInfo from " + msg.getSender().getName());
				}
				
				// received unknown message
				else
					System.out.println("-- ProducerSelectorAgent: received an unrecognizable message");

			} catch (UnreadableException e) {
				System.err.println("Cannot get profile and conReqList from message");
				e.printStackTrace();
			}
		}
	}

	public int onEnd() {
		if (agent.get_consumptionRequirementQueue().isEmpty())
			return 0;
		else
			return 1;
	}

}
