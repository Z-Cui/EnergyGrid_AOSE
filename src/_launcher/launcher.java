package _launcher;

import agents.BookingManagerAgent;
import agents.ConsumerAgent;
import agents.MarketPlaceAgent;
import agents.PaymentManagerAgent;
import agents.ProducerAgent;
import agents.ProducerListManagerAgent;
import agents.ProducerSelectorAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class launcher {
	public static void main(String[] args) throws InterruptedException {
		Runtime runtime = Runtime.instance();
		Profile config = new ProfileImpl("localhost", 8888, null);
		config.setParameter("gui", "true");
		AgentContainer mc = runtime.createMainContainer(config);

		AgentController ac1, ac2, ac3, ac4, ac5, ac6, ac7, ac8;

		try {
			ac1 = mc.createNewAgent("MarketPlace", MarketPlaceAgent.class.getName(), new Object[] {});
			ac1.start();
			ac2 = mc.createNewAgent("BookingManager", BookingManagerAgent.class.getName(), new Object[] {});
			ac2.start();
			ac3 = mc.createNewAgent("PaymentManager", PaymentManagerAgent.class.getName(), new Object[] {});
			ac3.start();
			Thread.sleep(5000);
			ac4 = mc.createNewAgent("ProducerListManager", ProducerListManagerAgent.class.getName(), new Object[] {});
			ac4.start();
			ac5 = mc.createNewAgent("ProducerSelector", ProducerSelectorAgent.class.getName(), new Object[] {});
			ac5.start();
			Thread.sleep(5000);

			// create a producer
			ac6 = mc.createNewAgent("Producer01", ProducerAgent.class.getName(), 
					new Object[] { new int[] { 10, 11 }, // Producer01 offers energy for 10-11am and 11-12am
					new int[] { 100, 120 }, // Producer01 offers 100 quantity of energy during 10-11am, 120 quantity during 11am-12am
					new String[] { "nonRenewable", "renewable" }, // he offers non-renewable energy during 10-11am, renewable energy during 11-12am
					new Double[] { 1.1, 1.2 } }); // prices of energy are 1.1(10-11am) and 1.2(11-12am)
			ac6.start();
			Thread.sleep(5000);

			// create a consumer
			ac7 = mc.createNewAgent("Consumer01", ConsumerAgent.class.getName(), 
					new Object[] { 
							100.00, // Consumer01 has budget 100 for energy
							new String("nonRenewable"), // he prefers nonRenewable energy
							2.00, // he accepts when energy price per unit is less than 2.0
							1.5, // for his utility, parameter k is 1.5
							5.1, // for his utility, parameter b for nonRenewable energy is 5.1
							4.2, // for his utility, parameter b for renewable energy is 4.2
							new int[] { 10, 11 }, // he needs energy 10-11am and 11-12am
							new int[] { 20, 30 } }); // he needs 20 quantity for 10-11am, 30 quantity for 11-12am
			// create another consumer
			ac8 = mc.createNewAgent("Consumer02", ConsumerAgent.class.getName(), 
					new Object[] { 
							100.00, // Consumer02 has budget 100 for energy
							new String("renewable"), 
							2.20, // he accepts when energy price per unit is less than 2.2
							1.7, // for his utility, parameter k is 1.7
							2.1, // for his utility, parameter b for nonRenewable energy is 2.1
							6.2, // for his utility, parameter b for renewable energy is 6.2
							new int[] { 10, 11 }, // he needs energy 10-11am and 11-12am
							new int[] { 10, 40 } }); // he needs 10 quantity for 10-11am, 40 quantity for 11-12am
			ac7.start();
			ac8.start();

		} catch (StaleProxyException e) {
		}

	}
}
