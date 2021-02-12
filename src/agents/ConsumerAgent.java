package agents;

import java.util.ArrayList;

import concepts.BookingRequest;
import concepts.HourlyConsumptionRequirement;
import concepts.Profile;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ConsumerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private ArrayList<HourlyConsumptionRequirement> _conReqList = new ArrayList<>();
	private Profile _profile;

	private double _cashBalance;
	private double _cumulatedUtility;
	
	private BookingRequest ongoing_bookingReq;

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
		
		this._cumulatedUtility = 0;
	}

	// add new consumption requirement
	public void addConsumptionRequirement(int startTime, int consumptionQuantity) {
		this._conReqList.add(new HourlyConsumptionRequirement(this.getAID(), startTime, consumptionQuantity));
	}

	// add profile to this consumer
	public void setProfile(String _preferredEnergyType, double _maximumBudgetPerQuantity, double _paramK,
			double _paramB_nonRenewable, double _paramB_renewable) {

		this._profile = new Profile(this.getAID(), _preferredEnergyType, _maximumBudgetPerQuantity, _paramK,
				_paramB_nonRenewable, _paramB_renewable);
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

	// getters and setters
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

	public double get_cumulatedUtility() {
		return _cumulatedUtility;
	}

	public void set_cumulatedUtility(double _cumulatedUtility) {
		this._cumulatedUtility = _cumulatedUtility;
	}

}
