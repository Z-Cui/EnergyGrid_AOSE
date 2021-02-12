package behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

import agents.MarketPlaceAgent;
import concepts.HourlyEnergyProductivity;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

// MarketPlace sends list to ProducerListManager
public class SendProducerInfoListBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private AID _producerListManagerAID;
	private PriorityQueue<HourlyEnergyProductivity> _producerQueue;

	public SendProducerInfoListBehaviour(MarketPlaceAgent a) {
		super(a);
		this._producerQueue = a.get_energyProductivityQueue();
	}

	@Override
	public void action() {

		// Search the AID of the ProducerListManager agent
		DFAgentDescription dfDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerListManager");
		dfDescription.addServices(serviceDescription);

		try {
			DFAgentDescription[] producerListManager = DFService.search(myAgent, dfDescription);
			if (producerListManager.length == 1) {
				this._producerListManagerAID = producerListManager[0].getName();
			}

		} catch (FIPAException e) {
			System.out.println(
					"MarketPlaceAgent " + myAgent.getAID().getName() + " SendProducerInfoListBehaviour: cannot find ProducerListManager AID");
			e.printStackTrace();
		}

		// Send producer list to ProducerListManager
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId("sendProducerInfoList");
		msg.addReceiver(this._producerListManagerAID);
		try {
			msg.setContentObject(this._producerQueue);
		} catch (IOException ex) {
			System.err.println("Cannot add ProducerInfoList to message. Sending empty message.");
			ex.printStackTrace(System.err);
		}
		myAgent.send(msg);
	}

}
