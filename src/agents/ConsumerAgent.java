package agents;

import java.util.ArrayList;

import behavioursConsumerAgent.initCA;
import behavioursConsumerAgent.sendCA;
import behavioursConsumerAgent.finalizeCA;
import behavioursConsumerAgent.processCA;
import concepts.BookingRequest;
import concepts.HourlyConsumptionRequirement;
import concepts.Profile;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class ConsumerAgent extends Agent {
	
	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_SEND = "send";
	private static final String BEHAVIOUR_PROCESS = "process";
	private static final String BEHAVIOUR_FINALIZE = "finalize";
	
	private static final long serialVersionUID = 1L;

	private ArrayList<HourlyConsumptionRequirement> _conReqList = new ArrayList<>();
	private Profile _profile;

	private double _cashBalance;
	private double _cumulatedUtility;
	
	private BookingRequest ongoing_bookingReq;

	protected void setup() {
		FSMBehaviour behaviour = new FSMBehaviour(this);
		// states
		behaviour.registerFirstState(new initCA(this), BEHAVIOUR_INIT);
		behaviour.registerState(new sendCA(this), BEHAVIOUR_SEND);
		behaviour.registerState(new processCA(this), BEHAVIOUR_PROCESS);
		behaviour.registerLastState(new finalizeCA(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_SEND);
		behaviour.registerDefaultTransition(BEHAVIOUR_SEND, BEHAVIOUR_PROCESS);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_SEND, 0);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 1);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_FINALIZE, 2);

		addBehaviour(behaviour);
		
	}

	// add new consumption requirement
	public void addConsumptionRequirement(int startTime, int consumptionQuantity) {
		this._conReqList.add(new HourlyConsumptionRequirement(this.getAID(), startTime, consumptionQuantity));
	}

	// add profile to this consumer
	public void setProfile(String _preferredEnergyType, double _maximumBudgetPerQuantity, double _paramK,
			double _paramB_nonRenewable, double _paramB_renewable, ArrayList<HourlyConsumptionRequirement> _conReqList) {

		// this._profile = new Profile(this.getAID(), _preferredEnergyType, _maximumBudgetPerQuantity, _paramK, _paramB_nonRenewable, _paramB_renewable);
		
		this.setProfile(new Profile(this.getAID(), _preferredEnergyType, _maximumBudgetPerQuantity, _paramK,
				_paramB_nonRenewable, _paramB_renewable));
		
		System.out.println("!!" + this._profile.toString());
		
	}

	public void takeDown() {
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
	
	public BookingRequest get_ongoing_bookingReq() {
		return ongoing_bookingReq;
	}

	public void set_ongoing_bookingReq(BookingRequest ongoing_bookingReq) {
		this.ongoing_bookingReq = ongoing_bookingReq;
	}

}
