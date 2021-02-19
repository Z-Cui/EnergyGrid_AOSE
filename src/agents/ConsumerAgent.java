package agents;

import java.util.ArrayList;

import behavioursConsumerAgent.initConsumer;
import behavioursConsumerAgent.sendConsumer_ConsReq;
import behavioursConsumerAgent.sendConsumer_Profile;
import behavioursConsumerAgent.finalizeConsumer;
import behavioursConsumerAgent.processConsumer;
import concepts.BookingRequest;
import concepts.HourlyConsumptionRequirement;
import concepts.PaymentRequest;
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

		int[] _startTime = (int[]) args[6];
		int[] _consumptionQuantity = (int[]) args[7];
		// System.out.println(_startTime.length);

		ArrayList<HourlyConsumptionRequirement> newList = new ArrayList<>();
		for (int i = 0; i <= _startTime.length - 1; i++) {
			newList.add(new HourlyConsumptionRequirement(getAID(), _startTime[i], _consumptionQuantity[i]));
		}
		this.setConReqList(newList);

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
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 0);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_FINALIZE, 1);

		addBehaviour(behaviour);
	}

	// delete a consumption requirement from startTime.
	// return 1 if success, 0 for failure
	public int deleteConsumptionRequirement(int startTime) {
		for (int i = 0; i < this._conReqList.size(); i++) {
			if (this._conReqList.get(i).get_startTime() == startTime) {
				this._conReqList.remove(i);
				return 1;
			}
		}
		return 0;
	}

	// check remaining cash is enough for a booking request
	public boolean canPay(BookingRequest bq) {
		if (bq.get_pricePerUnit() * bq.get_reservedEnergyQuantity() <= this._cashBalance) {
			return true;
		} else
			return false;
	}

	// add Utility from successful payment request
	public void addUtility(PaymentRequest pq) {
		double actualUtiliy = this.get_cumulatedUtility();
		double newUtility = this.CalculateUtility(pq.get_bq(), this.getProfile());
		this.set_cumulatedUtility(actualUtiliy + newUtility);
	}

	// calculate the utility
	public double CalculateUtility(BookingRequest bq, Profile profile) {

		double _p = bq.get_pricePerUnit();
		int _q = bq.get_reservedEnergyQuantity();
		String _type = bq.get_reservedEnergyType();

		double _k = profile.get_paramK();
		double _b;
		if (_type == "Renewable")
			_b = this.getProfile().get_paramB_renewable();
		else
			_b = this.getProfile().get_paramB_nonRenewable();
		/*
		 * utility = (K-P)*Q + B; K: utility from every one unit of quantity; P: price
		 * per energy unit; Q: energy quantity; B: utility for different energy type
		 */
		return (_k - _p) * _q + _b;
	}

	// if all requirements have been satisfied
	public int satisfied() {
		// 1: yes, 0: no
		if (this.getConReqList().size() == 0)
			return 1;
		else
			return 0;
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
