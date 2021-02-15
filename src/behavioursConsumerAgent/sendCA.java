package behavioursConsumerAgent;

import java.io.IOException;
import java.util.ArrayList;

import agents.ConsumerAgent;
import concepts.HourlyConsumptionRequirement;
import concepts.Profile;
import concepts.SendProfilReqList;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class sendCA extends OneShotBehaviour{
	private static final long serialVersionUID = 1L;

	private AID _producerSelectorAID;
	Profile _profile;
	ArrayList<HourlyConsumptionRequirement> _conReqList;
	SendProfilReqList sprl;
	ConsumerAgent agent;

	public sendCA(ConsumerAgent a) {
		this.agent = a;
		this._profile = a.getProfile();
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
					"ConsumerAgent " + agent.getAID().getName() + " SendConsumerProfileBehaviour: cannot find ProducerSelector AID");
			e.printStackTrace();
		}

		// Send consumer's profile to ProducerSelector
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId("sendConsumerProfileInfo");
		msg.addReceiver(this._producerSelectorAID);
		
		try {
			System.out.println("! sendCA");
			System.out.println("!! sendCA behaviour " + this._profile.toString());
			msg.setContentObject(this._profile);
		} catch (IOException ex) {
			System.err.println("Cannot add ConsumerProfile to message. Sending empty message.");
			ex.printStackTrace(System.err);
		}
		agent.send(msg);
		System.out.println("Send profile from " + agent.getAID().getName() + " to " + this._producerSelectorAID.getName());
	}
}
