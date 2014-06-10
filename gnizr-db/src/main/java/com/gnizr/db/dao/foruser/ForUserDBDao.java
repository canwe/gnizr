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
package com.gnizr.db.dao.foruser;

import com.gnizr.db.dao.*;
import com.gnizr.db.dao.bookmark.BookmarkDBDao;
import com.gnizr.db.dao.link.LinkDBDao;
import com.gnizr.db.dao.user.UserDBDao;
import com.gnizr.db.vocab.BookmarkSchema;
import com.gnizr.db.vocab.ForUserSchema;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SuppressWarnings("JpaQueryApiInspection")
public class ForUserDBDao implements ForUserDao {

	/**
	 *
	 */
	private static final long serialVersionUID = 7808067339634428740L;

	private static final Logger logger = Logger.getLogger(BookmarkDBDao.class.getName());

	private DataSource dataSource;

	public ForUserDBDao(DataSource ds) {
		this.dataSource = ds;
	}

	/* (non-Javadoc)
	 * @see com.gnizr.db.dao.foruser.ForUserDao#createForUser(com.gnizr.db.dao.ForUser)
	 */
	public int createForUser(ForUser forUser) {
		logger.debug("input: forUser=" + forUser);
		Connection conn = null;
		CallableStatement cStmt = null;
		int id = -1;
		try {
			conn = dataSource.getConnection();
			cStmt = conn.prepareCall("select * from createForUser(?,?,?,?)");
			cStmt.setInt(1, forUser.getForUser().getId());
			cStmt.setInt(2, forUser.getBookmark().getId());
			cStmt.setString(3, forUser.getMessage());
			cStmt.setTimestamp(4, new Timestamp(forUser.getCreatedOn().getTime()));
			ResultSet rs = cStmt.executeQuery();
			id = rs.next() ? rs.getInt(1) : id;
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, cStmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see com.gnizr.db.dao.foruser.ForUserDao#deleteForUser(com.gnizr.db.dao.ForUser)
	 */
	public boolean deleteForUser(int id) {
		logger.debug("input: id=" + id);
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean deleted = false;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareCall("{call deleteForUser(?)}");
			stmt.setLong(1, id);
			if (stmt.executeUpdate() > 0) {
				logger.debug("# row deleted=" + stmt.getUpdateCount());
				deleted = true;
			}
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return deleted;
	}

	/* (non-Javadoc)
	 * @see com.gnizr.db.dao.foruser.ForUserDao#getForUser(int)
	 */
	public ForUser getForUser(int id) {
		logger.debug("input: id=" + id);
		ForUser forUser = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from getForUser(?);");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				forUser = createForUserObject(rs);
				logger.debug("found: " + forUser);
			} else {
				logger.debug("found no matching forUser");
			}
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}

		return forUser;
	}

