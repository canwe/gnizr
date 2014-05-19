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
package com.gnizr.web.action.robot.rss;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.gnizr.core.robot.rss.CrawlRssFeed;
import com.gnizr.web.action.AbstractAction;

public class RssRobotService extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2313235266475403677L;

	/**
	 * Key string for fetching a boolean value in the <code>GnizrConfiguration</code>
	 * that determines whether or not this service is enabled.
	 */
	public static final String SERVICE_ENABLED_KEY = "gnizr.rssrobot.enabled";
	
	private static final Logger logger = Logger
			.getLogger(RssRobotService.class);
	
	private boolean status;
	private CrawlRssFeed service;
	
	public CrawlRssFeed getCrawlRssFeed() {
		return service;
	}

	public void setCrawlRssFeed(CrawlRssFeed service) {
		this.service = service;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String doDefault() throws Exception{
		logger.debug("RssRobotService.doDefault");
		this.status = service.isServiceEnabled();
		return SUCCESS;
	}
	
	@Override
	protected String go() throws Exception {
		logger.debug("RssRobotService.go()");
		service.setServiceEnabled(isStatus());
		Properties runtimeProperties = getGnizrConfiguration().getAppProperties();
		runtimeProperties.setProperty(SERVICE_ENABLED_KEY,String.valueOf(isStatus()));
		return SUCCESS;
	}

}
