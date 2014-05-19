/*
 * gnizr is a trademark of Image Matters LLC in the United States.
 * 
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either expressed or implied. See the License
 * for the specific language governing rights and limitations under the License.
 * 
 * The Initial Contributor of the Original Code is Image Matters LLC.
 * Portions created by the Initial Contributor are Copyright (C) 2007
 * Image Matters LLC. All Rights Reserved.
 */
package com.gnizr.web.action.user;

import java.util.HashMap;
import java.util.Map;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import com.gnizr.core.user.UserManager;
import com.gnizr.core.util.TokenManager;
import com.gnizr.core.web.junit.GnizrWebappTestBase;
import com.gnizr.db.dao.User;
import com.gnizr.db.vocab.AccountStatus;
import com.opensymphony.xwork.ActionSupport;

public class TestActivateUserAccount extends GnizrWebappTestBase {

	private UserManager userManager;
	private TokenManager tokenManager;
	private ActivateUserAccount action;
	
	private String username;
	private String token;
	
	private Map<String, Object> session;
	
	protected void setUp() throws Exception {
		super.setUp();
		userManager = new UserManager(getGnizrDao());
		tokenManager = new TokenManager();
		tokenManager.setUserManager(userManager);
		tokenManager.init();
		
		session = new HashMap<String, Object>();
		
		action = new ActivateUserAccount();
		action.setUserManager(userManager);
		action.setTokenManager(tokenManager);
		action.setSession(session);
		
		username = "hchen1";
		token = tokenManager.createResetToken(new User(username));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSet(TestActivateUserAccount.class
				.getResourceAsStream("/TestActivateUserAccount-input.xml"));
	}

	public void testGo() throws Exception{
		User user = userManager.getUser(username);
		assertEquals(AccountStatus.INACTIVE,user.getAccountStatus().intValue());
		
		action.setUsername(username);
		action.setToken(token);
		
		String op = action.execute();
		assertEquals(ActionSupport.SUCCESS,op);
		
		user = userManager.getUser(username);
		assertEquals(AccountStatus.ACTIVE,user.getAccountStatus().intValue());
		
		assertFalse(tokenManager.isValidResetToken(token, user));
	}
	
	
	
}
