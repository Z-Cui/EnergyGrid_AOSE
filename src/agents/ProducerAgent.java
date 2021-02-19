package agents;

import java.util.ArrayList;

import behavioursProducer.finalizeProducer;
import behavioursProducer.initProducer;
import behavioursProducer.processProducer;
import behavioursProducer.sendProducer_Productivity;
import concepts.BookingRequest;
import concepts.HourlyEnergyProductivity;
import concepts.PaymentRequest;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class ProducerAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private static final String BEHAVIOUR_INIT = "init";
	private static final String BEHAVIOUR_SEND_PRODUCTIVITY_INFO = "send_productivity_info";
	private static final String BEHAVIOUR_PROCESS = "process";
	private static final String BEHAVIOUR_FINALIZE = "finalize";

	private ArrayList<HourlyEnergyProductivity> _energyProductivityList = new ArrayList<>();

	private double _profit = 0.0;
	private long startTimeWithoutMessage = System.currentTimeMillis();

	protected void setup() {

		// Read the arguments for set energyProductivityList
		Object[] args = getArguments();
		int[] _startTime = (int[]) args[0];
		int[] _producedEnergyQuantity = (int[]) args[1];
		String[] _producedEnergyType = (String[]) args[2];
		Double[] _pricePerUnit = (Double[]) args[3];

		// Set productivity info
		ArrayList<HourlyEnergyProductivity> List = new ArrayList<>();
		for (int i = 0; i <= _startTime.length - 1; i++) {
			List.add(new HourlyEnergyProductivity(getAID(), _startTime[i], _producedEnergyQuantity[i],
					_producedEnergyType[i], _pricePerUnit[i]));
		}
		this.set_energyProductivityList(List);

		FSMBehaviour behaviour = new FSMBehaviour(this);
		// states
		behaviour.registerFirstState(new initProducer(this), BEHAVIOUR_INIT);
		behaviour.registerState(new sendProducer_Productivity(this), BEHAVIOUR_SEND_PRODUCTIVITY_INFO);
		behaviour.registerState(new processProducer(this), BEHAVIOUR_PROCESS);
		behaviour.registerLastState(new finalizeProducer(this), BEHAVIOUR_FINALIZE);

		// Transitions
		behaviour.registerDefaultTransition(BEHAVIOUR_INIT, BEHAVIOUR_SEND_PRODUCTIVITY_INFO);
		behaviour.registerDefaultTransition(BEHAVIOUR_SEND_PRODUCTIVITY_INFO, BEHAVIOUR_PROCESS);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_PROCESS, 1);
		behaviour.registerTransition(BEHAVIOUR_PROCESS, BEHAVIOUR_FINALIZE, 2);

		addBehaviour(behaviour);
	}

	// Accept(2) or Reject(3) a booking request
	public int acceptOrReject(BookingRequest bq) {
		for (int i = 0; i < this.get_energyProductivityList().size(); i++) {
			HourlyEnergyProductivity p = this.get_energyProductivityList().get(i);
			if (p.get_startTime() == bq.get_startTime()
					&& bq.get_reservedEnergyType().equals(p.get_producedEnergyType())
					&& bq.get_reservedEnergyQuantity() <= p.get_producedEnergyQuantity()) {
				return 2;
			}
		}
		return 3;
	}

	// Process a payment
	// Success(1);
	// Failure(-1, means the booking request is not valid)
	public int processPayment(PaymentRequest pq) {
		BookingRequest bq = pq.get_bq();
		for (int i = 0; i < this.get_energyProductivityList().size(); i++) {
			HourlyEnergyProductivity p = this.get_energyProductivityList().get(i);
			if (bq.get_startTime() == p.get_startTime()
					&& bq.get_reservedEnergyType().equals(p.get_producedEnergyType())) {
				int soldQuantity = bq.get_reservedEnergyQuantity();
				int actualQuantity = p.get_producedEnergyQuantity();
				if (actualQuantity >= soldQuantity) {
					p.set_producedEnergyQuantity(actualQuantity - soldQuantity);
					this.addReceivedPaymentToProfit(pq.get_money());
					return 1;
				} else
					return -1;
			}
		}
		return -1;
	}

	// Add received payment to profit
	public void addReceivedPaymentToProfit(double p) {
		this.set_profit(this.get_profit() + p);
	}

	public void takeDown() {
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

	public long getStartTimeWithoutMessage() {
		return startTimeWithoutMessage;
	}

	public void setStartTimeWithoutMessage(long timeWithoutMessage) {
		this.startTimeWithoutMessage = timeWithoutMessage;
	}

}
