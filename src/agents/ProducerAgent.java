package agents;

import java.util.ArrayList;

import concepts.HourlyEnergyProductivity;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ProducerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private ArrayList<HourlyEnergyProductivity> _energyProductivityList = new ArrayList<>();

	private double _profit;

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

		this._profit = 0;
	}

	// add a new consumption requirement
	public void addConsumptionRequirement(int _startTime, int _producedEnergyQuantity, String _producedEnergyType,
			double _pricePerQuantity) {

		this._energyProductivityList.add(new HourlyEnergyProductivity(this.getAID(), _startTime,
				_producedEnergyQuantity, _producedEnergyType, _pricePerQuantity));
	}
	
	// add received payment
	public void addReceivedPaymentToProfit(double p) {
		this.set_profit(this.get_profit() + p);
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

	// getters and setters
	public ArrayList<HourlyEnergyProductivity> get_energyProductivityList() {
		return _energyProductivityList;
	}

	public void set_energyProductivityList(ArrayList<HourlyEnergyProductivity> _energyProductivityList) {
		this._energyProductivityList = _energyProductivityList;
	}

	public double get_profit() {
		return _profit;
	}

	public void set_profit(double _profit) {
		this._profit = _profit;
	}

}
