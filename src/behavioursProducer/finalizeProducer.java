package behavioursProducer;

import agents.ProducerAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class finalizeProducer extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	ProducerAgent agent;
	private AID _marketPlaceAID;

	public finalizeProducer(ProducerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		// Search the AID of the MarketPlace agent
		DFAgentDescription dfDescription = new DFAgentDescription();
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("marketPlace");
		dfDescription.addServices(serviceDescription);

		try {
			DFAgentDescription[] marketPlace = DFService.search(agent, dfDescription);
			this._marketPlaceAID = marketPlace[0].getName();

		} catch (FIPAException e) {
			System.out.println("ProducerAgent " + agent.getAID().getName()
					+ " SendProducerProductivityInfoBehaviour: cannot find MarketPlace AID");
			e.printStackTrace();
		}

		// Ask MarketPlace to remove all advertisement of this producer
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setConversationId("removeAllAdvertisement");
		msg.addReceiver(this._marketPlaceAID);
		agent.send(msg);

		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
