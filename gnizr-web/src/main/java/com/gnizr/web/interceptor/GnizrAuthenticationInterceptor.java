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
package com.gnizr.web.interceptor;

import java.util.Map;

import com.gnizr.core.exceptions.NoSuchUserException;
import com.gnizr.core.user.UserManager;
import com.gnizr.db.dao.User;
import com.gnizr.web.action.LoggedInUserAware;
import com.gnizr.web.action.SessionConstants;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * An {@link com.opensymphony.xwork.interceptor.Interceptor} implementation for 
 * checking whether or not a given incoming request contains properly user-authentication
 * information for the user <code>gnizr</code>. This <code>Interceptor</code> is typically
 * used to support the running of a gnizr application under the "maintenance" mode. When
 * running in the "maintenance" mode, no users are allowed to login, exception for the superuser 
 * <code>gnizr</code>. 
 * 
 * @author Harry Chen
 * @since 2.3
 *
 */
public class GnizrAuthenticationInterceptor implements Interceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5194588686597399219L;

	public void destroy() {
		// no code
	}

	public void init() {
		// no code
	}
	
	private UserManager userManager;

	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		Map session = actionInvocation.getInvocationContext().getSession();
		User user = (User)session.get(SessionConstants.LOGGED_IN_USER);
		if(user == null || isGnizrUser(user) == false){
			if(user != null){
				session.remove(SessionConstants.LOGGED_IN_USER);
			}
			return Action.LOGIN;
		}else{	
			Action action = (Action)actionInvocation.getAction();
			if(action instanceof LoggedInUserAware){
				((LoggedInUserAware)action).setLoggedInUser(user);
			}
			return actionInvocation.invoke();
		}
	}

	private boolean isGnizrUser(User user){
		boolean isGnizrUser = false;
		try {
			User gnizr = userManager.getUser("gnizr");
			if(gnizr != null && gnizr.equals(user)){
				isGnizrUser = true;
			}
		} catch (NoSuchUserException e) {
			// no code;
		}
		return isGnizrUser;
		
	}
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
