package behavioursMarketPlaceAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import agents.MarketPlaceAgent;
import concepts.HourlyEnergyProductivity;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class processMarketPlace extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	MarketPlaceAgent agent;
	ArrayList<HourlyEnergyProductivity> _productivityList;

	public processMarketPlace(MarketPlaceAgent a) {
		this.agent = a;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		
		agent.doWait(200);

		if (msg != null) {

			try {
				switch (msg.getConversationId()) {
				case "producerInfo": // received Producer Info from Producer Agent
					this._productivityList = (ArrayList<HourlyEnergyProductivity>) msg.getContentObject();

					Iterator<HourlyEnergyProductivity> it = this._productivityList.iterator();
					while (it.hasNext()) {
						HourlyEnergyProductivity p = it.next();
						agent.addProducerInfoToQueue(p);
					}
					System.out.println("-- MarketPlace: Received " + this._productivityList.size()
							+ " Productivity Info from " + msg.getSender().getName());
					break;
				case "askProducerInfo": // received Producer Info request from ProducerListManager agent
					ACLMessage msg_send = new ACLMessage(ACLMessage.INFORM);
					msg_send.setConversationId("producerInfo_MarketPlaceToProducerListManager");
					msg_send.addReceiver(msg.getSender());

					if (agent.get_energyProductivityQueue().size() > 0) {
						msg_send.setContentObject(agent.get_energyProductivityQueue());
						agent.send(msg_send);
					} else {
						// do nothing
					}
					break;
				case "removeAllAdvertisement": // received remove all advertisement request from a Producer agent
					System.out.println(
							"-- MarketPlace: Received remove all advertisement request from " + msg.getSender());
					agent.removeProducerInfoFromQueue(msg.getSender());
					break;
				default:
					System.out.println("-- MarketPlace: Received an unrecognizable message");
				}

			} catch (UnreadableException e) {
				System.err.println("-- MarketPlace: Cannot get productivity info from message of Producer");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("-- MarketPlace: Cannot add productivity info to message");
				e.printStackTrace();
			}
		}
	}

	public int onEnd() {
		// System.out.println("--- Mkpc " + agent.get_energyProductivityQueue().size() +
		// "prod.");
		return 1;
	}
}
