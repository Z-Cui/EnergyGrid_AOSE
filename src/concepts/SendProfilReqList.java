package concepts;

import java.util.ArrayList;

import jade.content.Concept;
import jade.util.leap.Serializable;

public class SendProfilReqList implements Concept, Serializable{

	private static final long serialVersionUID = 1L;
	private Profile _profile;
	private ArrayList<HourlyConsumptionRequirement> _conReqList;
	
	public SendProfilReqList() {
		super();
	}
	
	public Profile get_profile() {
		return this._profile;
	}

	public void set_profile(Profile _profile) {
		this._profile = _profile;
	}
	
	public ArrayList<HourlyConsumptionRequirement> get_conReqList() {
		return this._conReqList;
	}

	public void set_conReqList(ArrayList<HourlyConsumptionRequirement> _conReqList) {
		this._conReqList = _conReqList;
	}

}
