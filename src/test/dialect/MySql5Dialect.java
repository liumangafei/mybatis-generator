package demo.base.dialect;

public class MySql5Dialect extends Dialect {

	public String getLimitString(String querySqlString, int offset, int limit) {
		return querySqlString + " limit " + offset + " ," + limit;
	}

	public boolean supportsLimit() {
		return true;
	}
}
