package behavioursConsumerAgent;

import java.io.IOException;

import agents.ConsumerAgent;
import concepts.BookingRequest;
import concepts.PaymentRequest;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class processConsumer extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	ConsumerAgent agent;
	AID _bookingManagerAID;
	AID _paymentManagerAID;
	int flag = 0;

	public processConsumer(ConsumerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {

		agent.doWait(200);

		// receive messages
		ACLMessage msg_receive = agent.receive();

		if (msg_receive != null) {

			if (msg_receive.getConversationId() == "noProducerForThisRequirement") {
				this.flag = 1;
			}

			// received a booking request object
			else if (msg_receive.getConversationId() == "bookingRequest") {
				try {
					BookingRequest bookingRequest = (BookingRequest) msg_receive.getContentObject();
					// Search the AID of the BookingManager agent
					DFAgentDescription dfDescription = new DFAgentDescription();
					ServiceDescription serviceDescription = new ServiceDescription();
					serviceDescription.setType("BookingManager");
					dfDescription.addServices(serviceDescription);
					try {
						DFAgentDescription[] bookingManager = DFService.search(agent, dfDescription);
						this._bookingManagerAID = bookingManager[0].getName();
					} catch (FIPAException e) {
						System.out.println(
								"ConsumerAgent " + agent.getAID().getName() + " cannot find BookingManager AID");
						e.printStackTrace();
					}

					switch (bookingRequest.get_status()) {
					case -1:
						// does not find producer
						System.out.println("processConsumer - cannot find any producer");
						break;
					case 0:
						if (agent.canPay(bookingRequest) && agent.getProfile()
								.get_maximumBudgetPerQuantity() >= bookingRequest.get_pricePerUnit()) {

							// send it to booking manager agent
							BookingRequest bookingReq = new BookingRequest(bookingRequest);
							bookingReq.set_status(1);

							ACLMessage msg_send = new ACLMessage(ACLMessage.INFORM);
							msg_send.setConversationId("bookingRequest");
							msg_send.addReceiver(this._bookingManagerAID);
							try {
								msg_send.setContentObject(bookingReq);
							} catch (IOException ex) {
								System.err.println("Cannot add BookingRequest to message. Sending empty message.");
								ex.printStackTrace(System.err);
							}
							agent.send(msg_send);
							System.out.println("-- Consumer: Confirmed and Send Booking Request from "
									+ agent.getAID().getName() + " to " + this._bookingManagerAID.getName());
						} else
							bookingRequest.get_status();
						// change req info to match budget and send it to Selector
						break;
					case 2: // booking request is accepted by producer
						// consumer generates a payment
						if (agent.canPay(bookingRequest)) {
							double toPay = bookingRequest.get_pricePerUnit()
									* bookingRequest.get_reservedEnergyQuantity();
							agent.set_cashBalance(agent.get_cashBalance() - toPay);
							PaymentRequest paymentRequest = new PaymentRequest(bookingRequest, toPay, 0);
							ACLMessage msg_send = new ACLMessage(ACLMessage.INFORM);
							msg_send.setConversationId("paymentRequest");

							// Search the AID of the PaymentManager agent
							DFAgentDescription dfDescription2 = new DFAgentDescription();
							ServiceDescription serviceDescription2 = new ServiceDescription();
							serviceDescription2.setType("PaymentManager");
							dfDescription2.addServices(serviceDescription2);
							try {
								DFAgentDescription[] paymentManager = DFService.search(agent, dfDescription2);
								this._paymentManagerAID = paymentManager[0].getName();

							} catch (FIPAException e) {
								System.out.println("ConsumerAgent " + agent.getAID().getName()
										+ " cannot find PaymentManager AID");
								e.printStackTrace();
							}
							msg_send.addReceiver(this._paymentManagerAID);
							msg_send.setContentObject(paymentRequest);
							agent.send(msg_send);
							System.out.println("-- Consumer: Send Payment from " + agent.getAID().getName()
									+ " to PaymentManager");
						}
						break;
					case 3: // rejected by producer
						System.out.println("processConsumer - producer rejected the payment");
						break;
					}

				} catch (UnreadableException | IOException e) {
					e.printStackTrace();
				}
			}

			// received a payment request object
			else if (msg_receive.getConversationId() == "paymentRequest") {
				try {
					PaymentRequest paymentRequest = (PaymentRequest) msg_receive.getContentObject();

					// Search the AID of the PaymentManager agent
					DFAgentDescription dfDescription2 = new DFAgentDescription();
					ServiceDescription serviceDescription2 = new ServiceDescription();
					serviceDescription2.setType("PaymentManager");
					dfDescription2.addServices(serviceDescription2);
					try {
						DFAgentDescription[] paymentManager = DFService.search(agent, dfDescription2);
						this._paymentManagerAID = paymentManager[0].getName();

					} catch (FIPAException e) {
						System.out.println(
								"ConsumerAgent " + agent.getAID().getName() + " cannot find PaymentManager AID");
						e.printStackTrace();
					}

					switch (paymentRequest.get_status()) {
					case 0: // refused payment, producer is not valid
					case -1: // the booking request is expired

						// money is back
						agent.set_cashBalance(agent.get_cashBalance() + paymentRequest.get_money());
						// to-do restart payment.
						System.out.println("In processConsumer, need to restart payment (not developed yet)");
						break;
					case 1: // producer received payment
						// energy is reserved, consumer gets utility.
						agent.addUtility(paymentRequest);
						agent.deleteConsumptionRequirement(paymentRequest.get_bq().get_startTime());
						System.out.println("-- Consumer: " + agent.getAID().getName()
								+ " energy is reserved, gets utility, current utility: " + agent.get_cumulatedUtility()
								+ ", remaining consumption requirements: " + agent.getConReqList().size());
						break;
					}

				} catch (UnreadableException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public int onEnd() {
		return agent.satisfied();
	}
}
