package concepts;

import jade.content.Concept;
import jade.core.AID;
import jade.util.leap.Serializable;

public class HourlyEnergyProductivity implements Concept, Serializable {

	private static final long serialVersionUID = 1L;
	private AID _producerId;
	private int _startTime;
	private int _producedEnergyQuantity;
	private String _producedEnergyType;
	private double _pricePerUnit;

	public HourlyEnergyProductivity(AID _producerId, int _startTime, int _producedEnergyQuantity,
			String _producedEnergyType, double _pricePerUnit) {
		this._producerId = _producerId;
		this._startTime = _startTime;
		this._producedEnergyQuantity = _producedEnergyQuantity;
		this._producedEnergyType = _producedEnergyType;
		this._pricePerUnit = _pricePerUnit;
	}

	@Override
	public String toString() {
		return "HourlyEnergyProductivity [_producerId=" + _producerId + ", _startTime=" + _startTime
				+ ", _producedEnergyQuantity=" + _producedEnergyQuantity + ", _producedEnergyType="
				+ _producedEnergyType + ", _pricePerUnit=" + _pricePerUnit + "]";
	}

	/* Getters and Setters */
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

	public int get_producedEnergyQuantity() {
		return _producedEnergyQuantity;
	}

	public void set_producedEnergyQuantity(int _producedEnergyQuantity) {
		this._producedEnergyQuantity = _producedEnergyQuantity;
	}

	public String get_producedEnergyType() {
		return _producedEnergyType;
	}

	public void set_producedEnergyType(String _producedEnergyType) {
		this._producedEnergyType = _producedEnergyType;
	}

	public double get_pricePerUnit() {
		return _pricePerUnit;
	}

	public void set_pricePerUnit(double _pricePerUnit) {
		this._pricePerUnit = _pricePerUnit;
	}

}
