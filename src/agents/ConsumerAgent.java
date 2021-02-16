package agents;

import java.util.ArrayList;

import behavioursConsumerAgent.initConsumer;
import behavioursConsumerAgent.sendConsumer_ConsReq;
import behavioursConsumerAgent.sendConsumer_Profile;
import behavioursConsumerAgent.finalizeConsumer;
import behavioursConsumerAgent.processConsumer;
import concepts.BookingRequest;
import concepts.HourlyConsumptionRequirement;
import concepts.Profile;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class ConsumerAgent extends Agent {

	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_SEND_PROFILE = "send_profile";
	private static final String BEHAVIOUR_SEND_CONSUMPTION_REQUIREMENT = "send_consumption_requirement";
	private static final String BEHAVIOUR_PROCESS = "process";
	private static final String BEHAVIOUR_FINALIZE = "finalize";

	private static final long serialVersionUID = 1L;

	private ArrayList<HourlyConsumptionRequirement> _conReqList = new ArrayList<>();
	private Profile _profile = new Profile();

	private double _cashBalance;
	private double _cumulatedUtility = 0.0;

	private BookingRequest ongoing_bookingReq;

	protected void setup() {

		// Read the arguments for set profile

		Object[] args = this.getArguments();
		this.set_cashBalance((double) args[0]);
		String _preferredEnergyType = (String) args[1];
		double _maximumBudgetPerQuantity = (double) args[2];
		double _paramK = (double) args[3];
		double _paramB_nonRenewable = (double) args[4];
		double _paramB_renewable = (double) args[5];
		int startTime = (int) args[6];
		int consumptionQuantity = (int) args[7];

		this.addConsumptionRequirement(startTime, consumptionQuantity);

		this.setProfile(new Profile(this.getAID(), _preferredEnergyType, _maximumBudgetPerQuantity, _paramK,
				_paramB_nonRenewable, _paramB_renewable));

		FSMBehaviour behaviour = new FSMBehaviour(this);
		
		// states
		behaviour.registerFirstState(new initConsumer(this), BEHAVIOUR_INIT);
		behaviour.registerState(new sendConsumer_Profile(this), BEHAVIOUR_SEND_PROFILE);
		behaviour.registerState(new sendConsumer_ConsReq(this), BEHAVIOUR_SEND_CONSUMPTION_REQUIREMENT);
		behaviour.registerState(new processConsumer(this), BEHAVIOUR_PROCESS);
		behaviour.registerLastState(new finalizeConsumer(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_SEND_PROFILE);
		behaviour.registerDefaultTransition(BEHAVIOUR_SEND_PROFILE, BEHAVIOUR_SEND_CONSUMPTION_REQUIREMENT);
		behaviour.registerDefaultTransition(BEHAVIOUR_SEND_CONSUMPTION_REQUIREMENT, BEHAVIOUR_PROCESS);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 1);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_FINALIZE, 2);

		addBehaviour(behaviour);
	}

	// add new consumption requirement
	public void addConsumptionRequirement(int startTime, int consumptionQuantity) {
		this._conReqList.add(new HourlyConsumptionRequirement(this.getAID(), startTime, consumptionQuantity));
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
