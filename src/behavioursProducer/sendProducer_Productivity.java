package behavioursProducer;

import java.io.IOException;
import java.util.ArrayList;

import agents.ProducerAgent;
import concepts.HourlyEnergyProductivity;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class sendProducer_Productivity extends OneShotBehaviour{
	private static final long serialVersionUID = 1L;

	private AID _marketPlaceAID;
	private ArrayList<HourlyEnergyProductivity> _energyProductivityList;
	ProducerAgent agent;

	public sendProducer_Productivity(ProducerAgent a) {
		this.agent = a;
		this._energyProductivityList = a.get_energyProductivityList();
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
			System.out.println(
					"ProducerAgent " + agent.getAID().getName() + " SendProducerProductivityInfoBehaviour: cannot find MarketPlace AID");
			e.printStackTrace();
		}

		// Send producer's productivity info to MarketPlace
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId("producerInfo");
		msg.addReceiver(this._marketPlaceAID);
		
		try {
			msg.setContentObject(this._energyProductivityList);
		} catch (IOException ex) {
			System.err.println("Cannot add Productivity Info to message. Sending empty message.");
			ex.printStackTrace(System.err);
		}
		agent.send(msg);
		System.out.println("-- ProducerAgent: Send " + this._energyProductivityList.size() + " Productivity Info from " + agent.getAID().getName() + " to " + this._marketPlaceAID.getName());
	}

}
