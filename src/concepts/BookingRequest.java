package concepts;

import jade.content.Concept;
import jade.core.AID;
import jade.util.leap.Serializable;

public class BookingRequest implements Concept, Serializable {

	private static final long serialVersionUID = 1L;
	private AID _consumerId;
	private AID _producerId;
	private int _startTime;
	private String _wantedEnergyType;
	private double _wantedEnergyQuantity;
	private double _pricePerUnity;

	/*
	 * Status of a booking request: 
	 * 0: [Proposition] ProducerSelector generates it as a proposition; 
	 * 1: [Request] Consumer confirms it and send it to BookingManager; 
	 * 2: [InQueue] BookingManager is managing this booking;
	 * 3: [Processing] BookingManager is handling this booking;
	 * 4: [Confirmed] Producer confirms this booking;
	 * 5: [Rejected] Producer rejects this booking;
	 */
	private int _status;

	public BookingRequest() {
		super();
	}

	public BookingRequest(AID _consumerId, AID _producerId, int _startTime, String _wantedEnergyType,
			double _wantedEnergyQuantity, double _pricePerUnity, int _status) {
		super();
		this._consumerId = _consumerId;
		this._producerId = _producerId;
		this._startTime = _startTime;
		this._wantedEnergyType = _wantedEnergyType;
		this._wantedEnergyQuantity = _wantedEnergyQuantity;
		this._pricePerUnity = _pricePerUnity;
		this._status = _status;
	}

	@Override
	public String toString() {
		return "BookingRequest [_consumerId=" + _consumerId + ", _producerId=" + _producerId + ", _startTime="
				+ _startTime + ", _wantedEnergyType=" + _wantedEnergyType + ", _wantedEnergyQuantity="
				+ _wantedEnergyQuantity + ", _pricePerUnity=" + _pricePerUnity + ", _status=" + _status + "]";
	}

	public AID get_consumerId() {
		return _consumerId;
	}

	public void set_consumerId(AID _consumerId) {
		this._consumerId = _consumerId;
	}

	public AID get_producerId() {
		return _producerId;
	}

	public void set_producerId(AID _producerId) {
		this._producerId = _producerId;
	}

	public int get_startTime() {
		return _startTime;
	}

	public void set_startTime(int _startTime) {
		this._startTime = _startTime;
	}

	public String get_wantedEnergyType() {
		return _wantedEnergyType;
	}

	public void set_wantedEnergyType(String _wantedEnergyType) {
		this._wantedEnergyType = _wantedEnergyType;
	}

	public double get_wantedEnergyQuantity() {
		return _wantedEnergyQuantity;
	}

	public void set_wantedEnergyQuantity(double _wantedEnergyQuantity) {
		this._wantedEnergyQuantity = _wantedEnergyQuantity;
	}

	public double get_pricePerUnity() {
		return _pricePerUnity;
	}

	public void set_pricePerUnity(double _pricePerUnity) {
		this._pricePerUnity = _pricePerUnity;
	}

	public int get_status() {
		return _status;
	}

	public void set_status(int _status) {
		this._status = _status;
	}

}
