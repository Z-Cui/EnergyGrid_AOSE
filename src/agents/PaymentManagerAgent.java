package agents;

import java.util.PriorityQueue;

import concepts.PaymentRequest;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import utils.PaymentRequest_Comparator;

public class PaymentManagerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	// Queue of BookingRequest, ordering with startTime.
	private PriorityQueue<PaymentRequest> _paymentRequestQueue = new PriorityQueue<PaymentRequest>(
			new PaymentRequest_Comparator());

	protected void setup() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(this.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("paymentManager");
		serviceDescription.setName(this.getLocalName() + "-paymentManager");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(this, dfDescription);
			System.out.println("PaymentManagerAgent " + getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("PaymentManagerAgent " + getAID().getName() + " is ready.");
	}

	// add new received booking request to queue
	public void addPaymentRequestToQueue(PaymentRequest pReq) {
		// if this bq doesn't exist in queue, we add it.
		if (!this._paymentRequestQueue.contains(pReq)) {
			this._paymentRequestQueue.add(pReq);
		}
	}

	// remove a booking requirement from queue
	public void removeBookingRequirementFromQueue(PaymentRequest pReq) {
		try {
			this._paymentRequestQueue.remove(pReq);
		} catch (Exception e) {
			System.out.print("Cannot remove PaymentRequest from PriorityQueue in PaymentManagerAgent");
			e.printStackTrace();
		}
	}

	protected void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("PaymentManagerAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("PaymentManagerAgent " + getAID().getName() + " terminated.");
	}

}
