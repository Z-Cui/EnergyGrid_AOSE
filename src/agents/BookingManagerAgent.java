package agents;

import java.util.PriorityQueue;

import behavioursBookingManagerAgent.finalizeBookingManager;
import behavioursBookingManagerAgent.initBookingManager;
import behavioursBookingManagerAgent.processBookingManager;
import concepts.BookingRequest;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import utils.BookingRequest_Comparator;

public class BookingManagerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_PROCESS = "process";
	private static final String BEHAVIOUR_FINALIZE = "finalize";

	// Queue of BookingRequest, ordering with startTime.
	private PriorityQueue<BookingRequest> _bookingRequestQueue = new PriorityQueue<BookingRequest>(
			new BookingRequest_Comparator());

	protected void setup() {

		FSMBehaviour behaviour = new FSMBehaviour(this);

		// states
		behaviour.registerFirstState(new initBookingManager(this), BEHAVIOUR_INIT);
		behaviour.registerState(new processBookingManager(this), BEHAVIOUR_PROCESS);
		behaviour.registerLastState(new finalizeBookingManager(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_PROCESS);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 1);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_FINALIZE, 2);

		addBehaviour(behaviour);
	}

	// add new received booking request to queue
	public void addBookingRequestToQueue(BookingRequest bReq) {
		// if this bq doesn't exist in queue, we add it.
		if (!this._bookingRequestQueue.contains(bReq)) {
			this._bookingRequestQueue.add(bReq);
		}
	}

	// remove a booking requirement from queue
	public void removeBookingRequirementFromQueue(BookingRequest bReq) {
		try {
			this._bookingRequestQueue.remove(bReq);
		} catch (Exception e) {
			System.out.print("Cannot remove BookingRequest from PriorityQueue in BookingManagerAgent");
			e.printStackTrace();
		}
	}

	public void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("BookingManagerAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("BookingManagerAgent " + getAID().getName() + " terminated.");
	}

	// getters and setters
	public PriorityQueue<BookingRequest> get_bookingRequestQueue() {
		return _bookingRequestQueue;
	}

	public void set_bookingRequestQueue(PriorityQueue<BookingRequest> _bookingRequestQueue) {
		this._bookingRequestQueue = _bookingRequestQueue;
	}

}
