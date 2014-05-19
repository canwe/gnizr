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

import com.gnizr.db.dao.User;
import com.gnizr.web.action.LoggedInUserAware;
import com.gnizr.web.action.SessionConstants;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * An {@link com.opensymphony.xwork.interceptor.Interceptor} implementation for
 * checking user login authentication. When this <code>Interceptor</code> is 
 * used on a WebWork Action, it will allow an incoming request to pass if and only
 * if that the current session stores a valid logged-in user entry. If no login
 * information is find, this <code>Interceptor</code> will redirect the
 * request to the <code>LOGIN</code> result.  
 *   
 * @author Harry Chen
 * @since 2.2
 */
public class AuthenticationInterceptor implements Interceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9000709873811063043L;

	public void destroy() {
		// no code
	}

	public void init() {
		// no code
	}

	@SuppressWarnings("unchecked")
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		Map session = actionInvocation.getInvocationContext().getSession();
		User user = (User)session.get(SessionConstants.LOGGED_IN_USER);
		if(user == null){
			return Action.LOGIN;
		}else{
			Action action = (Action)actionInvocation.getAction();
			if(action instanceof LoggedInUserAware){
				((LoggedInUserAware)action).setLoggedInUser(user);
			}
			return actionInvocation.invoke();
		}
	}

}
