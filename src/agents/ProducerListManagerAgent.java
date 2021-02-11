package agents;

import java.util.ArrayList;

import concepts.HourlyEnergyProductivity;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ProducerListManagerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private ArrayList<HourlyEnergyProductivity> _producerList = new ArrayList<>();

	protected void setup() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(this.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producerListManager");
		serviceDescription.setName(this.getLocalName() + "-producerListManager");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(this, dfDescription);
			System.out.println("ProducerListManagerAgent " + getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ProducerListManagerAgent " + getAID().getName() + " is ready.");
	}

	protected void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("ProducerListManagerAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ProducerListManagerAgent " + getAID().getName() + " terminated.");
	}

	public ArrayList<HourlyEnergyProductivity> get_producerList() {
		return _producerList;
	}

	public void set_producerList(ArrayList<HourlyEnergyProductivity> _producerList) {
		this._producerList = _producerList;
	}

}
