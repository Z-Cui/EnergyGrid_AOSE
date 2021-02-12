package agents;

import java.util.PriorityQueue;

import concepts.HourlyEnergyProductivity;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import utils.HourlyEnergyProductivity_Comparator;

public class ProducerListManagerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	// Queue of producers' EnergyProductivity, ordering with startTime and price.
	private PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue = new PriorityQueue<HourlyEnergyProductivity>(
			new HourlyEnergyProductivity_Comparator());

	protected void setup() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(this.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerListManager");
		serviceDescription.setName(this.getLocalName() + "-producerListManager");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(this, dfDescription);
			System.out.println("ProducerListManagerAgent " + getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ProducerListManagerAgent " + getAID().getName() + " is ready.");
	}

	protected void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("ProducerListManagerAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ProducerListManagerAgent " + getAID().getName() + " terminated.");
	}

	// getters and setters
	public PriorityQueue<HourlyEnergyProductivity> get_energyProductivityQueue() {
		return _energyProductivityQueue;
	}

	public void set_energyProductivityQueue(PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue) {
		this._energyProductivityQueue = _energyProductivityQueue;
	}

}
