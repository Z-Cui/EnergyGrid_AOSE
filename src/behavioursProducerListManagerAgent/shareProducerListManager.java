package behavioursProducerListManagerAgent;

import java.io.IOException;

import agents.ProducerListManagerAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class shareProducerListManager extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;

	private AID _producerSelectorAID;
	ProducerListManagerAgent agent;

	public shareProducerListManager(ProducerListManagerAgent a) {
		this.agent = a;

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
			System.out.println("ProducerListManager " + agent.getAID().getName()
					+ " share ProducerInfo to ProducerSelector: cannot find ProducerSelector AID");
			e.printStackTrace();
		}

		// Send consumer's profile to ProducerSelector
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId("producerInfoQueue");
		msg.addReceiver(this._producerSelectorAID);

		try {
			msg.setContentObject(agent.get_energyProductivityQueue());
		} catch (IOException ex) {
			System.err.println("Cannot add ProducerInfo to message. Sending empty message.");
			ex.printStackTrace(System.err);
		}
		agent.send(msg);
		System.out.println("-- ProducerListManager: Share " + agent.get_energyProductivityQueue().size()
				+ " Producer Info from " + agent.getAID().getName() + " to " + this._producerSelectorAID.getName());
	}
}
