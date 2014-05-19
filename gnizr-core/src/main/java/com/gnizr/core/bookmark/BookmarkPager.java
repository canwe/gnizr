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
package com.gnizr.core.bookmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.gnizr.core.exceptions.MissingIdException;
import com.gnizr.core.exceptions.NoSuchBookmarkException;
import com.gnizr.core.exceptions.NoSuchLinkException;
import com.gnizr.core.exceptions.NoSuchTagException;
import com.gnizr.core.exceptions.NoSuchUserException;
import com.gnizr.core.exceptions.NoSuchUserTagException;
import com.gnizr.core.util.GnizrDaoUtil;
import com.gnizr.db.dao.Bookmark;
import com.gnizr.db.dao.DaoResult;
import com.gnizr.db.dao.GnizrDao;
import com.gnizr.db.dao.Link;
import com.gnizr.db.dao.Tag;
import com.gnizr.db.dao.User;
import com.gnizr.db.dao.UserTag;
import com.gnizr.db.dao.bookmark.BookmarkDao;
import com.gnizr.db.dao.link.LinkDao;
import com.gnizr.db.dao.tag.TagDao;
import com.gnizr.db.dao.user.UserDao;

/**
 * <p>This class provide methods for implementing bookmark paging capability. Paging methods 
 * usually require two input parameters: (1) start index and (2) count. The "start index" defines
 * the position in the result set where the paging starts. The first position in the matching result
 * set is 0. The "count" defines the maximum number of item to return in a single paging operation.  
 * </p>   
 * <h4>How to page bookmarks</h4>
 * <pre>
 *  // Get the max count
 *  // We set both 'offset' and 'count' to 0 to reduce overhead in the SQL query  
 *      DaoResult&lt;Bookmark&gt; results = bookmarkPager.pageBookmark(user,0,0);
 *  
 *  // Page 10 items starting at position 20
 *      DaoResult&lt;Bookmark&gt; results = bookmarkPager.pageBookmark(user,20,10); 
 * </pre>
 * @author Harry Chen
 *
 */
