package model;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 该类为表中的属性信息及对应类的相关信息
 */
public class GenProperty {

    private Boolean isPrimaryKey;
    private String propertyType;
    private String propertyName;
    private String tablePropertyName;
    private String tablePropertyType;
    private String tablePropertyRemarmk;
    private String propertyNameSetStr;
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