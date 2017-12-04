package de.randomerror.chat.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.fh_dortmund.inf.cw.chat.server.entities.UserStatistic;
import de.randomerror.chat.ServiceHandlerImpl;

public class StatisticHandlerTest {
	private ServiceHandlerImpl serviceHandler;
		
	@Before
	public void before() throws Exception {
		((ServiceHandlerImpl) ServiceHandlerImpl.getInstance()).nuke();
		ServiceHandlerImpl.reinit();
		serviceHandler = (ServiceHandlerImpl) ServiceHandlerImpl.getInstance();
	}
	
	@Test
	public void testGetUserStatistic() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");
		
		UserStatistic stat = serviceHandler.getUserStatistic();
		
		assertEquals(1, stat.getLogins());
	}
	
	@Test
	public void testGetUserStatisticReturnsNull() throws Exception {
		UserStatistic stat = serviceHandler.getUserStatistic();
		
		assertEquals(null, stat);
	}
	
	@Test
	public void testGetStatistics() throws Exception {
		serviceHandler.getStatistics();
	}
}
