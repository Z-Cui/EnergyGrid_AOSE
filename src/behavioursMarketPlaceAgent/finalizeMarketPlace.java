package behavioursMarketPlaceAgent;

import agents.MarketPlaceAgent;
import jade.core.behaviours.OneShotBehaviour;

public class finalizeMarketPlace extends OneShotBehaviour{
	
	private static final long serialVersionUID = 1L;
	MarketPlaceAgent agent;
	
	public finalizeMarketPlace(MarketPlaceAgent a) {
		this.agent = a;
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		agent.takeDown();
	}
}
