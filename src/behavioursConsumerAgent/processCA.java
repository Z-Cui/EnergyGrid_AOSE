package behavioursConsumerAgent;

import java.io.IOException;

import agents.ConsumerAgent;
import concepts.BookingRequest;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class processCA extends OneShotBehaviour{

	private static final long serialVersionUID = 1L;
	ConsumerAgent agent;
	AID _BookingManagerAID;
	int flag;

	public processCA(ConsumerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		ACLMessage message = agent.receive();
		BookingRequest bookingRequestBestProducer;
		
		
		this.flag = 1;
		
		if (message != null) {
			try {
				bookingRequestBestProducer = (BookingRequest) message.getContentObject();
				if (bookingRequestBestProducer.get_status()==0) {
					agent.set_ongoing_bookingReq(bookingRequestBestProducer);
					agent.get_ongoing_bookingReq().set_status(1);
					
					// Search the AID of the BookingManager agent
					DFAgentDescription dfDescription = new DFAgentDescription();
					ServiceDescription serviceDescription = new ServiceDescription();
					serviceDescription.setType("BookingManager");
					dfDescription.addServices(serviceDescription);

					try {
						DFAgentDescription[] BookingManager = DFService.search(myAgent, dfDescription);
						this._BookingManagerAID = BookingManager[0].getName();

					} catch (FIPAException e) {
						System.out.println(
								"ConsumerAgent " + agent.getAID().getName() + " processCA: cannot find BookingManager AID");
						e.printStackTrace();
					}
					
					// Send bookingRequestBestProducer to BookingManager
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.setConversationId("sendBestProducerBookingManager");
					msg.addReceiver(this._BookingManagerAID);
					try {
						msg.setContentObject(agent.get_ongoing_bookingReq());
					} catch (IOException ex) {
						System.err.println("Cannot add bookingRequestBestProducer to message. Sending empty message.");
						ex.printStackTrace(System.err);
					}
					myAgent.send(msg);
					System.out.println("Send profile from " + myAgent.getAID().getName() + " to " + this._BookingManagerAID.getName());
				}
				
				
				
				this.flag = 2;
			} catch (UnreadableException e) {
				System.err.println("Cannot get bookingRequestBestProducer");
				e.printStackTrace();
			}
			
			
				
			}
	}
	
	public int onEnd() {
		return this.flag;
	}
}
