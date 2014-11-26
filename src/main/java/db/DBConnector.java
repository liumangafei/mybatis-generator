package db;

import properties.ConfigPropertiesUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 数据库连接类，几个方法为了获取到表和字段的相关信息
 */
public class DBConnector {

	private static String driverName = null;
	private static String sqlUrl = null;
	private static String sqlUser = null;
	private static String sqlPassword = null;

	static {
		if (driverName == null && sqlUrl == null && sqlUser == null && sqlPassword == null) {
            synchronized (DBConnector.class) {
                if (driverName == null || sqlUrl == null || sqlUser == null || sqlPassword == null) {
                    driverName = ConfigPropertiesUtil.getProperty("driverName");
                    sqlUrl = ConfigPropertiesUtil.getProperty("sqlUrl");
                    sqlUser = ConfigPropertiesUtil.getProperty("sqlUser");
                    sqlPassword = ConfigPropertiesUtil.getProperty("sqlPassword");
                }
            }
		}
	}

	public static Connection startConnect() throws ClassNotFoundException, SQLException {
		Class.forName(driverName);
		return DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);

	}

	public static void disconnect(Connection conn, Statement stmt, ResultSet rs) {
		
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据表名获取主键
     * @param tableName
     * @return
     */
	public List<String> queryPrimarykey(String tableName) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<String> result = new ArrayList<String>();

		try {
			con = DriverManager.getConnection(sqlUrl, sqlUser, sqlPassword);
			DatabaseMetaData dmd = con.getMetaData();

			rs = dmd.getPrimaryKeys(null, null, tableName);
			
			while (rs.next()) {
				result.add(rs.getString(4));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			DBConnector.disconnect(con, stmt, rs);
		}
		
		return result;
	}

    /**
     * 根据字段的注释
     * @param tableName
     * @return
     */
    public Map<String, String> queryRemarks(String tableName) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, String> resultMap = new LinkedHashMap<String, String>();

        try {
            conn = DBConnector.startConnect();
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            rs = databaseMetaData.getColumns(null, "%", tableName, "%");

            while (rs.next()) {
                resultMap.put(rs.getString("COLUMN_NAME"), rs.getString("REMARKS"));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnector.disconnect(conn, stmt, rs);
        }

        return resultMap;
    }
	
	/**
	 * 获取字段和字段对应类型
     *
	 * @param tableName
	 * @return
	 */
	public Map<String, String> queryField(String tableName) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> resultMap = new LinkedHashMap<String, String>();

		try {
			conn = DBConnector.startConnect();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("select * from " + tableName + " limit 0, 1");
			ResultSetMetaData data = rs.getMetaData();

			for (int i = 1; i <= data.getColumnCount(); i++) {
				resultMap.put(data.getColumnName(i), data.getColumnTypeName(i));
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.disconnect(conn, stmt, rs);
		}

		return resultMap;
	}

    public List<String> queryAllTableNames(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<String> resultList = new ArrayList<String>();

        try {
            conn = DBConnector.startConnect();
            rs = conn.getMetaData().getTables(null,null,null,null);

            while(rs.next())
            {
                resultList.add(rs.getString("TABLE_NAME"));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnector.disconnect(conn, stmt, rs);
        }

        return resultList;
    }

    public static void main(String[] args) {
        System.out.println(new DBConnector().queryAllTableNames());
    }

}
