package agents;

import java.util.PriorityQueue;

import behavioursPaymentManagerAgent.finalizePaymentManager;
import behavioursPaymentManagerAgent.initPaymentManager;
import behavioursPaymentManagerAgent.processPaymentManager;
import concepts.PaymentRequest;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import utils.PaymentRequest_Comparator;

public class PaymentManagerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_PROCESS = "process";
	private static final String BEHAVIOUR_FINALIZE = "finalize";

	// Queue of BookingRequest, ordering with startTime.
	private PriorityQueue<PaymentRequest> _paymentRequestQueue = new PriorityQueue<PaymentRequest>(
			new PaymentRequest_Comparator());

	protected void setup() {
		FSMBehaviour behaviour = new FSMBehaviour(this);

		// states
		behaviour.registerFirstState(new initPaymentManager(this), BEHAVIOUR_INIT);
		behaviour.registerState(new processPaymentManager(this), BEHAVIOUR_PROCESS);
		behaviour.registerLastState(new finalizePaymentManager(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_PROCESS);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 1);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_FINALIZE, 2);

		addBehaviour(behaviour);
	}

	// Add new received booking request to queue
	public void addPaymentRequestToQueue(PaymentRequest pReq) {
		// if this payment request doesn't exist in queue, we add it.
		if (!this._paymentRequestQueue.contains(pReq)) {
			this._paymentRequestQueue.add(pReq);
		}
	}

	public void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("PaymentManagerAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("PaymentManagerAgent " + getAID().getName() + " terminated.");
	}

	// getters and setters
	public PriorityQueue<PaymentRequest> get_paymentRequestQueue() {
		return _paymentRequestQueue;
	}

	public void set_paymentRequestQueue(PriorityQueue<PaymentRequest> _paymentRequestQueue) {
		this._paymentRequestQueue = _paymentRequestQueue;
	}

}
