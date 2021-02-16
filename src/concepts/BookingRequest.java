package concepts;

import jade.content.Concept;
import jade.core.AID;
import jade.util.leap.Serializable;

public class BookingRequest implements Concept, Serializable {

	private static final long serialVersionUID = 1L;
	
	private AID _consumerId;
	private AID _producerId;
	private int _startTime;
	private String _reservedEnergyType;
	private int _reservedEnergyQuantity;
	private double _pricePerUnit;

	// Status of a booking request:
	// 0: [Proposition] ProducerSelector generates it as a proposition to Consumer;
	// 1: [Request] Consumer confirms it and send it to BookingManager;
	// 2: [InQueue] BookingManager is managing this booking;
	// 3: [Processing] BookingManager is handling this booking;
	// 4: [Confirmed] Producer confirms this booking;
	// 5: [Rejected] Producer rejects this booking;
	// -1: Error
	private int _status;

	public BookingRequest() {
		
	}
	
	public BookingRequest(AID _consumerId, AID _producerId, int _startTime, String _reservedEnergyType,
			int _reservedEnergyQuantity, double _pricePerUnit, int _status) {
		this._consumerId = _consumerId;
		this._producerId = _producerId;
		this._startTime = _startTime;
		this._reservedEnergyType = _reservedEnergyType;
		this._reservedEnergyQuantity = _reservedEnergyQuantity;
		this._pricePerUnit = _pricePerUnit;
		this._status = _status;
	}

	@Override
	public String toString() {
		return "BookingRequest [_consumerId=" + _consumerId + ", _producerId=" + _producerId + ", _startTime="
				+ _startTime + ", _reservedEnergyType=" + _reservedEnergyType + ", _reservedEnergyQuantity="
				+ _reservedEnergyQuantity + ", _pricePerUnit=" + _pricePerUnit + ", _status=" + _status + "]";
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

	public String get_reservedEnergyType() {
		return _reservedEnergyType;
	}

	public void set_reservedEnergyType(String _reservedEnergyType) {
		this._reservedEnergyType = _reservedEnergyType;
	}

	public int get_reservedEnergyQuantity() {
		return _reservedEnergyQuantity;
	}

	public void set_reservedEnergyQuantity(int _reservedEnergyQuantity) {
		this._reservedEnergyQuantity = _reservedEnergyQuantity;
	}

	public double get_pricePerUnit() {
		return _pricePerUnit;
	}

	public void set_pricePerUnit(double _pricePerUnit) {
		this._pricePerUnit = _pricePerUnit;
	}

	public int get_status() {
		return _status;
	}

	public void set_status(int _status) {
		this._status = _status;
	}

}
