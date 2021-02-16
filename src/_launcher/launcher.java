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
	public static void main(String[] args) {
		Runtime runtime = Runtime.instance();
		Profile config = new ProfileImpl("localhost", 8888, null);
		config.setParameter("gui", "true");
		AgentContainer mc = runtime.createMainContainer(config);

		AgentController ac1,ac2,ac3,ac4,ac5,ac6,ac7,ac8;

		try {
			ac1 = mc.createNewAgent("BookingManager", BookingManagerAgent.class.getName(), new Object[] {new String("agent02")});
			ac2 = mc.createNewAgent("MarketPlace", MarketPlaceAgent.class.getName(), new Object[] {new String("agent02")});
			ac3 = mc.createNewAgent("PaymentManager", PaymentManagerAgent.class.getName(), new Object[] {new String("agent02")});
			ac4 = mc.createNewAgent("ProducerListManager", ProducerListManagerAgent.class.getName(), new Object[] {new String("agent02")});
			ac5 = mc.createNewAgent("ProducerSelector", ProducerSelectorAgent.class.getName(), new Object[] {new String("agent02")});
			//Consumers  new Object[] {_cashBalance,new String("_preferredEnergyType"),_maximumBudgetPerQuantity,_paramK,_paramB_nonRenewable,_paramB_renewable})
			ac6 = mc.createNewAgent("Consumer01", ConsumerAgent.class.getName(), new Object[] {100.00,new String("nonRenewable"),2.00,1.5,5.1,4.2,10,20});
			//ac7 = mc.createNewAgent("Consumer02", ConsumerAgent.class.getName(), new Object[] {100.00,new String("nonRenewable"),2.00,1.5,5.1,4.2,10,40});
			//Producers, create one producer with list hourly energy productivity
			ac8 = mc.createNewAgent("Producer01", ProducerAgent.class.getName(), new Object[] {new int[] {10,11},new int[] {100,120},
					new String[] {"nonRenewable","nonRenewable"},new Double[] {1.1,1.2}});
			
			// 0 product
		
			ac1.start();
			ac2.start();
			ac3.start();
			ac4.start();
			ac5.start();
			ac6.start();
			//ac7.start();
			ac8.start();
		} catch (StaleProxyException e) {}

	}
}
