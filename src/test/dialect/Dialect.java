package demo.base.dialect;

public abstract class Dialect {

	public static enum Type {
		MYSQL, ORACLE, SQLSERVER
	}

	public abstract String getLimitString(String querySqlString, int offset, int limit);

}
