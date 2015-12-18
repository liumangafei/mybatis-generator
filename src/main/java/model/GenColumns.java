package model;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 该类为表中的属性信息及对应类的相关信息
 */
public class GenColumns {

    /**
     * 是否主键
     */
    private Boolean isPrimaryKey;
    /**
     * 属性类型
     */
    private String propertyType;
    /**
     * 属性类型
     */
    private String propertyName;
    /**
     * 数据库中属性名称
     */
    private String tablePropertyName;
    /**
     * 数据库中属性类型
     */
    private String tablePropertyType;
    /**
     * 数据库属性备注
     */
    private String tablePropertyRemarmk;
    /**
     * 属性的setter方法名称
     */
    private String propertyNameSetStr;
    /**
     * 属性的getter方法名称
     */
    private String propertyNameGetStr;

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getTablePropertyName() {
        return tablePropertyName;
    }

    public void setTablePropertyName(String tablePropertyName) {
        this.tablePropertyName = tablePropertyName;
    }

    public String getTablePropertyType() {
        return tablePropertyType;
    }

    public void setTablePropertyType(String tablePropertyType) {
        this.tablePropertyType = tablePropertyType;
    }

    public String getTablePropertyRemarmk() {
        return tablePropertyRemarmk;
    }

    public void setTablePropertyRemarmk(String tablePropertyRemarmk) {
        this.tablePropertyRemarmk = tablePropertyRemarmk;
    }

    public String getPropertyNameSetStr() {
        return propertyNameSetStr;
    }

    public void setPropertyNameSetStr(String propertyNameSetStr) {
        this.propertyNameSetStr = propertyNameSetStr;
    }

    public String getPropertyNameGetStr() {
        return propertyNameGetStr;
    }

    public void setPropertyNameGetStr(String propertyNameGetStr) {
        this.propertyNameGetStr = propertyNameGetStr;
    }
}