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
package com.gnizr.core.search;

import java.io.Serializable;

public class OpenSearchService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7124626370222484815L;
	
	private String serviceUrlPattern;
	private String shortName;
	private String description;
	private String[] tags;
	private String type;
	private boolean supportsPageBased;
	private boolean supportsIndexBased;
	private boolean loginRequired;
	private boolean defaultEnabled;

	/**
	 * Flag that identifies a service that output opensearch result in a syndication XML format (RSS or Atom). 
	 * This flag has string value: <code>synd</code>
	 */
	public static final String TYPE_SYND = "synd";
	/**
	 * Flag that identifies a service that output opensearch result in a gnizr-specific JSON format.
	 * This flag has string value: <code>gn-json</code>
	 */
	public static final String TYPE_GN_JSON = "gn-json";
	
	public OpenSearchService(){
		tags = new String[0];
	}

	public boolean isDefaultEnabled() {
		return defaultEnabled;
	}

	public void setDefaultEnabled(boolean isDefaultEnabled) {
		this.defaultEnabled = isDefaultEnabled;
	}

	public boolean isLoginRequired() {
		return loginRequired;
	}

	public void setLoginRequired(boolean loginRequired) {
		this.loginRequired = loginRequired;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getServiceUrlPattern() {
		return serviceUrlPattern;
	}
	public void setServiceUrlPattern(String serviceUrlPattern) {
		this.serviceUrlPattern = serviceUrlPattern;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public boolean isSupportsIndexBased() {
		return supportsIndexBased;
	}
	public void setSupportsIndexBased(boolean supportsIndexBased) {
		this.supportsIndexBased = supportsIndexBased;
	}
	public boolean isSupportsPageBased() {
		return supportsPageBased;
	}
	public void setSupportsPageBased(boolean supportsPageBased) {
		this.supportsPageBased = supportsPageBased;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
