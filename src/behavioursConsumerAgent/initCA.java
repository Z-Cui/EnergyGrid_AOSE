package behavioursConsumerAgent;

import agents.ConsumerAgent;
import concepts.HourlyConsumptionRequirement;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class initCA extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	ConsumerAgent agent;
	
	public initCA(ConsumerAgent a) {
		this.agent = a;
	}

	@Override
	public void action() {
		// Registration with Directory Facilitator (DF)
		DFAgentDescription dfDescription = new DFAgentDescription();
		dfDescription.setName(agent.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("consumer");
		serviceDescription.setName(agent.getLocalName() + "-consumer");
		dfDescription.addServices(serviceDescription);
		try {
			DFService.register(agent, dfDescription);
			System.out.println("ConsumerAgent " + agent.getAID().getName() + " regitstered.");
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		//Read the arguments for set profile
		
		Object[] args = agent.getArguments();
		agent.set_cashBalance((double) args[0]);
		String _preferredEnergyType = (String) args[1];
		double _maximumBudgetPerQuantity = (double) args[2];
		double _paramK = (double) args[3];
		double _paramB_nonRenewable = (double) args[4];
		double _paramB_renewable = (double) args[5];
		int startTime = (int) args[6];
		int consumptionQuantity = (int) args[7];
		
		agent.addConsumptionRequirement(startTime, consumptionQuantity);
		
		agent.setProfile(_preferredEnergyType, _maximumBudgetPerQuantity, _paramK,
				_paramB_nonRenewable, _paramB_renewable, agent.getConReqList());
		
		
		
		System.out.println("ConsumerAgent " + agent.getAID().getName() + " is ready.");
		
		agent.set_cumulatedUtility(0);
		agent.doWait(200);
		
	}
}
