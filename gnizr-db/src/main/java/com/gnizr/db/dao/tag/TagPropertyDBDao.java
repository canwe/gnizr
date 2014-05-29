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
package com.gnizr.db.dao.tag;

import com.gnizr.db.dao.DBUtil;
import com.gnizr.db.dao.TagProperty;
import com.gnizr.db.vocab.TagPropertySchema;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("JpaQueryApiInspection")
public class TagPropertyDBDao implements TagPropertyDao {

	/**
	 *
	 */
	private static final long serialVersionUID = 5100035689234770684L;

	private static final Logger logger = Logger.getLogger(TagPropertyDBDao.class);
	private DataSource dataSource;

	public TagPropertyDBDao(DataSource ds) {
		logger.debug("created TagPropertyDBDao. dataSource=" + ds);
		this.dataSource = ds;
	}

	public int createTagProperty(TagProperty tagPrpt) {
		logger.debug("input: tagPrpt=" + tagPrpt);
		Connection conn = null;
		CallableStatement cStmt = null;
		int id = -1;
		try {
			conn = dataSource.getConnection();
			cStmt = conn.prepareCall("{? = call createTagProperty(?,?,?,?,?)}");
			cStmt.registerOutParameter(1, Types.INTEGER);
			cStmt.setString(2, tagPrpt.getName());
			cStmt.setString(3, tagPrpt.getNamespacePrefix());
			cStmt.setString(4, tagPrpt.getDescription());
			cStmt.setString(5, tagPrpt.getPropertyType());
			cStmt.setInt(6, tagPrpt.getCardinality());
			cStmt.execute();
			id = cStmt.getInt(1);
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

	public boolean deleteTagProperty(int id) {
		logger.debug("deleteTagProperty: id=" + id);
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean deleted = false;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareCall("{call deleteTagProperty(?)}");
			stmt.setInt(1, id);
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


	public TagProperty getTagProperty(int id) {
		logger.debug("input: id=" + id);
		TagProperty tagPrpt = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from getTagProperty(?)");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				tagPrpt = createTagPropertyObject(rs);
				logger.debug("found: " + tagPrpt);
			} else {
				logger.debug("found no matching tag property");
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
		return tagPrpt;
	}

	public static TagProperty createTagPropertyObject(ResultSet rs) throws SQLException {
		if (rs == null) return null;
		TagProperty tagPrpt = new TagProperty();
		tagPrpt.setId(rs.getInt(TagPropertySchema.ID));
		tagPrpt.setName(rs.getString(TagPropertySchema.NAME));
		tagPrpt.setDescription(rs.getString(TagPropertySchema.DESCRIPTION));
		tagPrpt.setNamespacePrefix(rs.getString(TagPropertySchema.NS_PREFIX));
		tagPrpt.setPropertyType(rs.getString(TagPropertySchema.PRPT_TYPE));
		tagPrpt.setCardinality(rs.getInt(TagPropertySchema.CARDINALITY));
		return tagPrpt;
	}

	public boolean updateTagProperty(TagProperty tagPrpt) {
		logger.debug("input: tagPrpt=" + tagPrpt);
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean isChanged = false;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareCall("{call updateTagProperty(?,?,?,?,?,?)}");
			stmt.setInt(1, tagPrpt.getId());
			stmt.setString(2, tagPrpt.getName());
			stmt.setString(3, tagPrpt.getNamespacePrefix());
			stmt.setString(4, tagPrpt.getDescription());
			stmt.setString(5, tagPrpt.getPropertyType());
			stmt.setInt(6, tagPrpt.getCardinality());
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

	public List<TagProperty> findTagProperty() {
		logger.debug("listTagProperty()");
		Connection conn = null;
		PreparedStatement stmt = null;
		List<TagProperty> tagPrpts = new ArrayList<TagProperty>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from listAllTagProperty()");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TagProperty tp = createTagPropertyObject(rs);
				logger.debug("found tagPrpt=" + tp);
				tagPrpts.add(tp);
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
		return tagPrpts;
	}

	public TagProperty getTagProperty(String nsPrefix, String name) {
		logger.debug("getTagProperty: nsPrefix=" + nsPrefix + ",name=" + name);
		Connection conn = null;
		PreparedStatement stmt = null;
		TagProperty tagPrpt = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from getTagPropertyNSPrefixName(?,?)");
			stmt.setString(1, nsPrefix);
			stmt.setString(2, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				TagProperty tp = createTagPropertyObject(rs);
				logger.debug("found tagPrpt=" + tp);
				tagPrpt = tp;
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
		return tagPrpt;
	}

	public List<TagProperty> findTagProperty(String propertyType) {
		logger.debug("findTagProperty: propertyType=" + propertyType);
		Connection conn = null;
		PreparedStatement stmt = null;
		List<TagProperty> tagPrpts = new ArrayList<TagProperty>();
		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement("select * from listTagPropertyOfType(?)");
			stmt.setString(1, propertyType);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TagProperty tp = createTagPropertyObject(rs);
				logger.debug("found tagPrpt=" + tp);
				tagPrpts.add(tp);
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
		return tagPrpts;
	}


}
