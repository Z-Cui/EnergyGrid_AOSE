package behaviours;

import agents.ProducerListManagerAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

// ProducerListManager asks MarketPlace for a producer list 
public class AskProducerInfoListBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;

	private AID _marketPlaceAID;

	public AskProducerInfoListBehaviour(ProducerListManagerAgent a) {
		super(a);
	}

	@Override
	public void action() {

		// Search the AID of the ProducerListManager agent
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
					"ProducerListManagerAgent " + myAgent.getAID().getName() + " AskProducerInfoListBehaviour: cannot find MarketPlace AID");
			e.printStackTrace();
		}

		// Send producer list to ProducerListManager
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setConversationId("askProducerInfoList");
		msg.addReceiver(this._marketPlaceAID);
		myAgent.send(msg);
	}

}
