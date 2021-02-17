package utils;

import java.util.Comparator;
import concepts.BookingRequest;
import jade.util.leap.Serializable;

public class BookingRequest_Comparator implements Comparator<BookingRequest>, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(BookingRequest req1, BookingRequest req2) {

		if (req1.get_startTime() > req2.get_startTime())
			return -1;
		else if (req1.get_startTime() < req2.get_startTime())
			return 1;
		else
			return 0;
	}

}