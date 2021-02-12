package behaviours;

import java.io.IOException;

import agents.ConsumerAgent;
import concepts.HourlyEnergyProductivity;
import concepts.Profile;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

// Consumer sends his profile to ProducerSelectorAgent
public class SendConsumerProfileBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private AID _producerSelectorAID;
	private Profile _profile;

	public SendConsumerProfileBehaviour(ConsumerAgent a) {
		super(a);
		this._profile = a.getProfile();
	}

	@Override
	public void action() {

		// Search the AID of the ProducerSelector agent
		DFAgentDescription dfDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerSelector");
		dfDescription.addServices(serviceDescription);

		try {
			DFAgentDescription[] producerSelector = DFService.search(myAgent, dfDescription);
			this._producerSelectorAID = producerSelector[0].getName();

		} catch (FIPAException e) {
			System.out.println(
					"ConsumerAgent " + myAgent.getAID().getName() + " SendConsumerProfileBehaviour: cannot find ProducerSelector AID");
			e.printStackTrace();
		}

		// Send consumer's profile to ProducerSelector
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId("sendConsumerProfileInfo");
		msg.addReceiver(this._producerSelectorAID);
		try {
			msg.setContentObject(this._profile);
		} catch (IOException ex) {
			System.err.println("Cannot add ConsumerProfile to message. Sending empty message.");
			ex.printStackTrace(System.err);
		}
		myAgent.send(msg);
	}

}
