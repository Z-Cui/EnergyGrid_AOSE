package concepts;

import jade.content.Concept;
import jade.core.AID;
import jade.util.leap.Serializable;

public class Profile implements Concept, Serializable {

	private static final long serialVersionUID = 1L;
	private AID _consumerId;
	private String _preferredEnergyType;
	private double _maximumBudgetPerQuantity;

	/*
	 * Hourly utility of a consumer = (K-P)*Q + B;
	 * K: utility from every one unit of quantity;
	 * P: price per energy unit;
	 * Q: energy quantity; 
	 * B: utility for different energy type
	 */
	private double _paramK;
	private double _paramB_nonRenewable;
	private double _paramB_renewable;

	public Profile() {
		super();
	}

	public Profile(AID _consumerId, String _preferredEnergyType, double _maximumBudgetPerQuantity,
			double _paramK, double _paramB_nonRenewable, double _paramB_renewable) {
		super();
		this._consumerId = _consumerId;
		this._preferredEnergyType = _preferredEnergyType;
		this._maximumBudgetPerQuantity = _maximumBudgetPerQuantity;
		this._paramK = _paramK;
		this._paramB_nonRenewable = _paramB_nonRenewable;
		this._paramB_renewable = _paramB_renewable;
	}

	@Override
	public String toString() {
		return "PreferenceAndUtility [_consumerId=" + _consumerId + ", _preferredEnergyType=" + _preferredEnergyType
				+ ", _maximumBudgetPerQuantity=" + _maximumBudgetPerQuantity + ", _paramK=" + _paramK
				+ ", _paramB_nonRenewable=" + _paramB_nonRenewable + ", _paramB_renewable=" + _paramB_renewable + "]";
	}

	/* Getters and Setters */
	public AID get_consumerId() {
		return _consumerId;
	}

	public void set_consumerId(AID _consumerId) {
		this._consumerId = _consumerId;
	}

	public String get_preferredEnergyType() {
		return _preferredEnergyType;
	}

	public void set_preferredEnergyType(String _preferredEnergyType) {
		this._preferredEnergyType = _preferredEnergyType;
	}

	public double get_maximumBudgetPerQuantity() {
		return _maximumBudgetPerQuantity;
	}

	public void set_maximumBudgetPerQuantity(double _maximumBudgetPerQuantity) {
		this._maximumBudgetPerQuantity = _maximumBudgetPerQuantity;
	}

	public double get_paramK() {
		return _paramK;
	}

	public void set_paramK(double _paramK) {
		this._paramK = _paramK;
	}

	public double get_paramB_nonRenewable() {
		return _paramB_nonRenewable;
	}

	public void set_paramB_nonRenewable(double _paramB_nonRenewable) {
		this._paramB_nonRenewable = _paramB_nonRenewable;
	}

	public double get_paramB_renewable() {
		return _paramB_renewable;
	}

	public void set_paramB_renewable(double _paramB_renewable) {
		this._paramB_renewable = _paramB_renewable;
	}

}
