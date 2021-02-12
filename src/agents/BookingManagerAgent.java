package agents;

import java.util.PriorityQueue;

import concepts.BookingRequest;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import utils.BookingRequest_Comparator;

public class BookingManagerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	// Queue of BookingRequest, ordering with startTime.
	private PriorityQueue<BookingRequest> _bookingRequestQueue = new PriorityQueue<BookingRequest>(
			new BookingRequest_Comparator());

	protected void setup() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(this.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("bookingManager");
		serviceDescription.setName(this.getLocalName() + "-bookingManager");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(this, dfDescription);
			System.out.println("BookingManagerAgent " + getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("BookingManagerAgent " + getAID().getName() + " is ready.");
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

	protected void takeDown() {
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
