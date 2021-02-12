package concepts;

import jade.content.Concept;
import jade.core.AID;
import jade.util.leap.Serializable;

public class HourlyConsumptionRequirement implements Concept, Serializable {

	private static final long serialVersionUID = 1L;
	private AID _consumerId;
	private int _startTime;
	private int _consumptionQuantity;

	public HourlyConsumptionRequirement(AID consumerId, int startTime, int consumptionQuantity) {
		this._consumerId = consumerId;
		this._startTime = startTime;
		this._consumptionQuantity = consumptionQuantity;
	}

	@Override
	public String toString() {
		return "HourlyConsumptionRequirement [_consumerId=" + _consumerId + ", _startTime=" + _startTime
				+ ", _consumptionQuantity=" + _consumptionQuantity + "]";
	}

	/* Getters and Setters */
	public AID get_consumerId() {
		return _consumerId;
	}

	public void set_consumerId(AID _consumerId) {
		this._consumerId = _consumerId;
	}

	public int get_startTime() {
		return _startTime;
	}

	public void set_startTime(int _timeSlot) {
		this._startTime = _timeSlot;
	}

	public int get_consumptionQuantity() {
		return _consumptionQuantity;
	}

	public void set_consumptionQuantity(int _consumptionQuantity) {
		this._consumptionQuantity = _consumptionQuantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_consumerId == null) ? 0 : _consumerId.hashCode());
		result = prime * result + _consumptionQuantity;
		result = prime * result + _startTime;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HourlyConsumptionRequirement other = (HourlyConsumptionRequirement) obj;
		if (_consumerId == null) {
			if (other._consumerId != null)
				return false;
		} else if (!_consumerId.equals(other._consumerId))
			return false;
		if (_consumptionQuantity != other._consumptionQuantity)
			return false;
		if (_startTime != other._startTime)
			return false;
		return true;
	}

}
