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

public class TestCase6 {
	public static void main(String[] args) throws InterruptedException {
		Runtime runtime = Runtime.instance();
		Profile config = new ProfileImpl("localhost", 8888, null);
		config.setParameter("gui", "true");
		AgentContainer mc = runtime.createMainContainer(config);

		AgentController ac1, ac2, ac3, ac4, ac5, ac6, ac7, ac8;

		try {
						
			// ac2 = mc.createNewAgent("BookingManager",
			// BookingManagerAgent.class.getName(), new Object[] {new String("agent02")});
			ac1 = mc.createNewAgent("MarketPlace", MarketPlaceAgent.class.getName(), new Object[] {});
			ac1.start();
			ac2 = mc.createNewAgent("BookingManager", BookingManagerAgent.class.getName(), new Object[] {});
			ac2.start();
			ac3 = mc.createNewAgent("PaymentManager", PaymentManagerAgent.class.getName(), new Object[] {});
			ac3.start();
			Thread.sleep(5000);
			
			// Producers, create one producer with list hourly energy productivity
			ac4 = mc.createNewAgent("Producer01", ProducerAgent.class.getName(),
					new Object[] { new int[] { 10, 11 }, new int[] { 100, 120 },
							new String[] { "nonRenewable", "nonRenewable" }, new Double[] { 1.1, 1.2 } });
			ac4.start();
			Thread.sleep(5000);
			ac7 = mc.createNewAgent("Producer02", ProducerAgent.class.getName(),
					new Object[] { new int[] { 10, 11 }, new int[] { 100, 120 },
							new String[] { "Renewable", "Renewable" }, new Double[] { 1.1, 1.2 } });
			ac7.start();
			Thread.sleep(5000);
			ac5 = mc.createNewAgent("ProducerListManager", ProducerListManagerAgent.class.getName(), new Object[] {});
			ac5.start();
			ac6 = mc.createNewAgent("ProducerSelector", ProducerSelectorAgent.class.getName(), new Object[] {});
			ac6.start();
			Thread.sleep(5000);
			
		

		} catch (StaleProxyException e) {
		}

	}
}