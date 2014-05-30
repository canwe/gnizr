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

public interface FolderSchema {

	public static final String TABLE_NAME = "folder";
	public static final String ID = TABLE_NAME + "_id";
	public static final String FOLDER_NAME = "folder_name";
	public static final String OWNER_ID = "owner_id";
	public static final String DESCRIPTION = "description";
	public static final String LAST_UPDATED = "last_updated";
	public static final String SIZE = "folder_size";
}
