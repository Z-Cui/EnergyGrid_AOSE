package behavioursMarketPlaceAgent;

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

		if (msg != null) {

			try {
				if (msg.getConversationId() == "sendProducerProductivityInfo") {

					this._productivityList = (ArrayList<HourlyEnergyProductivity>) msg.getContentObject();

					Iterator<HourlyEnergyProductivity> it = this._productivityList.iterator();
					while (it.hasNext()) {
						HourlyEnergyProductivity p = it.next();
						agent.addProducerInfoToQueue(p);
					}
					System.out.println("-- Received " + this._productivityList.size() + " Productivity Info from "
							+ msg.getSender().getName());
				} else
					System.out.println("-- MarketPlace listened an unrecognizable message");

			} catch (UnreadableException e) {
				System.err.println("Cannot get productivity info from message");
				e.printStackTrace();
			}
		}
	}

	public int onEnd() {
		return 1;
	}
}