public class BookmarkPager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8089078507222123887L;

	private static final Logger logger = Logger.getLogger(BookmarkPager.class);

	private BookmarkDao bookmarkDao;

	private UserDao userDao;

	private TagDao tagDao;
	
	private LinkDao linkDao;
	
	/**
	 * Creates an instance of this class and using the input
	 * <code>gnizrDao</code> to support low-level paging calls.
	 * @param gnizrDao an instantiated DAO object.
	 */
	public BookmarkPager(GnizrDao gnizrDao) {
		this.bookmarkDao = gnizrDao.getBookmarkDao();
		this.userDao = gnizrDao.getUserDao();
		this.tagDao = gnizrDao.getTagDao();
		this.linkDao = gnizrDao.getLinkDao();
	}

	
	/**
	 * Returns the maximum number of pages for bookmarks of a given tag. The input
	 * <code>perPageCount</code> defines the number of bookmarks per page.
	 *   
	 * @param tag page only bookmarks that have been tagged <code>tag</code>
	 * @param perPageCount the number of bookmarks to page.
	 * @return the computed maximum number of bookmarks per page.
	 * @throws NoSuchTagException An exception is thrown if <code>tag</code> doesn't exist
	 * in the system.
	 */
	public int getMaxPageNumber(Tag tag, int perPageCount) throws NoSuchTagException{
		logger.debug("getMaxPageNumber: tag="+tag+",perPageCount="+perPageCount);
		int max = 1;
		Tag aTag = new Tag(tag);
		GnizrDaoUtil.fillId(tagDao, aTag);
		int numOfbmarks = bookmarkDao.getBookmarkCount(aTag);
		if (numOfbmarks > 0) {
			if (perPageCount > 0) {
				int tnp = numOfbmarks / perPageCount;
				if ((numOfbmarks % perPageCount) > 0) {
					tnp++;
				}
				if (tnp > 1) {
					max = tnp;
				}
			}
		}
		return max;
	}
	
	/**
	 * Returns the maximum number of pages for bookmarks saved by a given user. The input
	 * <code>perPageCount</code> defines the number of bookmarks per page.
	 *   
	 * @param user page only bookmarks that have been saved by <code>user</code>
	 * @param perPageCount the number of bookmarks to page.
	 * @return the computed maximum number of bookmarks per page.
	 * @throws NoSuchUserException An exception is thrown if <code>user</code> doesn't exist
	 * in the system.
	 */
	public int getMaxPageNumber(User user, Integer perPageCount) throws NoSuchUserException {
		logger.debug("getMaxPageNumber: gUser=" + user + ",perPageCount="
				+ perPageCount);
		int max = 1;
		GnizrDaoUtil.checkNull(user);
		User aUser = new User(user);
		GnizrDaoUtil.fillId(userDao,aUser);		
		int numOfBmarks = bookmarkDao.getBookmarkCount(aUser);
		if (numOfBmarks > 0) {
			if (perPageCount != null && perPageCount > 0) {
				int tnp = numOfBmarks / perPageCount;
				if ((numOfBmarks % perPageCount) > 0) {
					tnp++;
				}
				if (tnp > 1) {
					max = tnp;
				}
			}
		}
		return max;
	}

	/**
	 * Returns the maximum number of pages for bookmarks saved by a given user under a specific tag. The input
	 * <code>perPageCount</code> defines the number of bookmarks per page.
	 *   
	 * @param userTag page only bookmarks that have been saved by <code>userTag.getUser</code> and tagged <code>userTag.getTag</code>
	 * @param perPageCount the number of bookmarks to page.
	 * @return the computed maximum number of bookmarks per page.
	 * @throws NoSuchUserException An exception is thrown if <code>userTag.getUser</code> doesn't exist in the system.
	 * @throws NoSuchTagException An exception is thrown if <code>userTag.getTag</code> doesn't exist
	 * in the system.
	 * @throws MissingIdException An exception is thrown if the ID of <code>userTag</code> is not defined. 
	 */
	public int getMaxPageNumber(UserTag tag, Integer perPageCount) throws NoSuchUserException, NoSuchTagException, NoSuchUserTagException, MissingIdException{
		logger.debug("getMaxPageNumber: userTag=" + tag
				+ ",perPageCount=" + perPageCount);
		int max = 1;
		GnizrDaoUtil.checkNull(tag);
		UserTag userTag = new UserTag(tag);
		GnizrDaoUtil.fillId(tagDao,userDao,userTag);
		GnizrDaoUtil.fillObject(tagDao,userDao,userTag);
		if (userTag != null) {
			int numOfBmarks = userTag.getCount();
			if (numOfBmarks > 0) {
				if (perPageCount != null && perPageCount > 0) {
					int tnp = numOfBmarks / perPageCount;
					if ((numOfBmarks % perPageCount) > 0) {
						tnp++;
					}
					if (tnp > 1) {
						max = tnp;
					}
				}
			}
		}
		return max;
	}
	
	/**
	 * Returns a page of the bookmarks saved by a given user. The paging starts at <code>offset</code>
	 * and returns at maximum no more than <code>count</code> number of bookmarks.
	 * @param user search bookmarks saved by this user
	 * @param offset the starting index of the paging
	 * @param count the maximum of bookmarks to return
	 * @return bookmarks in this page. Unless an exception is thrown, otherwise,
	 * the returned object is always instantied. If no bookmarks are found, the result set 
	 * is of size 0.
	 * 
	 * @throws NoSuchUserException
	 * @throws NoSuchLinkException
	 * @throws MissingIdException
	 * @throws NoSuchBookmarkException
	 */
	public DaoResult<Bookmark> pageBookmark(User user, int offset,
			int count) throws NoSuchUserException, NoSuchLinkException, MissingIdException, NoSuchBookmarkException {
		logger.debug("pageGnizrBookmark: user=" + user + ",offset=" + offset
				+ ",count=" + count);
		GnizrDaoUtil.fillId(userDao,user);
		return bookmarkDao.pageBookmarks(user, offset, count);		
	}

	/**
	 * Returns a page of the bookmarks saved by a given user under a specific tag. 
	 * The paging starts at <code>offset</code>
	 * and returns at maximum no more than <code>count</code> number of bookmarks.
	 * @param userTag search bookmarks saved by a user under a specific tag.
	 * @param offset the starting index of the paging
	 * @param count the maximum of bookmarks to return
	 * @return bookmarks in this page. Unless an exception is thrown, otherwise,
	 * the returned object is always instantied. If no bookmarks are found, the result set 
	 * is of size 0.
	 * 
	 * @throws NoSuchUserException
	 * @throws NoSuchLinkException
	 * @throws MissingIdException
	 * @throws NoSuchBookmarkException
	 */
	public DaoResult<Bookmark> pageBookmark(UserTag tag, int offset,
			int count) throws NoSuchUserException, NoSuchTagException, NoSuchUserTagException, MissingIdException, NoSuchLinkException, NoSuchBookmarkException {
		logger.debug("pageGnizrBookmark: userTag=" + tag
				+ ",offset=" + offset + ",count=" + count);
		GnizrDaoUtil.checkNull(tag);
		GnizrDaoUtil.checkNull(tag.getTag());
		GnizrDaoUtil.checkNull(tag.getUser());
		User aUser = tag.getUser();
		Tag aTag = tag.getTag();
		GnizrDaoUtil.fillId(userDao,aUser);
		GnizrDaoUtil.fillId(tagDao,aTag);		
		if (tag != null) {
			return bookmarkDao.pageBookmarks(aUser,aTag,offset,count);			
		}
		return new DaoResult<Bookmark>(new ArrayList<Bookmark>(),0);
	}
	
	/**
	 * Returns a page of the bookmarks saved by any one under a specific tag. 
	 * The paging starts at <code>offset</code>
	 * and returns at maximum no more than <code>count</code> number of bookmarks.
	 * @param tag search bookmarks saved under a specific tag.
	 * @param offset the starting index of the paging
	 * @param count the maximum of bookmarks to return
	 * @return bookmarks in this page. Unless an exception is thrown, otherwise,
	 * the returned object is always instantied. If no bookmarks are found, the result set 
	 * is of size 0.
	 * 
	 */
	public List<Bookmark> pageBookmark(Tag tag, int offset, int count) throws NoSuchTagException{
		logger.debug("pageLink: tag="+tag+",offset="+offset+",count="+count);		
		GnizrDaoUtil.checkNull(tag);
		//make a local copy
		Tag aTag = new Tag(tag);
		GnizrDaoUtil.fillId(tagDao,aTag);
		DaoResult<Bookmark> result = bookmarkDao.pageBookmarks(aTag, offset, count);
		return result.getResult();
	}
	
	/**
	 * Returns all bookmarks saved by a given user.
	 * @param user page the bookmarks of this user
	 * @return all bookmarks saved by <code>user</code>
	 * @throws NoSuchUserException
	 */
	public DaoResult<Bookmark> pageAllBookmark(User user) throws NoSuchUserException{
		GnizrDaoUtil.fillId(userDao, user);
		DaoResult<Bookmark> result = bookmarkDao.pageBookmarks(user,0,0);
		return bookmarkDao.pageBookmarks(user,0, result.getSize());
	}
	
	/**
	 * Returns all bookmarks saved by a user under a specific tag.
	 * 
	 * @param user page bookmarks of a given user.
	 * @param tag page bookmarks of <code>user</code> that are saved under <code>tag</code>
	 * @return all bookmarks saved by <code>user</code> under <code>tag</code>
	 * @throws NoSuchUserException
	 * @throws NoSuchTagException
	 */
	public DaoResult<Bookmark> pageAllBookmark(User user, Tag tag) throws NoSuchUserException, NoSuchTagException{
		GnizrDaoUtil.fillId(userDao, user);
		GnizrDaoUtil.fillId(tagDao, tag);
		DaoResult<Bookmark> result = bookmarkDao.pageBookmarks(user, tag,0,0);
		return bookmarkDao.pageBookmarks(user, tag,0, result.getSize());
	}
	
	/**
	 * Returns all bookmarks saved under a given tag.
	 * 
	 * @param tag page bookmarks that are saved under <code>tag</code> 
	 * @return all bookmarks saved under <code>tag</code>
	 * @throws NoSuchTagException
	 */
	public DaoResult<Bookmark> pageAllBookmark(Tag tag) throws NoSuchTagException{
		GnizrDaoUtil.fillId(tagDao, tag);
		DaoResult<Bookmark> result = bookmarkDao.pageBookmarks(tag,0, 0);
		return bookmarkDao.pageBookmarks(tag,0, result.getSize());
	}
	
	/**
	 * Returns all bookmarks that are about a given <code>Link</code>. 
	 * @param link page bookmarks of <code>link</code>
	 * @return all bookmarks that are saved for <code>link</code>
	 * @throws NoSuchLinkException
	 */
	public DaoResult<Bookmark> pageAllBookmark(Link link) throws NoSuchLinkException{
		GnizrDaoUtil.fillId(linkDao, link);
		DaoResult<Bookmark> result = bookmarkDao.pageBookmarks(link, 0,0);
		return bookmarkDao.pageBookmarks(link,0,result.getSize());
	}
	
}
