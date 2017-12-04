package de.randomerror.chat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fh_dortmund.inf.cw.chat.client.shared.UserSessionHandler;
import de.randomerror.chat.ServiceHandlerImpl;
import de.randomerrror.chat.exceptions.AuthenticationFailedException;
import de.randomerrror.chat.exceptions.NotAuthenticatedException;
import de.randomerrror.chat.exceptions.UserAlreadyExistsException;
import de.randomerrror.chat.exceptions.UserNotFoundException;

public class UserSessionHandlerTest {
	
	private UserSessionHandler serviceHandler;
	
	@Before
	public void before() throws Exception {
		((ServiceHandlerImpl) ServiceHandlerImpl.getInstance()).nuke();
		ServiceHandlerImpl.reinit();
		serviceHandler = (UserSessionHandler) ServiceHandlerImpl.getInstance();
	}

	@Test
	public void testRegister() throws Exception {
		serviceHandler.register("test", "test");
		assertEquals(1, serviceHandler.getNumberOfRegisteredUsers());
	}
	
	@Test(expected=UserAlreadyExistsException.class)
	public void testRegisterFails() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.register("test", "test");
		assertEquals(serviceHandler.getNumberOfRegisteredUsers(), 1);
	}
	
	@Test
	public void testLogin() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");
		assertEquals(serviceHandler.getNumberOfOnlineUsers(), 1);
	}
	
	@Test(expected=AuthenticationFailedException.class)
	public void testLoginFailsWrongPassword() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test2");
		assertEquals(serviceHandler.getNumberOfOnlineUsers(), 0);
	}
	
	@Test(expected=UserNotFoundException.class)
	public void testLoginFailsUserNotFound() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test2", "test");
		assertEquals(serviceHandler.getNumberOfOnlineUsers(), 0);
	}
	
	@Test
	public void testLogout() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");
		serviceHandler.logout();

		assertEquals(serviceHandler.getNumberOfOnlineUsers(), 0);
	}
	
	@Test(expected=NotAuthenticatedException.class)
	public void testLogoutFailsNotLoggedIn() throws Exception {
		serviceHandler.logout();
		assertEquals(serviceHandler.getNumberOfOnlineUsers(), 0);
	}
	
	@Test
	public void testGetUserName() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");

		assertEquals(serviceHandler.getUserName(), "test");
	}
	
	@Test
	public void testGetOnlineUsers() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");
		
		serviceHandler.register("test2", "test2");
		serviceHandler.login("test2", "test2");

		assertEquals(serviceHandler.getOnlineUsers().size(), 2);
		assertTrue(serviceHandler.getOnlineUsers().contains("test"));
		assertTrue(serviceHandler.getOnlineUsers().contains("test2"));
	}
	
	@Test
	public void testChangePassword() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");
		serviceHandler.changePassword("test", "hallo");
		serviceHandler.login("test", "hallo");
	}
	
	@Test(expected=AuthenticationFailedException.class)
	public void testChangePasswordFailsWrongPassword() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");
		serviceHandler.changePassword("test1", "hallo");
	}
	
	@Test(expected=NotAuthenticatedException.class)
	public void testChangePasswordNotLoggedIn() throws Exception {
		serviceHandler.changePassword("test1", "hallo");
	}
	
	@Test
	public void disconnect() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.login("test", "test");
		//test if disconnect calls logout
		serviceHandler.disconnect();

		assertEquals(serviceHandler.getNumberOfOnlineUsers(), 0);
	}

	@Test
	public void testGetNumberOfOnlineUsers() throws Exception {
		serviceHandler.register("test", "test");
		serviceHandler.register("test2", "test");
		serviceHandler.login("test", "test");
		assertEquals(serviceHandler.getNumberOfOnlineUsers(), 1);
		serviceHandler.login("test2", "test");
		assertEquals(serviceHandler.getNumberOfOnlineUsers(), 2);
	}

	@Test
	public void testGetNumberOfRegisteredUsers() throws Exception{
		serviceHandler.register("test", "test");
		assertEquals(serviceHandler.getNumberOfRegisteredUsers(), 1);
		serviceHandler.register("test2", "test");
		assertEquals(serviceHandler.getNumberOfRegisteredUsers(), 2);
	}
}
