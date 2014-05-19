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
package com.gnizr.db.dao;

import java.io.Serializable;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * <p>This class provides an implementation of <code>BasicDataSource</code> that allow
 * JDBC connection properties to be configured via a Java <code>Properties</code></p>
 * <p>This class used for creating a <code>BasicDataSource</code> via Spring IoC.</p>
 * 
 * @author Harry Chen
 *
 */
public class GnizrBasicDataSource extends BasicDataSource implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8131969966130166615L;

	/**
	 * Sets the connection property by reading configurations from the input <code>Properties</code>
	 * object. When reading configurations from <code>props</code>, key/value pairs in <code>props</code>
	 * are assumed to be of type <code>String</code>. The key is the name of the property, and the value
	 * is the value to be set for that property.
	 * @param props an instantiated properties object -- keys are the connection property names,
	 * and values are the values to be set for the corresponding connection properties. 
	 */
	@SuppressWarnings("unchecked")
	public void setConnectionProperties(Properties props) {
		for (Iterator iter = props.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			addConnectionProperty((String) entry.getKey(), (String)entry.getValue());
		}
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return Logger.getLogger("gnizr-db-logger");
	}
}
