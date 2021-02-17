package behavioursPaymentManagerAgent;

import java.io.IOException;

import agents.PaymentManagerAgent;
import concepts.PaymentRequest;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class processPaymentManager extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	PaymentManagerAgent agent;

	public processPaymentManager(PaymentManagerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		ACLMessage msg_receive = agent.receive();

		if (msg_receive != null) {

			try {
				if (msg_receive.getConversationId() == "paymentRequest") {

					ACLMessage msg_send = new ACLMessage(ACLMessage.INFORM);
					msg_send.setConversationId("paymentRequest");

					PaymentRequest paymentRequest = (PaymentRequest) msg_receive.getContentObject();

					switch (paymentRequest.get_status()) {
					case 0: // consumer sent to PaymentManager
						msg_send.addReceiver(paymentRequest.get_bq().get_producerId());
						msg_send.setContentObject(paymentRequest);
						agent.send(msg_send);
						System.out.println("-- PaymentManagerAgent: Received PaymentRequest from Consumer "
								+ paymentRequest.get_bq().get_consumerId().getName() + " regarding Producer "
								+ paymentRequest.get_bq().get_producerId().getName());
						break;
					case 1: // producer accepted it and need to send to consumer
						msg_send.addReceiver(paymentRequest.get_bq().get_consumerId());
						msg_send.setContentObject(paymentRequest);
						agent.send(msg_send);
						System.out.println("-- PaymentManagerAgent: Send accepted " + " by Producer "
								+ paymentRequest.get_bq().get_producerId().getName()
								+ " regarding PaymentRequest to Consumer "
								+ paymentRequest.get_bq().get_consumerId().getName());
						break;
					default:
						System.out.println("-- PaymentManagerAgent: Received an unkown payment request status");
					}
				}
			} catch (UnreadableException | IOException e) {
				System.err.println("-- PaymentManagerAgent: Cannot get booking request info from message");
				e.printStackTrace();
			}
		}
	}

	public int onEnd() {
		return 1;
	}
}
