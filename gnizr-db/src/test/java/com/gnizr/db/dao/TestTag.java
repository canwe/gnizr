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


import junit.framework.TestCase;

public class TestTag extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCopyConstructor() throws Exception{
		Tag t = new Tag();
		t.setId((210303));
		t.setCount((39394));
		t.setLabel("fjdks");
		
		Tag t2 = new Tag(t);
		assertEquals(t.getId(),t2.getId());
		assertEquals(t.getCount(),t2.getCount());
		assertEquals(t.getLabel(),t2.getLabel());
	}

}
