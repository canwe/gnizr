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
package com.gnizr.db.dao.link;

import com.gnizr.db.dao.DBUtil;
import com.gnizr.db.dao.Link;
import com.gnizr.db.vocab.LinkSchema;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("JpaQueryApiInspection")
public class LinkDBDao implements LinkDao {

	/**
	 *
	 */
	private static final long serialVersionUID = 3670309014246507954L;

	private static final Logger logger = Logger.getLogger(LinkDBDao.class.getName());
	private DataSource dataSource;


	public LinkDBDao(DataSource ds) {
		logger.debug("created LinkDBDao. dataSource=" + ds.toString());
		this.dataSource = ds;
	}

	public int createLink(Link link) {
		logger.debug("input: link=" + link);
		Connection conn = null;
		CallableStatement cStmt = null;
		int id = -1;
		try {
			conn = dataSource.getConnection();
			cStmt = conn.prepareCall("select * from createLink(?,?)");
			cStmt.setString(1, link.getUrl());
			cStmt.setInt(2, link.getMimeTypeId());
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

	public boolean deleteLink(int id) {
		logger.debug("input: id=" + id);
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean deleted = false;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from deleteLink(?)");
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

	public List<Link> findLink(String url) {
		logger.debug("input: url=" + url);
		List<Link> links = new ArrayList<Link>();
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from findLinkUrl(?) as f(bookmark_id integer, bookmark_user_id integer, bookmark_link_id integer, bookmark_title text, bookmark_notes text, bookmark_created_on timestamp with time zone, bookmark_last_updated timestamp with time zone, link_id integer, link_mime_type_id integer, link_url text, link_url_hash varchar, link_cnt integer)");
			stmt.setString(1, url);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Link aLink = createLinkObject(rs);
				links.add(aLink);
				logger.debug("found: " + aLink);
			}
			if (links.size() == 0) {
				logger.debug("found no matching links");
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
		return links;
	}

	public static Link createLinkObject(String tableAlias, ResultSet rs) throws SQLException {
		return createNamedLinkObject(tableAlias, rs, true);
	}

	public static Link createNamedLinkObject(String tableAlias, ResultSet rs, boolean noColumnRef) throws SQLException {
		if (rs == null) return null;

		String idCol = tableAlias + "." + LinkSchema.ID;
		String urlCol = tableAlias + "." + LinkSchema.URL;
		String urlHashCol = tableAlias + "." + LinkSchema.URL_HASH;
		String countCol = tableAlias + "." + LinkSchema.COUNT;
		String mimeTypeIdCol = tableAlias + "." + LinkSchema.MIME_TYPE_ID;

		if (noColumnRef) {
			idCol = idCol.replaceAll("\\.", "_");
			urlCol = urlCol.replaceAll("\\.", "_");
			urlHashCol = urlHashCol.replaceAll("\\.", "_");
			countCol = countCol.replaceAll("\\.", "_");
			mimeTypeIdCol = mimeTypeIdCol.replaceAll("\\.", "_");
		}

		Link link = new Link();
		link.setId(rs.getInt(idCol));
		link.setUrl(rs.getString(urlCol));
		link.setMimeTypeId(rs.getInt(mimeTypeIdCol));
		link.setCount(rs.getInt(countCol));
		link.setUrlHash(rs.getString(urlHashCol));
		return link;
	}

	public static Link createLinkObject(ResultSet rs) throws SQLException {
		if (rs == null) return null;
		Link aLink = new Link();
		aLink.setId(rs.getInt(LinkSchema.ID));
		aLink.setUrl(rs.getString(LinkSchema.URL));
		aLink.setMimeTypeId(rs.getInt(LinkSchema.MIME_TYPE_ID));
		aLink.setUrlHash(rs.getString(LinkSchema.URL_HASH));
		aLink.setCount(rs.getInt(LinkSchema.COUNT));
		return aLink;
	}

	public Link getLink(int id) {
		logger.debug("getLink: id=" + id);
		Link aLink = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from getLink(?) as f(bookmark_id integer, bookmark_user_id integer, bookmark_link_id integer, bookmark_title text, bookmark_notes text, bookmark_created_on timestamp with time zone, bookmark_last_updated timestamp with time zone, link_id integer, link_mime_type_id integer, link_url text, link_url_hash varchar, link_cnt integer)");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				aLink = createLinkObject(rs);
				logger.debug("found: " + aLink);
			} else {
				logger.debug("found no matching links");
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
		return aLink;
	}

	public boolean updateLink(Link link) {
		logger.debug("input: link=" + link);
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean isChanged = false;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from updateLink(?,?,?)");
			stmt.setInt(1, link.getId());
			stmt.setString(2, link.getUrl());
			stmt.setInt(3, link.getMimeTypeId());
			stmt.execute();
			if (stmt.getUpdateCount() > 0) {
				logger.debug("updateCount=" + stmt.getUpdateCount());
				isChanged = true;
			}
			stmt.getResultSet();
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			try {
				DBUtil.cleanup(conn, stmt);
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}
		return isChanged;
	}

	public List<Link> findLinkByUrlHash(String urlHash) {
		logger.debug("findLinkByUrlHash: url=" + urlHash);
		List<Link> links = new ArrayList<Link>();
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from findLinkUrlHash(?) as f(bookmark_id integer, bookmark_user_id integer, bookmark_link_id integer, bookmark_title text, bookmark_notes text, bookmark_created_on timestamp with time zone, bookmark_last_updated timestamp with time zone, link_id integer, link_mime_type_id integer, link_url text, link_url_hash varchar, link_cnt integer)");
			stmt.setString(1, urlHash);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Link aLink = createLinkObject(rs);
				links.add(aLink);
				logger.debug("found: " + aLink);
			}
			if (links.size() == 0) {
				logger.debug("found no matching links");
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
		return links;
	}
}
