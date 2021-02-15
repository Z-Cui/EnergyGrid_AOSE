package agents;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import behavioursProducerSelectorAgent.listenPS;
import behavioursProducerSelectorAgent.processPS;
import behavioursProducerSelectorAgent.finalizePS;
import behavioursProducerSelectorAgent.initPS;
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
	
	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_LISTEN = "listen";
	private static final String BEHAVIOUR_PROCESS = "process";
	private static final String BEHAVIOUR_FINALIZE = "finalize";
	
	private static final long serialVersionUID = 1L;

	// Queue of consumers' ConsumptionRequirement, ordering with startTime.
	private PriorityQueue<HourlyConsumptionRequirement> _consumptionRequirementQueue = new PriorityQueue<HourlyConsumptionRequirement>(
			new HourlyConsumptionRequirement_Comparator());

	// HashMap to save consumers' profile
	private HashMap<String, Profile> _profileHashMap = new HashMap<>();

	// Queue of producers' EnergyProductivity, ordering with startTime and price.
	private PriorityQueue<HourlyEnergyProductivity> _energyProductivityQueue = new PriorityQueue<HourlyEnergyProductivity>(
			new HourlyEnergyProductivity_Comparator());

	private Profile ongoing_profile;
	private BookingRequest ongoing_bookingReq;

	protected void setup() {
		FSMBehaviour behaviour = new FSMBehaviour(this);
		// states
		behaviour.registerFirstState(new initPS(this), BEHAVIOUR_INIT);
		behaviour.registerState(new listenPS(this), BEHAVIOUR_LISTEN);
		behaviour.registerState(new processPS(this), BEHAVIOUR_PROCESS);
		behaviour.registerLastState(new finalizePS(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_LISTEN);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 0);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_LISTEN, 1);
		behaviour.registerTransition(BEHAVIOUR_LISTEN, BEHAVIOUR_PROCESS, 0);
		behaviour.registerTransition(BEHAVIOUR_LISTEN, BEHAVIOUR_FINALIZE, 1);

		addBehaviour(behaviour);
	}

	// To-Do with Behaviour
	public int ProcessAllRequirements() {
		
		int FlagNoReq = 0;
		//FlagNoReq, 1 if _consumptionRequirementQueue is Empty
		//Flag for transitions from process
		
		Iterator<HourlyConsumptionRequirement> it = this._consumptionRequirementQueue.iterator();

		while (it.hasNext()) {
			// loop: every consumption requirement
			HourlyConsumptionRequirement req = it.next();

			// find the best producer (info encapsulated in BookingRequest type)
			BookingRequest bookingRequestBestProducer = SelectProducerForOneRequirement(req);

			// remove the success consumption requirement
			if (bookingRequestBestProducer != null) {
				
				// Send consumer's profile to ProducerSelector
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setConversationId("sendBestProducter");
				msg.addReceiver(bookingRequestBestProducer.get_consumerId());
				try {
					msg.setContentObject(bookingRequestBestProducer);
				} catch (IOException ex) {
					System.err.println("Cannot add bookingRequestBestProducer to message. Sending empty message.");
					ex.printStackTrace(System.err);
				}
				this.send(msg);
				System.out.println("Send best producter from " + getAID().getName() + " to " + bookingRequestBestProducer.get_consumerId().getName());
				
				this.removeConsumptionRequirementFromQueue(req);
			}
		}
		if (_consumptionRequirementQueue.isEmpty()) {
			FlagNoReq = 1;
		}
		
		
		return FlagNoReq;
	}
	
	// Flag for transitions from listen
	public int NoExistconsumptionRequirementQueue() {
		int FlagNoReq = 0;
		if (_consumptionRequirementQueue.isEmpty()) {
			FlagNoReq = 1;
		}
		return FlagNoReq;
		
	}

	// Select a producer maximize utility for a consumption requirement req
	public BookingRequest SelectProducerForOneRequirement(HourlyConsumptionRequirement req) {

		// load this consumer's profile
		this.ongoing_profile = this.findProfileFromAID(req.get_consumerId());
		// create a new booking request
		this.ongoing_bookingReq = new BookingRequest();

		// initialize parameters of this new booking request
		this.ongoing_bookingReq.set_consumerId(req.get_consumerId());
		this.ongoing_bookingReq.set_startTime(req.get_startTime());
		this.ongoing_bookingReq.set_status(-1);

		// find a producer maximize this consumer's utility
		double utility_max = 0;
		Iterator<HourlyEnergyProductivity> it = this._energyProductivityQueue.iterator();
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
			double utility = CalculateUtility(req, ongoing_profile, prod, _q);

			// if this producer's offer brings higher utility
			if (utility > utility_max) {
				utility_max = utility;
				this.ongoing_bookingReq.set_producerId(prod.get_producerId());
				this.ongoing_bookingReq.set_pricePerUnit(prod.get_pricePerUnit());
				this.ongoing_bookingReq.set_reservedEnergyType(prod.get_producedEnergyType());
				this.ongoing_bookingReq.set_reservedEnergyQuantity(_q);
				this.ongoing_bookingReq.set_status(0);
			}
		}

		// send the best producer within the created BookingRequest
		if (this.ongoing_bookingReq.get_status() == 0) {
			return this.ongoing_bookingReq;
		} else
			return null;
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

	// remove a consumption requirement from queue
	public void removeConsumptionRequirementFromQueue(HourlyConsumptionRequirement consReq) {
		try {
			this._consumptionRequirementQueue.remove(consReq);
		} catch (Exception e) {
			System.out.println("Cannot remove ConsumptionRequirement from PriorityQueue in ProducerSelectorAgent");
			e.printStackTrace();
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

}
