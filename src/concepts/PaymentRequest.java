package concepts;

import jade.content.Concept;
import jade.util.leap.Serializable;

public class PaymentRequest implements Concept, Serializable {

	private static final long serialVersionUID = 1L;

	private BookingRequest _bq;
	private double _money;
	private int _status;
	// status: 0: consumer sends payment
	// status: 1: producer receives payment
	// status: -1: the booking request(inside of payment request) is expired 

	public PaymentRequest(BookingRequest _bq, double _money, int _status) {
		this._bq = _bq;
		this._money = _money;
		this._status = _status;
	}

	// getters and setters
	public BookingRequest get_bq() {
		return _bq;
	}

	public void set_bq(BookingRequest _bq) {
		this._bq = _bq;
	}

	public double get_money() {
		return _money;
	}

	public void set_money(double _money) {
		this._money = _money;
	}

	public int get_status() {
		return _status;
	}

	public void set_status(int _status) {
		this._status = _status;
	}
	
	

}
