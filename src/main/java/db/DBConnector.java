package db;

import model.Columns;
import model.Tables;
import org.apache.log4j.Logger;
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
	 * 查询数据库中所有表信息
	 *
	 * @return
	 */
	public List<Tables> queryAllTables() throws ClassNotFoundException, SQLException{
		String sql = "select * from information_schema.tables where table_schema=DATABASE()";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Tables> resultList = new ArrayList<Tables>();

		try {
			conn = DBConnector.startConnect();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);
			Tables tables = null;

			while(rs.next()){
				tables = new Tables();
				tables.setTableCatalog(rs.getString("TABLE_CATALOG"));
				tables.setTableSchema(rs.getString("TABLE_SCHEMA"));
				tables.setTableName(rs.getString("TABLE_NAME"));
				tables.setTableType(rs.getString("TABLE_TYPE"));
				tables.setEngine(rs.getString("ENGINE"));
				tables.setVersion(rs.getLong("VERSION"));
				tables.setRowFormat(rs.getString("ROW_FORMAT"));
				tables.setTableRows(rs.getLong("TABLE_ROWS"));
				tables.setAvgRowLength(rs.getLong("AVG_ROW_LENGTH"));
				tables.setDataLength(rs.getLong("DATA_LENGTH"));
				tables.setMaxDataLength(rs.getLong("MAX_DATA_LENGTH"));
				tables.setIndexLength(rs.getLong("INDEX_LENGTH"));
				tables.setDataFree(rs.getLong("DATA_FREE"));
				tables.setAutoIncrement(rs.getLong("AUTO_INCREMENT"));
				tables.setCreateTime(rs.getTimestamp("CREATE_TIME"));
				tables.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
				tables.setCheckTime(rs.getTimestamp("CHECK_TIME"));
				tables.setTableCollation(rs.getString("TABLE_COLLATION"));
				tables.setChecksum(rs.getLong("CHECKSUM"));
				tables.setCreateOptions(rs.getString("CREATE_OPTIONS"));
				tables.setTableComment(rs.getString("TABLE_COMMENT"));

				resultList.add(tables);
			}

		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBConnector.disconnect(conn, stmt, rs);
		}

		return resultList;
	}

	/**
	 * 查询数据库中表名称为tableName的信息
	 *
	 * @return
	 */
	public Tables queryTables(String tableName) throws ClassNotFoundException, SQLException{
		String sql = "select * from information_schema.tables where table_schema=DATABASE() and table_name = '"+ tableName +"'";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Tables tables = null;

		try {
			conn = DBConnector.startConnect();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);

			if(rs.next()){
				tables = new Tables();
				tables.setTableCatalog(rs.getString("TABLE_CATALOG"));
				tables.setTableSchema(rs.getString("TABLE_SCHEMA"));
				tables.setTableName(rs.getString("TABLE_NAME"));
				tables.setTableType(rs.getString("TABLE_TYPE"));
				tables.setEngine(rs.getString("ENGINE"));
				tables.setVersion(rs.getLong("VERSION"));
				tables.setRowFormat(rs.getString("ROW_FORMAT"));
				tables.setTableRows(rs.getLong("TABLE_ROWS"));
				tables.setAvgRowLength(rs.getLong("AVG_ROW_LENGTH"));
				tables.setDataLength(rs.getLong("DATA_LENGTH"));
				tables.setMaxDataLength(rs.getLong("MAX_DATA_LENGTH"));
				tables.setIndexLength(rs.getLong("INDEX_LENGTH"));
				tables.setDataFree(rs.getLong("DATA_FREE"));
				tables.setAutoIncrement(rs.getLong("AUTO_INCREMENT"));
				tables.setCreateTime(rs.getTimestamp("CREATE_TIME"));
				tables.setUpdateTime(rs.getTimestamp("UPDATE_TIME"));
				tables.setCheckTime(rs.getTimestamp("CHECK_TIME"));
				tables.setTableCollation(rs.getString("TABLE_COLLATION"));
				tables.setChecksum(rs.getLong("CHECKSUM"));
				tables.setCreateOptions(rs.getString("CREATE_OPTIONS"));
				tables.setTableComment(rs.getString("TABLE_COMMENT"));
			}

		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBConnector.disconnect(conn, stmt, rs);
		}

		return tables;
	}

	/**
	 * 根据表明查询表内所有字段的信息
	 *
	 * @param tableName
	 * @return
	 */
	public List<Columns> queryColumnsByTableName(String tableName) throws ClassNotFoundException, SQLException{
		String sql = "select * from information_schema.columns where table_schema=DATABASE() and table_name='"+ tableName +"'";

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Columns> resultList = new ArrayList<Columns>();

		try {
			conn = DBConnector.startConnect();
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);
			Columns columns = null;
			while(rs.next()){
				columns = new Columns();
				columns.setTableCatalog(rs.getString("TABLE_CATALOG"));
				columns.setTableSchema(rs.getString("TABLE_SCHEMA"));
				columns.setTableName(rs.getString("TABLE_NAME"));
				columns.setColumnName(rs.getString("COLUMN_NAME"));
				columns.setOrdinalPosition(rs.getLong("ORDINAL_POSITION"));
				columns.setColumnDefault(rs.getString("COLUMN_DEFAULT"));
				columns.setIsNullable(rs.getString("IS_NULLABLE"));
				columns.setDataType(rs.getString("DATA_TYPE"));
				columns.setCharacterMaximumLength(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
				columns.setCharacterOctetLength(rs.getLong("CHARACTER_OCTET_LENGTH"));
				columns.setNumericPrecision(rs.getLong("NUMERIC_PRECISION"));
				columns.setNumericScale(rs.getLong("NUMERIC_SCALE"));
				columns.setDatetimePrecision(rs.getLong("DATETIME_PRECISION"));
				columns.setCharacterSetName(rs.getString("CHARACTER_SET_NAME"));
				columns.setCollationName(rs.getString("COLLATION_NAME"));
				columns.setColumnType(rs.getString("COLUMN_TYPE"));
				columns.setColumnKey(rs.getString("COLUMN_KEY"));
				columns.setExtra(rs.getString("EXTRA"));
				columns.setPrivileges(rs.getString("PRIVILEGES"));
				columns.setColumnComment(rs.getString("COLUMN_COMMENT"));

				resultList.add(columns);
			}

		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBConnector.disconnect(conn, stmt, rs);
		}

		return resultList;
	}

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
		System.out.println(new DBConnector().queryTables("t_admin"));
    }

}
