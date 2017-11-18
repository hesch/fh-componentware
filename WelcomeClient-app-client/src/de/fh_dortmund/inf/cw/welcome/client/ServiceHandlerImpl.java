package de.fh_dortmund.inf.cw.welcome.client;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.fh_dortmund.inf.cw.welcome.client.shared.ServiceHandler;
import de.fh_dortmund.inf.cw.welcome.client.shared.WelcomeHandler;
import de.fh_dortmund.inf.cw.welcome.server.beans.interfaces.WelcomeRemote;

public class ServiceHandlerImpl extends ServiceHandler implements WelcomeHandler {

	private static ServiceHandlerImpl instance;
	
	private Context ctx;
	private WelcomeRemote welcome;
	
	
	private ServiceHandlerImpl() {
		try {
			ctx = new InitialContext();
			
			welcome = (WelcomeRemote) ctx.lookup("java:global/WelcomeClient-ear/WelcomeClient-ejb/WelcomeBean!de.fh_dortmund.inf.cw.welcome.server.beans.interfaces.WelcomeRemote");
		} catch (NamingException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	public static ServiceHandlerImpl getInstance() {
		if(instance == null) {
			instance = new ServiceHandlerImpl();
		}
		
		return instance;
	}

	@Override
	public String sayHello(String name) {
		return welcome.sayHello(name);
	}
	
}
