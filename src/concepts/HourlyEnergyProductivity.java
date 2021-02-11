package concepts;

import jade.content.Concept;
import jade.core.AID;
import jade.util.leap.Serializable;

public class HourlyEnergyProductivity implements Concept, Serializable {

	private static final long serialVersionUID = 1L;
	private AID _producerId;
	private int _producedEnergyQuantity;
	private String _producedEnergyType;
	private double _pricePerQuantity;

	public HourlyEnergyProductivity() {
		super();
	}

	public HourlyEnergyProductivity(AID producerId, int startTime, int producedEnergyQuantity,
			String producedEnergyType, double pricePerQuantity) {
		super();
		this._producerId = producerId;
		this._producedEnergyQuantity = producedEnergyQuantity;
		this._producedEnergyType = producedEnergyType;
		this._pricePerQuantity = pricePerQuantity;
	}

	@Override
	public String toString() {
		return "HourlyEnergyProductivity [_producerId=" + _producerId + 
				", _producedEnergyQuantity=" + _producedEnergyQuantity + ", _producedEnergyType="
				+ _producedEnergyType + ", _pricePerQuantity=" + _pricePerQuantity + "]";
	}

	/* Getters and Setters */
	public AID get_producerId() {
		return _producerId;
	}

	public void set_producerId(AID _producerId) {
		this._producerId = _producerId;
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

	public double get_pricePerQuantity() {
		return _pricePerQuantity;
	}

	public void set_pricePerQuantity(double _pricePerQuantity) {
		this._pricePerQuantity = _pricePerQuantity;
	}

}
