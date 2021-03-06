package utils;

import java.util.Comparator;
import concepts.PaymentRequest;
import jade.util.leap.Serializable;

public class PaymentRequest_Comparator implements Comparator<PaymentRequest>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(PaymentRequest req1, PaymentRequest req2) {

		if (req1.get_bq().get_startTime() > req2.get_bq().get_startTime())
			return -1;
		else if (req1.get_bq().get_startTime() < req2.get_bq().get_startTime())
			return 1;
		else
			return 0;
	}

}