	/* (non-Javadoc)
	 * @see com.gnizr.db.dao.foruser.ForUserDao#pageForUser(com.gnizr.db.dao.User)
	 */
	public List<ForUser> pageForUser(User user, int offset, int count) {
		logger.debug("pageForUser, input: user=" + user
				+ ",offset=" + offset + ",count=" + count);
		Connection conn = null;
		PreparedStatement stmt = null;
		List<ForUser> linksForUser = new ArrayList<ForUser>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from pageForUser(?,?,?)");
			stmt.setInt(1, user.getId());
			stmt.setInt(2, offset);
			stmt.setInt(3, count);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ForUser forUser = createForUserObject(rs);
				logger.debug("found forUser=" + forUser);
				linksForUser.add(forUser);
			}
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return linksForUser;
	}

	/* (non-Javadoc)
	 * @see com.gnizr.db.dao.foruser.ForUserDao#updateForUser(com.gnizr.db.dao.ForUser)
	 */
	public boolean updateForUser(ForUser forUser) {
		logger.debug("input: forUser=" + forUser);
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean updated = false;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from updateForUser(?,?,?,?,?)");
			stmt.setInt(1, forUser.getId());
			stmt.setInt(2, forUser.getForUser().getId());
			stmt.setInt(3, forUser.getBookmark().getId());
			stmt.setString(4, forUser.getMessage());
			stmt.setTimestamp(5, new Timestamp(forUser.getCreatedOn().getTime()));
			stmt.execute();
			if (stmt.executeUpdate() > 0) {
				logger.debug("# row updated=" + stmt.getUpdateCount());
				updated = true;
			}
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return updated;
	}

	public boolean hasForUser(Bookmark bookmark, User user) {
		boolean found = false;

		logger.debug("input: bookmark=" + bookmark + ", user=" + user);
		Connection conn = null;
		CallableStatement cStmt = null;
		int count = 0;
		try {
			conn = dataSource.getConnection();
			cStmt = conn.prepareCall("select * from hasForUser(?,?)");
			cStmt.setInt(1, bookmark.getId());
			cStmt.setInt(2, user.getId());
			ResultSet rs = cStmt.executeQuery();
			count = rs.next() ? rs.getInt(1) : count;
			if (count > 0) found = true;
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, cStmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}

		return found;
	}

	public int getForUserCount(User user) {
		Connection conn = null;
		CallableStatement cStmt = null;
		int count = 0;
		try {
			conn = dataSource.getConnection();
			cStmt = conn.prepareCall("select * from getForUserCount(?)");
			cStmt.setInt(1, user.getId());
			ResultSet rs = cStmt.executeQuery();
			count = rs.next() ? rs.getInt(1) : count;
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, cStmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return count;
	}

	public List<ForUser> findForUserFromBookmark(Bookmark bookmark) {
		logger.debug("pageForUser, input: bookmark=" + bookmark);
		Connection conn = null;
		PreparedStatement stmt = null;
		List<ForUser> linksForUser = new ArrayList<ForUser>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from listForUserFromBookmark(?)");
			stmt.setInt(1, bookmark.getId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ForUser forUser = createForUserObject(rs);
				logger.debug("found forUser=" + forUser);
				linksForUser.add(forUser);
			}
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return linksForUser;
	}

	private ForUser createForUserObject(ResultSet rs) throws SQLException {
		ForUser forUser = new ForUser();

		forUser.setId(rs.getInt(ForUserSchema.ID));
		forUser.setMessage(rs.getString(ForUserSchema.MESSAGE));
		forUser.setCreatedOn(rs.getTimestamp(ForUserSchema.CREATED_ON));

		Bookmark bm = BookmarkDBDao.createBookmarkObject(BookmarkSchema.TABLE_NAME, rs);
		Link bmLink = LinkDBDao.createLinkObject(rs);
		User buser = UserDBDao.createUserObject("buser", rs);
		bm.setLink(bmLink);
		bm.setUser(buser);

		User fuser = UserDBDao.createUserObject("fuser", rs);

		forUser.setBookmark(bm);
		forUser.setForUser(fuser);

		return forUser;
	}


	public List<ForUser> pageForUser(User user, Date start, Date end, int offset, int count) {
		logger.debug("findForUser, input: user=" + user + ",start=" + start + ",end=" + end);
		Connection conn = null;
		PreparedStatement stmt = null;
		List<ForUser> linksForUser = new ArrayList<ForUser>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from pageForUserInTimeRange(?,?,?,?,?)");
			stmt.setInt(1, user.getId());
			stmt.setTimestamp(2, new Timestamp(start.getTime()));
			stmt.setTimestamp(3, new Timestamp(end.getTime()));
			stmt.setInt(4, offset);
			stmt.setInt(5, count);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ForUser forUser = createForUserObject(rs);
				logger.debug("found forUser=" + forUser);
				linksForUser.add(forUser);
			}
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return linksForUser;
	}

	public int getForUserCount(User user, Date start, Date end) {
		Connection conn = null;
		CallableStatement cStmt = null;
		int count = 0;
		try {
			conn = dataSource.getConnection();
			cStmt = conn.prepareCall("select * from getForUserInTimeRangeCount(?,?,?)");
			cStmt.setInt(1, user.getId());
			cStmt.setTimestamp(2, new Timestamp(start.getTime()));
			cStmt.setTimestamp(3, new Timestamp(end.getTime()));
			ResultSet rs = cStmt.executeQuery();
			count = rs.next() ? rs.getInt(1) : count;
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, cStmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return count;
	}

	public boolean deleteAllForUser(User user) {
		logger.debug("deleteAllForUser: user=" + user);
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean deleted = false;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("{call deleteAllForUser(?)}");
			stmt.setInt(1, user.getId());
			if (stmt.executeUpdate() > 0) {
				logger.debug("# row deleted=" + stmt.getUpdateCount());
				deleted = true;
			}
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return deleted;
	}

	public boolean deleteForUser(User user, int[] ids) {
		logger.debug("deleteForUser: user=" + user + ",ids=" + Arrays.toString(ids));
		Connection conn = null;
		CallableStatement stmt = null;
		boolean deleted = false;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareCall("{call deleteForUserById(?,?)}");
			for (int id : ids) {
				stmt.setInt(1, user.getId());
				stmt.setInt(2, id);
				stmt.addBatch();
			}
			stmt.executeBatch();
			return true;
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return deleted;
	}

	public DaoResult<ForUser> pageForUser(User user, User sender, int offset, int count) {
		logger.debug("pageForUser: user=" + user + ",sender=" + sender + ",offset=" + offset + ",count=" + count);
		List<ForUser> forUsers = new ArrayList<ForUser>();
		DaoResult<ForUser> result = null;
		CallableStatement cStmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			cStmt = conn.prepareCall("select * from pageForUserBySenderId(?,?,?,?)");
			cStmt.setInt(1, user.getId());
			cStmt.setInt(2, sender.getId());
			cStmt.setInt(3, offset);
			cStmt.setInt(4, count);
			ResultSet rs = cStmt.executeQuery();
			int size = rs.next() ? rs.getInt(1) : -1;
			if (size < 0) {
				size = 0;
			}
			//TODO
			//while (rs.next()) {
			//	ForUser forUser = createForUserObject(rs);
			//	forUsers.add(forUser);
			//}
			result = new DaoResult<ForUser>(forUsers, size);
			logger.debug("DaoResult: forUsers=" + forUsers + ",size=" + size);
		} catch (Exception e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, cStmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return result;
	}

	public List<User> listForUserSenders(User user) {
		logger.debug("listForUserSenders, input: user=" + user);
		Connection conn = null;
		PreparedStatement stmt = null;
		List<User> senders = new ArrayList<User>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from listForUserSenders(?)");
			stmt.setInt(1, user.getId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User sender = UserDBDao.createUserObject(rs);
				logger.debug("found sender=" + sender);
				senders.add(sender);
			}
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return senders;
	}
}
