package utils;

import java.util.Comparator;

import concepts.HourlyEnergyProductivity;
import jade.util.leap.Serializable;

public class HourlyEnergyProductivity_Comparator implements Comparator<HourlyEnergyProductivity>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(HourlyEnergyProductivity req1, HourlyEnergyProductivity req2) {

		if (req1.get_startTime() > req2.get_startTime())
			return -1;
		else if (req1.get_startTime() < req2.get_startTime())
			return 1;
		else
			return 0;
	}
	
}