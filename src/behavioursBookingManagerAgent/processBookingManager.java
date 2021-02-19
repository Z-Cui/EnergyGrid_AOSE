package behavioursBookingManagerAgent;

import java.io.IOException;

import agents.BookingManagerAgent;
import concepts.BookingRequest;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class processBookingManager extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	BookingManagerAgent agent;

	public processBookingManager(BookingManagerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		ACLMessage msg_receive = agent.receive();
		
		agent.doWait(200);

		if (msg_receive != null) {

			try {
				if (msg_receive.getConversationId() == "bookingRequest") {

					ACLMessage msg_send = new ACLMessage(ACLMessage.INFORM);
					msg_send.setConversationId("bookingRequest");

					BookingRequest bookingRequest = (BookingRequest) msg_receive.getContentObject();

					switch (bookingRequest.get_status()) {
					case 1: // consumer confirmed it and need to send to producer
						msg_send.addReceiver(bookingRequest.get_producerId());
						msg_send.setContentObject(bookingRequest);
						agent.send(msg_send);
						System.out.println("-- BookingManagerAgent: Received BookingRequest from Consumer "
								+ bookingRequest.get_consumerId().getName() + " regarding Producer "
								+ bookingRequest.get_producerId().getName());
						break;
					case 2: // producer accepted it and need to send to consumer
						msg_send.addReceiver(bookingRequest.get_consumerId());
						msg_send.setContentObject(bookingRequest);
						agent.send(msg_send);
						System.out.println("-- BookingManagerAgent: Send accepted BookingRequest of Consumer "
								+ bookingRequest.get_consumerId().getName() + " by Producer "
								+ bookingRequest.get_producerId().getName());
						break;
					case 3: // producer rejected it and need to inform consumer
						msg_send.addReceiver(bookingRequest.get_consumerId());
						msg_send.setContentObject(bookingRequest);
						agent.send(msg_send);
						System.out.println("-- BookingManagerAgent: Send rejected BookingRequest of Consumer "
								+ bookingRequest.get_consumerId().getName() + " by Producer "
								+ bookingRequest.get_producerId().getName());
						break;
					default:
						System.out.println("-- BookingManagerAgent: Received an unkown booking request status");
					}
				}
			} catch (UnreadableException | IOException e) {
				System.err.println("-- BookingManagerAgent: Cannot get booking request info from message");
				e.printStackTrace();
			}
		}
	}

	public int onEnd() {
		// System.out.println("--- Mkpc " + agent.get_energyProductivityQueue().size() +
		// "prod.");
		return 1;
	}
}
