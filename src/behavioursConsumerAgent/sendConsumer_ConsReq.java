package behavioursConsumerAgent;

import java.io.IOException;
import java.util.ArrayList;

import agents.ConsumerAgent;
import concepts.HourlyConsumptionRequirement;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class sendConsumer_ConsReq extends OneShotBehaviour{
	private static final long serialVersionUID = 1L;

	private AID _producerSelectorAID;
	ArrayList<HourlyConsumptionRequirement> _conReqList;
	ConsumerAgent agent;

	public sendConsumer_ConsReq(ConsumerAgent a) {
		this.agent = a;
		this._conReqList = a.getConReqList();
	}

	@Override
	public void action() {

		// Search the AID of the ProducerSelector agent
		DFAgentDescription dfDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerSelector");
		dfDescription.addServices(serviceDescription);

		try {
			DFAgentDescription[] producerSelector = DFService.search(agent, dfDescription);
			this._producerSelectorAID = producerSelector[0].getName();

		} catch (FIPAException e) {
			System.out.println(
					"ConsumerAgent " + agent.getAID().getName() + " SendConsumerConsumptionRequirementBehaviour: cannot find ProducerSelector AID");
			e.printStackTrace();
		}

		// Send consumer's profile to ProducerSelector
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId("consumerConsumptionRequirementInfo");
		msg.addReceiver(this._producerSelectorAID);
		
		try {
			msg.setContentObject(this._conReqList);
		} catch (IOException ex) {
			System.err.println("Cannot add ConsumptionRequirement to message. Sending empty message.");
			ex.printStackTrace(System.err);
		}
		agent.send(msg);
		System.out.println("-- ConsumerAgent: Send " + this._conReqList.size() + " Consumption Requirement from " + agent.getAID().getName() + " to " + this._producerSelectorAID.getName());
	}
}
