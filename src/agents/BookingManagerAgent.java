package agents;

import java.util.ArrayList;

import concepts.BookingRequest;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class BookingManagerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private ArrayList<BookingRequest> _bookingList = new ArrayList<>();

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

	public ArrayList<BookingRequest> get_bookingList() {
		return _bookingList;
	}

	public void set_bookingList(ArrayList<BookingRequest> _bookingList) {
		this._bookingList = _bookingList;
	}
	
}
