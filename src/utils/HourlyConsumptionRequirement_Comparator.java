package utils;

import java.util.Comparator;

import concepts.HourlyConsumptionRequirement;
import jade.util.leap.Serializable;

public class HourlyConsumptionRequirement_Comparator implements Comparator<HourlyConsumptionRequirement>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(HourlyConsumptionRequirement req1, HourlyConsumptionRequirement req2) {

		if (req1.get_startTime() < req2.get_startTime())
			return -1;
		else if (req1.get_startTime() > req2.get_startTime())
			return 1;
		else
			return 0;
	}

}