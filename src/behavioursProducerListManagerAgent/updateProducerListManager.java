package behavioursProducerListManagerAgent;

import java.util.PriorityQueue;

import agents.ProducerListManagerAgent;
import concepts.HourlyEnergyProductivity;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class updateProducerListManager extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	ProducerListManagerAgent agent;
	// private PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue =
	// new PriorityQueue<HourlyEnergyProductivity>(new
	// HourlyEnergyProductivity_Comparator());
	AID _marketPlaceAID;

	public updateProducerListManager(ProducerListManagerAgent a) {
		this.agent = a;

		// Search the AID of the ProducerSelector agent
		DFAgentDescription dfDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("marketPlace");
		dfDescription.addServices(serviceDescription);

		try {
			DFAgentDescription[] marketPlace = DFService.search(agent, dfDescription);
			this._marketPlaceAID = marketPlace[0].getName();

		} catch (FIPAException e) {
			System.out.println("ProducerListManager " + agent.getAID().getName() + " cannot find MarketPlace AID");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void action() {

		ACLMessage msg_send = new ACLMessage(ACLMessage.REQUEST);
		msg_send.setConversationId("askProducerInfo");
		msg_send.addReceiver(this._marketPlaceAID);
		agent.send(msg_send);

		agent.doWait(200);

		ACLMessage msg = agent.receive();

		try {
			if (msg != null) {

				if (msg.getConversationId() == "producerInfo_MarketPlaceToProducerListManager") {

					PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue = (PriorityQueue<HourlyEnergyProductivity>) msg
							.getContentObject();

					agent.set_energyProductivityQueue(_energyProductivityQueue);

					// System.out.println("- ProducerListManager: Updated " +
					// _energyProductivityQueue.size() + " Productivity Info from " +
					// msg.getSender().getName());

				} else
					System.out.println("-- ProducerListManager: Received an unrecognizable message");
			} else {
				// System.out.println(
				// "-- ProducerListManager: No response from MarketPlace agent regarding
				// Producer Info, next update in 30 seconds");
			}
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// update every 4 hours (30 seconds)
		agent.doWait(30000);

	}

	public int onEnd() {
		return 1;
	}
}
