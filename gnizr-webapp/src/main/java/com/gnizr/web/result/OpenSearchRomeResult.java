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
package com.gnizr.web.result;

import com.gnizr.web.action.OpenSearchResultAware;
import com.opensymphony.xwork.ActionInvocation;

/**
 * <p>An extended {@link RomeResult} to output OpenSearch Response in ATOM. This class extends the function provided 
 * by {@link RomeResult} by fixing RSS parameter values: <code>mimeType</code>, <code>feedType</code>
 * and <code>feedName</code>.</p>
 * <pre>
 *     name          value
 *     mimeType     text/xml
 *     feedType     atom_1.0
 *     feedName     openSearchResult
 * </pre>
 * 
 * 
 * <h3>How to use this <code>Result</code> in the xwork configuration</h3>
 * <pre>
 *  // SearchBookmark.java
 * public class SearchBookmark extends Action {
 *   ...
 *   // required method "getOpenSearchResult"
 *   public SyndFeed getOpenSearchResult(){
 *     ...
 *     return feed;
 *   }
 * }
 * </pre>
 * <pre>
 *  // xwork configuration 
 * &lt;action name="search" class="com.gnizr.web.action.search.SearchBookmark"&gt;
 *   ...
 *   &lt;result name="success" type="opensearch"&gt;								
 *     &lt;param name="encoding"&gt;UTF-8&lt;/param&gt;				
 *   &lt;/result&gt;	
 * &lt;/action&gt;
 * </pre>
 * 
 * @author Harry Chen
 * @since 2.3
 *
 */
public class OpenSearchRomeResult extends RomeResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6548652321191974405L;
	
	/**
	 * Executes the action to create Open Search Response. The <code>SyndFeed</code> 
	 * object is already created with MIME-TYPE set to <code>text/xml</code>, feed type set to 
	 * <code>atom_1.0</code>. The class of <code>actionInvocation</code> 
	 * must implement {@link OpenSearchResultAware}.
	 */
	@Override
	public void execute(ActionInvocation actionInvocation) throws Exception {
		super.setMimeType("text/xml");
		super.setFeedType("atom_1.0");
		super.setFeedName("openSearchResult");
		super.execute(actionInvocation);
	}

}
