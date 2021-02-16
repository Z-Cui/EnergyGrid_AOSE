package behavioursProducer;

import agents.ProducerAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class processProducer extends OneShotBehaviour {

	private static final long serialVersionUID = 1L;
	ProducerAgent agent;
	AID _bookingManagerAID;
	int flag;

	public processProducer(ProducerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		ACLMessage message = agent.receive();

		this.flag = 1;

		if (message != null) {

			this.flag = 2;

		}
	}

	public int onEnd() {
		return this.flag;
	}
}
