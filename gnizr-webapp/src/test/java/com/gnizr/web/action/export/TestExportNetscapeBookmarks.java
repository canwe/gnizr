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
package com.gnizr.web.action.export;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import com.gnizr.core.folder.FolderManager;
import com.gnizr.core.user.UserManager;
import com.gnizr.core.web.junit.GnizrWebappTestBase;
import com.gnizr.web.util.GnizrConfiguration;
import com.opensymphony.xwork.ActionSupport;

public class TestExportNetscapeBookmarks extends GnizrWebappTestBase {

	private GnizrConfiguration gnizrConfiguration;
	private FolderManager folderManager;
	private UserManager userManager;
	private ExportNetscapeBookmarks action;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		gnizrConfiguration = new GnizrConfiguration();
		gnizrConfiguration.setTempDirectoryPath("target");
		
		folderManager = new FolderManager(getGnizrDao());
		userManager = new UserManager(getGnizrDao());
		
		action = new ExportNetscapeBookmarks();
		action.setFolderManager(folderManager);
		action.setGnizrConfiguration(gnizrConfiguration);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGo() throws Exception{
		action.setLoggedInUser(userManager.getUser("hchen1"));
		
		String exportedData = "";
		
		String op = action.execute();
		assertEquals(ActionSupport.SUCCESS,op);
		InputStream is = action.getNetscapeBookmarkStream();
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader bfReader = new BufferedReader(reader);
		try{
			StringBuilder sb = new StringBuilder();
			String s = bfReader.readLine();
			while(s != null){
				sb.append(s);
				sb.append("\n");
				s = bfReader.readLine();
			}
			exportedData = sb.toString();
		}finally{
			if(bfReader != null){
				bfReader.close();
			}
			if(is != null){
				is.close();
			}
		}	
		System.out.println(exportedData);
	}
	
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSet(TestExportNetscapeBookmarks.class.getResourceAsStream("/TestExportNetscapeBookmarks-input.xml"));
	}

}
