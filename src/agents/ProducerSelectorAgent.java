package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import behavioursProducerSelectorAgent.listenProducerSelector;
import behavioursProducerSelectorAgent.processProducerSelector;
import behavioursProducerSelectorAgent.finalizeProducerSelector;
import behavioursProducerSelectorAgent.initProducerSelector;
import concepts.BookingRequest;
import concepts.HourlyConsumptionRequirement;
import concepts.HourlyEnergyProductivity;
import concepts.Profile;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import utils.HourlyConsumptionRequirement_Comparator;
import utils.HourlyEnergyProductivity_Comparator;

public class ProducerSelectorAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_LISTEN = "listen";
	private static final String BEHAVIOUR_PROCESS = "process";
	private static final String BEHAVIOUR_FINALIZE = "finalize";

	// Queue of consumers' ConsumptionRequirement, ordering with startTime.
	private PriorityQueue<HourlyConsumptionRequirement> _consumptionRequirementQueue = new PriorityQueue<HourlyConsumptionRequirement>(
			new HourlyConsumptionRequirement_Comparator());

	// HashMap to save consumers' profile
	private HashMap<String, Profile> _profileHashMap = new HashMap<>();

	// Queue of producers' EnergyProductivity, ordering with startTime and price.
	private PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue = new PriorityQueue<HourlyEnergyProductivity>(
			new HourlyEnergyProductivity_Comparator());

	// History of proposals (booking requests) of best producer recommendations
	private ArrayList<BookingRequest> _historical_proposals = new ArrayList<>();

	private Profile ongoing_profile;

	protected void setup() {
		FSMBehaviour behaviour = new FSMBehaviour(this);

		// states
		behaviour.registerFirstState(new initProducerSelector(this), BEHAVIOUR_INIT);
		behaviour.registerState(new listenProducerSelector(this), BEHAVIOUR_LISTEN);
		behaviour.registerState(new processProducerSelector(this), BEHAVIOUR_PROCESS);
		behaviour.registerLastState(new finalizeProducerSelector(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_LISTEN);
		behaviour.registerTransition(BEHAVIOUR_LISTEN, BEHAVIOUR_LISTEN, 0);
		behaviour.registerTransition(BEHAVIOUR_LISTEN, BEHAVIOUR_PROCESS, 1);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 0);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_LISTEN, 1);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_FINALIZE, 2);
		behaviour.registerTransition(BEHAVIOUR_LISTEN, BEHAVIOUR_FINALIZE, 2);

		addBehaviour(behaviour);
	}

	public int ProcessAllRequirements() {

		int FlagNoReq = 0;
		// FlagNoReq, 1 if _consumptionRequirementQueue is Empty
		// Flag for transitions from process

		if (this.get_energyProductivityQueue().size() == 0)
			return 1;

		Iterator<HourlyConsumptionRequirement> it = this._consumptionRequirementQueue.iterator();

		PriorityQueue<HourlyConsumptionRequirement> newQueue = new PriorityQueue<HourlyConsumptionRequirement>(
				new HourlyConsumptionRequirement_Comparator());

		while (it.hasNext()) {

			// loop: every consumption requirement
			HourlyConsumptionRequirement req = it.next();
			// this.removeConsumptionRequirementFromQueue(req);

			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(req.get_consumerId());

			// find the best producer (info encapsulated in BookingRequest type)
			BookingRequest bookingRequestBestProducer = SelectProducerForOneRequirement(req,
					this.get_energyProductivityQueue());

			switch (bookingRequestBestProducer.get_status()) {
			case -1: // did not find a producer who maximizes utility
				msg.setConversationId("noProducerForThisRequirement");
				newQueue.add(req);
				try {
					msg.setContentObject(bookingRequestBestProducer);
				} catch (Exception e) {
					System.err.println(
							"Cannot add Unsatisfiable ConsumptionRequirement to message. Sending empty message.");
					e.printStackTrace();
				}
				System.out.println("-- ProducerSelector: Cannot find a producer for the requirement " + req.toString());
				break;
			case 0: // found the best producer
				// add the generated booking request proposal to queue (history)
				this._historical_proposals.add(bookingRequestBestProducer);

				// send proposal to consumer
				msg.setConversationId("bookingRequest");
				try {
					msg.setContentObject(bookingRequestBestProducer);
				} catch (Exception ex) {
					System.err.println("Cannot add bookingRequestBestProducer to message. Sending empty message.");
					ex.printStackTrace(System.err);
				}
				System.out.println("-- ProducerSelector: Found best producter for "
						+ bookingRequestBestProducer.get_consumerId().getName());
				break;
			}
			this.send(msg);
		}

		this.set_consumptionRequirementQueue(newQueue);

		if (this.get_consumptionRequirementQueue().isEmpty()) {
			FlagNoReq = 1;
			System.out.println("!!! ProducerSelectorAgent: Now the queue for consumption requirements is EMPTY");
		}
		else {
			System.out.println("!!! ProducerSelectorAgent: Tried to find producers, but some consumption requirements are unsatisfiable");
		}

		return FlagNoReq;
	}

	// Select a producer maximize utility for a consumption requirement req
	public BookingRequest SelectProducerForOneRequirement(HourlyConsumptionRequirement req,
			PriorityQueue<HourlyEnergyProductivity> prodQueue) {

		// load this consumer's profile
		this.ongoing_profile = this.findProfileFromAID(req.get_consumerId());
		// create a new booking request
		BookingRequest ongoing_bookingReq = new BookingRequest();

		// initialize parameters of this new booking request
		ongoing_bookingReq.set_consumerId(req.get_consumerId());
		ongoing_bookingReq.set_startTime(req.get_startTime());
		ongoing_bookingReq.set_status(-1);

		// find a producer maximize this consumer's utility
		double utility_max = 0;
		Iterator<HourlyEnergyProductivity> it = prodQueue.iterator();
		while (it.hasNext()) {

			HourlyEnergyProductivity prod = it.next();

			// if the time parameter does not match
			if (prod.get_startTime() != req.get_startTime()) {
				continue;
			}

			// find a reservable quantity with this producer: min(requirement, productivity)
			// consumer needs 100, producer has -10.
			int _q = req.get_consumptionQuantity() < prod.get_producedEnergyQuantity() ? req.get_consumptionQuantity()
					: prod.get_producedEnergyQuantity();
			// If reservable quantity < 0, there is an error
			if (_q < 0) {
				System.out.println("Error in ProducerProductivity");
				continue;
			}

			// calculate utility
			double utility = CalculateUtility(req, this.ongoing_profile, prod, _q);

			// if this producer's offer brings higher utility OR same utility but preferred
			// energy type
			if (utility > utility_max || (utility == utility_max
					&& prod.get_producedEnergyType().equals(this.ongoing_profile.get_preferredEnergyType()))) {
				utility_max = utility;
				ongoing_bookingReq.set_producerId(prod.get_producerId());
				ongoing_bookingReq.set_pricePerUnit(prod.get_pricePerUnit());
				ongoing_bookingReq.set_reservedEnergyType(prod.get_producedEnergyType());
				ongoing_bookingReq.set_reservedEnergyQuantity(_q);
				ongoing_bookingReq.set_status(0);
			}
		}

		// verify if this booking request has been proposed.
		// if yes, need to choose another producer
		if (this.get_historical_proposals().isEmpty()) {
			return ongoing_bookingReq;
		} else {
			int need_another_producer = 0;
			PriorityQueue<HourlyEnergyProductivity> newQueue = new PriorityQueue<HourlyEnergyProductivity>(
					new HourlyEnergyProductivity_Comparator());
			for (int i = 0; i < this.get_historical_proposals().size(); i++) {

				BookingRequest proposal = this.get_historical_proposals().get(i);

				if (proposal.equals(ongoing_bookingReq)) {
					Iterator<HourlyEnergyProductivity> it2 = this.get_energyProductivityQueue().iterator();
					while (it2.hasNext()) {
						HourlyEnergyProductivity prod = it2.next();
						// if same time, type, producerId between previous proposal and current best
						// producer
						if (!(prod.get_producerId().equals(proposal.get_producerId())
								&& prod.get_startTime() == proposal.get_startTime()
								&& prod.get_producedEnergyType().equals(proposal.get_reservedEnergyType()))) {
							newQueue.add(prod);
						} else
							need_another_producer = 1;
					}
				}
			}

			return (need_another_producer == 0) ? ongoing_bookingReq : SelectProducerForOneRequirement(req, newQueue);
		}

	}

	// calculate the utility
	public double CalculateUtility(HourlyConsumptionRequirement req, Profile profile, HourlyEnergyProductivity prod,
			int quantity) {

		double _p = prod.get_pricePerUnit();
		int _q = quantity;
		if (prod.get_producedEnergyQuantity() < _q && prod.get_producedEnergyQuantity() >= 0)
			_q = prod.get_producedEnergyQuantity();
		String _type = prod.get_producedEnergyType();

		double _k = profile.get_paramK();
		double _b;
		if (_type == "Renewable")
			_b = this.ongoing_profile.get_paramB_renewable();
		else
			_b = this.ongoing_profile.get_paramB_nonRenewable();
		/*
		 * utility = (K-P)*Q + B; K: utility from every one unit of quantity; P: price
		 * per energy unit; Q: energy quantity; B: utility for different energy type
		 */
		return (_k - _p) * _q + _b;
	}

	// add new received consumption requirement to queue
	public void addConsumptionRequirementToQueue(HourlyConsumptionRequirement consReq) {
		// if this req doesn't exist in queue, we add it.
		if (!this._consumptionRequirementQueue.contains(consReq)) {
			this._consumptionRequirementQueue.add(consReq);
		}
	}

	// add new profile to profile-hashmap: key = AID.toString()
	public void addProfileToHashMap(Profile profile) {

		String _key = profile.get_consumerId().toString();

		// if this profile exists in HashMap, we update it (delete existing + add new)
		if (this._profileHashMap.containsKey(_key)) {
			this._profileHashMap.remove(_key);
			this._profileHashMap.put(_key, profile);
		} else
			this._profileHashMap.put(_key, profile);
	}

	// find a profile from AID
	public Profile findProfileFromAID(AID aid) {
		return _profileHashMap.get(aid.toString());
	}

	public void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("ProducerSelectorAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ProducerSelectorAgent " + getAID().getName() + " terminated.");
	}

	// getters and setters
	public PriorityQueue<HourlyConsumptionRequirement> get_consumptionRequirementQueue() {
		return _consumptionRequirementQueue;
	}

	public void set_consumptionRequirementQueue(
			PriorityQueue<HourlyConsumptionRequirement> _consumptionRequirementQueue) {
		this._consumptionRequirementQueue = _consumptionRequirementQueue;
	}

	public HashMap<String, Profile> get_profileHashMap() {
		return _profileHashMap;
	}

	public void set_profileHashMap(HashMap<String, Profile> _profileHashMap) {
		this._profileHashMap = _profileHashMap;
	}

	public PriorityQueue<HourlyEnergyProductivity> get_energyProductivityQueue() {
		return _energyProductivityQueue;
	}

	public void set_energyProductivityQueue(PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue) {
		this._energyProductivityQueue = _energyProductivityQueue;
	}

	public Profile getOngoing_profile() {
		return ongoing_profile;
	}

	public void setOngoing_profile(Profile ongoing_profile) {
		this.ongoing_profile = ongoing_profile;
	}

	public ArrayList<BookingRequest> get_historical_proposals() {
		return _historical_proposals;
	}

	public void set_historical_proposals(ArrayList<BookingRequest> _historical_proposals) {
		this._historical_proposals = _historical_proposals;
	}

}
