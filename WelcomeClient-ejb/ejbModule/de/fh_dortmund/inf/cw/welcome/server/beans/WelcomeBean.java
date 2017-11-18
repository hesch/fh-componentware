package de.fh_dortmund.inf.cw.welcome.server.beans;

import javax.ejb.Stateless;

import de.fh_dortmund.inf.cw.welcome.server.beans.interfaces.WelcomeLocal;
import de.fh_dortmund.inf.cw.welcome.server.beans.interfaces.WelcomeRemote;

@Stateless
public class WelcomeBean implements WelcomeLocal, WelcomeRemote {
	
	@Override
	public String sayHello(String name) {
		return "Hello " + name + "!";
	}

}
