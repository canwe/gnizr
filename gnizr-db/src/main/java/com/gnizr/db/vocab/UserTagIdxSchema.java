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
package com.gnizr.db.vocab;

public interface UserTagIdxSchema {
	public static final String TABLE_NAME = "user_tag_idx";
	public static final String ID = TABLE_NAME + ".id";
	public static final String USER_ID = TABLE_NAME + ".user_id";
	public static final String TAG_ID = TABLE_NAME + ".tag_id";
	public static final String COUNT = TABLE_NAME + ".count";
	
	public static final String USER_TAG_ID = "user_tag_id";
	public static final String USER_TAG_COUNT = "user_tag_count";
	
	public static final String ID_COL = ".id";
	public static final String USER_ID_COL = ".user_id";
	public static final String TAG_ID_COL = ".tag_id";
	public static final String COUNT_COL = ".count";
}
