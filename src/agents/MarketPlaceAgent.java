package agents;

import java.util.ArrayList;
import java.util.PriorityQueue;

import concepts.HourlyEnergyProductivity;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import utils.HourlyEnergyProductivity_Comparator;

public class MarketPlaceAgent extends Agent {
	private static final long serialVersionUID = 1L;

	// Queue of producers' EnergyProductivity, ordering with startTime and price.
	private PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue = 
			new PriorityQueue<HourlyEnergyProductivity>(new HourlyEnergyProductivity_Comparator());

	protected void setup() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(this.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("marketPlace");
		serviceDescription.setName(this.getLocalName() + "-marketPlace");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(this, dfDescription);
			System.out.println("MarketPlaceAgent " + getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("MarketPlaceAgent " + getAID().getName() + " is ready.");
	}
	
	// add an ArrayList of productivity info to queue
	public void addArrayListProducerInfoToQueue(ArrayList<HourlyEnergyProductivity> list) {
		for (int i = 0; i < list.size(); i++) {
			addProducerInfoToQueue(list.get(i));
		}
	}

	// add a new received productivity info to queue
	public void addProducerInfoToQueue(HourlyEnergyProductivity p) {
		this._energyProductivityQueue.add(p);
	}

	// remove automatically expired info
	public void removeExpiredInfo(int currentTime) {
		// if the first info is expired,
		while (this._energyProductivityQueue.peek().get_startTime() < currentTime) {
			// then remove it from queue
			this._energyProductivityQueue.poll();
		}
	}

	protected void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("MarketPlaceAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("MarketPlaceAgent " + getAID().getName() + " terminated.");
	}

	// getters and setters
	public PriorityQueue<HourlyEnergyProductivity> get_energyProductivityQueue() {
		return _energyProductivityQueue;
	}

	public void set_energyProductivityQueue(PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue) {
		this._energyProductivityQueue = _energyProductivityQueue;
	}

}
