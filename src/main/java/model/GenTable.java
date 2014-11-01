package model;

import java.util.List;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 该类包含表的相关信息、对应的类的相关信息、表中属性的相关信息
 */
public class GenTable {

    private String tableName;
    private String className;
    private List<GenProperty> genPropertyList;
    private String modelPackage;
    private String mapperPackage;
    private String mapperXmlPackage;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<GenProperty> getGenPropertyList() {
        return genPropertyList;
    }

    public void setGenPropertyList(List<GenProperty> genPropertyList) {
        this.genPropertyList = genPropertyList;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public String getMapperXmlPackage() {
        return mapperXmlPackage;
    }

    public void setMapperXmlPackage(String mapperXmlPackage) {
        this.mapperXmlPackage = mapperXmlPackage;
    }
}
