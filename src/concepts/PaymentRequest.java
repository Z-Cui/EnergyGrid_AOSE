package concepts;

import jade.content.Concept;
import jade.util.leap.Serializable;

public class PaymentRequest implements Concept, Serializable {

	private static final long serialVersionUID = 1L;

	private BookingRequest _bq;
	private double _money;

	public PaymentRequest() {
		super();
	}

	public PaymentRequest(BookingRequest _bq, double _money) {
		super();
		this._bq = _bq;
		this._money = _money;
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

}
