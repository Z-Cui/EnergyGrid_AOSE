package behavioursProducer;

import java.io.IOException;

import agents.ProducerAgent;
import concepts.BookingRequest;
import concepts.PaymentRequest;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class processProducer extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	ProducerAgent agent;
	int flag;

	public processProducer(ProducerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		ACLMessage msg_receive = agent.receive();

		this.flag = 1;

		if (msg_receive != null) {
			if (msg_receive.getConversationId() == "bookingRequest") {
				try {
					BookingRequest bookingRequest = (BookingRequest) msg_receive.getContentObject();

					ACLMessage msg_send = new ACLMessage(ACLMessage.INFORM);
					msg_send.setConversationId("bookingRequest");
					msg_send.addReceiver(msg_receive.getSender());

					// accept: 2; reject: 3
					int new_status = agent.acceptOrReject(bookingRequest);
					bookingRequest.set_status(new_status);
					try {
						msg_send.setContentObject(bookingRequest);
						agent.send(msg_send);
						if (new_status == 2) {
							System.out.println("-- Producer: Accepted and sending Booking Requirement for "
									+ bookingRequest.get_consumerId().getName() + " to "
									+ msg_receive.getSender().getName());
						} else if (new_status == 3) {
							System.out.println("-- Producer: Rejected and sending Booking Requirement for "
									+ bookingRequest.get_consumerId().getName() + " to "
									+ msg_receive.getSender().getName());
						} else {
							System.out.println(
									"-- Producer: unknown status for Booking Request -" + bookingRequest.toString());
						}

					} catch (IOException e) {
						System.out.println("Cannot add Booking Request to message");
						e.printStackTrace();
					}

				} catch (UnreadableException e) {
					System.out.println("Cannot read Booking Request from message");
					e.printStackTrace();
				}
			} else if (msg_receive.getConversationId() == "paymentRequest") {
				// receive a payment
				try {
					PaymentRequest paymentRequest = (PaymentRequest) msg_receive.getContentObject();
					if (paymentRequest.get_status() == 0) {
						int confirmPayment = agent.processPayment(paymentRequest);
						switch(confirmPayment) {
						case 1:
							// success
							paymentRequest.set_status(1);
							ACLMessage msg_send = new ACLMessage(ACLMessage.INFORM);
							msg_send.setConversationId("paymentRequest");
							// send PaymentRequest from Producer to PaymentManager
							msg_send.addReceiver(msg_receive.getSender());
							msg_send.setContentObject(paymentRequest);
							agent.send(msg_send);
							System.out.println("-- Producer: Received and confirmed PaymentRequest, send it back to PaymentManager");
							break;
						case -1:
							// failure
							break;
						default:
							// unknown status
							break;	
						}
						
					}
					
				} catch (UnreadableException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int onEnd() {
		return this.flag;
	}
}
