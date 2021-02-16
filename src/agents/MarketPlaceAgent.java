package agents;

import java.util.ArrayList;
import java.util.PriorityQueue;

import behavioursMarketPlaceAgent.finalizeMarketPlace;
import behavioursMarketPlaceAgent.initMarketPlace;
import behavioursMarketPlaceAgent.processMarketPlace;
import concepts.HourlyEnergyProductivity;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import utils.HourlyEnergyProductivity_Comparator;

public class MarketPlaceAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_PROCESS = "process";
	private static final String BEHAVIOUR_FINALIZE = "finalize";

	// Queue of producers' EnergyProductivity, ordering with startTime and price.
	private PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue = new PriorityQueue<HourlyEnergyProductivity>(
			new HourlyEnergyProductivity_Comparator());

	protected void setup() {
		FSMBehaviour behaviour = new FSMBehaviour(this);

		// states
		behaviour.registerFirstState(new initMarketPlace(this), BEHAVIOUR_INIT);
		behaviour.registerState(new processMarketPlace(this), BEHAVIOUR_PROCESS);
		behaviour.registerLastState(new finalizeMarketPlace(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_PROCESS);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 1);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_FINALIZE, 2);

		addBehaviour(behaviour);
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

	public void takeDown() {
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
