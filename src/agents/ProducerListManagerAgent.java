package agents;

import java.util.PriorityQueue;

import behavioursProducerListManagerAgent.finalizeProducerListManager;
import behavioursProducerListManagerAgent.initProducerListManager;
import behavioursProducerListManagerAgent.shareProducerListManager;
import behavioursProducerListManagerAgent.updateProducerListManager;
import concepts.HourlyEnergyProductivity;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import utils.HourlyEnergyProductivity_Comparator;

public class ProducerListManagerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_UPDATE = "update";
	private static final String BEHAVIOUR_SHARE = "share";
	private static final String BEHAVIOUR_FINALIZE = "finalize";

	// Queue of producers' EnergyProductivity, ordering with startTime and price.
	private PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue = new PriorityQueue<HourlyEnergyProductivity>(
			new HourlyEnergyProductivity_Comparator());

	protected void setup() {

		FSMBehaviour behaviour = new FSMBehaviour(this);

		// states
		behaviour.registerFirstState(new initProducerListManager(this), BEHAVIOUR_INIT);
		behaviour.registerState(new updateProducerListManager(this), BEHAVIOUR_UPDATE);
		behaviour.registerState(new shareProducerListManager(this), BEHAVIOUR_SHARE);
		behaviour.registerLastState(new finalizeProducerListManager(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_UPDATE);
		behaviour.registerDefaultTransition(BEHAVIOUR_UPDATE, BEHAVIOUR_SHARE);
		behaviour.registerDefaultTransition(BEHAVIOUR_SHARE, BEHAVIOUR_UPDATE);
		behaviour.registerTransition(BEHAVIOUR_UPDATE, BEHAVIOUR_FINALIZE, 2);

		addBehaviour(behaviour);

	}

	public void takeDown() {
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
