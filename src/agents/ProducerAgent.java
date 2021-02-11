package agents;

import concepts.HourlyEnergyProductivity;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ProducerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private HourlyEnergyProductivity _energyProductivity;
	private double _cashBalance;

	protected void setup() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(this.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("producer");
		serviceDescription.setName(this.getLocalName() + "-producer");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(this, dfDescription);
			System.out.println("ProducerAgent " + getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ProducerAgent " + getAID().getName() + " is ready.");
	}

	protected void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("ProducerAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ProducerAgent " + getAID().getName() + " terminated.");
	}

	public HourlyEnergyProductivity get_energyProductivity() {
		return _energyProductivity;
	}

	public void set_energyProductivity(HourlyEnergyProductivity _energyProductivity) {
		this._energyProductivity = _energyProductivity;
	}

	public double get_cashBalance() {
		return _cashBalance;
	}

	public void set_cashBalance(double _cashBalance) {
		this._cashBalance = _cashBalance;
	}

}
