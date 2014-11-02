package model;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 数据库字段类型与java类型之间的对应关系类
 */
public enum DBType {

    TINYINT("Integer"),
    SMALLINT("Integer"),
    MEDIUMINT("Integer"),
    INT("Integer"),
    BIGINT("Long"),
    BIT("Boolean"),
    DOUBLE("Double"),
    FLOAT("Float"),
    DECIMAL("BigDecimal"),
    CHAR("String"),
    VARCHAR("String"),
    DATE("Date"),
    TIME("Date"),
    YEAR("Date"),
    TIMESTAMP("Date"),
    DATETIME("Date"),
    TINYBLOB("byte[]"),
    BLOB("byte[]"),
    MEDIUMBLOB("byte[]"),
    LONGBLOB("byte[]"),
    BINARY("byte[]"),
    VARBINARY("byte[]"),
    GEOMETRY("Object");

    /**
     * 数据库类型对应的java类型名称
     */
    private String javaType;

    private DBType(String javaType){
        this.javaType = javaType;
    }

    /**
     * 根据数据库字段类型名称dbTypeName，获取到对应的java类型名称
     *
     * @param dbTypeName
     * @return
     */
    public static String getJavaType(String dbTypeName){
        for(DBType dbType : DBType.values()){
            if(dbType.name().equals(dbTypeName)){
                return dbType.javaType;
            }
        }

        return null;
    }

}
