package agents;

import java.util.ArrayList;

import concepts.HourlyConsumptionRequirement;
import concepts.Profile;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ProducerSelectorAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private ArrayList<HourlyConsumptionRequirement> _conReqList = new ArrayList<>();;
	private Profile _profile;
	private double _cashBalance;

	protected void setup() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(this.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("consumer");
		serviceDescription.setName(this.getLocalName() + "-consumer");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(this, dfDescription);
			System.out.println("ConsumerAgent " + getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ConsumerAgent " + getAID().getName() + " is ready.");
	}

	protected void takeDown() {
		// De-registration
		try {
			DFService.deregister(this);
			System.out.println("ConsumerAgent " + getAID().getName() + " de-registered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		System.out.println("ConsumerAgent " + getAID().getName() + " terminated.");
	}

	public ArrayList<HourlyConsumptionRequirement> getConReqList() {
		return this._conReqList;
	}

	public void setConReqList(ArrayList<HourlyConsumptionRequirement> conReqList) {
		this._conReqList = conReqList;
	}

	public Profile getProfile() {
		return this._profile;
	}

	public void setProfile(Profile profile) {
		this._profile = profile;
	}

	public double get_cashBalance() {
		return _cashBalance;
	}

	public void set_cashBalance(double _cashBalance) {
		this._cashBalance = _cashBalance;
	}

}
