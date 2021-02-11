package behaviours;

import java.io.IOException;

import agents.ProducerAgent;
import concepts.HourlyEnergyProductivity;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

// Producer send info to MarketPlace
public class SendProducerInfoBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private AID _marketPlaceAID;
	private HourlyEnergyProductivity _capacity;

	public SendProducerInfoBehaviour(ProducerAgent a) {
		super(a);
		this._capacity = a.get_energyProductivity();
	}

	@Override
	public void action() {

		// Search the AID of the MarketPlace agent
		DFAgentDescription dfDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("marketPlace");
		dfDescription.addServices(serviceDescription);

		try {
			DFAgentDescription[] marketPlace = DFService.search(myAgent, dfDescription);
			if (marketPlace.length == 1) {
				this._marketPlaceAID = marketPlace[0].getName();
			}

		} catch (FIPAException e) {
			System.out.println(
					"Agent " + myAgent.getAID().getName() + " SendProducerInfoBehaviour: cannot find MarketPlace AID");
			e.printStackTrace();
		}

		// Send producer's information to Market Place
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setConversationId("producer_info");
		msg.addReceiver(this._marketPlaceAID);
		try {
			msg.setContentObject(this._capacity);
		} catch (IOException ex) {
			System.err.println("Cannot add Producer Info to message. Sending empty message.");
			ex.printStackTrace(System.err);
		}
		myAgent.send(msg);
	}

}
