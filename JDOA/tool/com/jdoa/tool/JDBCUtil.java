package com.jdoa.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.sun.rowset.CachedRowSetImpl;

public class JDBCUtil {

	private SqlMapClient sqlMap;

	/**
	 * @author Action
	 * @return conn
	 * @throws SQLException
	 * @describe 获取数据库连接
	 * @date 2014-12-24
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = sqlMap.getDataSource().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 执行无返回结果sql语句
	 * 
	 * @author Action
	 * @param sql
	 * @throws SQLException
	 * @date 2017-04-03
	 */
	public void execute(String sql) {
		Connection conn = null;
		PreparedStatement pstmt=null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行查询语句
	 * 
	 * @author Action
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @date 2017-04-03
	 */
	public ResultSet executeQuery(String sql) {
		Connection conn = null;
		CachedRowSetImpl crs = null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		try {
			conn = getConnection();
		    pstmt = conn.prepareStatement(sql);
		    rs = pstmt.executeQuery();
			crs = new CachedRowSetImpl();
			crs.populate(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				rs.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return crs;
	}

	/**
	 * 执行update sql语句
	 * 
	 * @author Action
	 * @param sql
	 * @throws SQLException
	 * @date 2017-04-03
	 */
	public int executeUpdateSql(String sql) {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt=null;
		try {
			conn = getConnection();
		    pstmt = conn.prepareStatement(sql);
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	public SqlMapClient getSqlMap() {
		return sqlMap;
	}

	public void setSqlMap(SqlMapClient sqlMap) {
		this.sqlMap = sqlMap;
	}

	/**
	 * 执行批处理语句
	 * 
	 * @author Action
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @date 2017-04-03
	 */
	public void executeBatch(String[] sqls) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (int i = 0; i < sqls.length; i++) {
				String sql = sqls[i];
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 执行批处理语句
	 * 
	 * @author Action
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @date 2017-04-03
	 */
	public void executeBatch(List<String> sqlList) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (int i = 0; i < sqlList.size(); i++) {
				String sql = sqlList.get(i);
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
