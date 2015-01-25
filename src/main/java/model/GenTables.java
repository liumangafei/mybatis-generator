package model;

import java.util.List;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 该类包含表的相关信息、对应的类的相关信息、表中属性的相关信息
 */
public class GenTables {

    /** 数据库表名 */
    private String tableName;
    /** Model类名 */
    private String className;
    /** Mapper类名 */
    private String mapperClassName;
    /** 数据库表名注释 */
    private String tableComment;
    /** 表字段列表 */
    private List<GenColumns> genColumnsList;
    /** model类的package */
    private String modelPackage;
    /** mapper类的package */
    private String mapperPackage;
    /** mapperXml文件的package */
    private String mapperXmlPackage;
    /** model类path */
    private String modelPath;
    /** mapper类path */
    private String mapperPath;
    /** mapperXml文件path */
    private String mapperXmlPath;

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

    public String getMapperClassName() {
        return mapperClassName;
    }

    public void setMapperClassName(String mapperClassName) {
        this.mapperClassName = mapperClassName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public List<GenColumns> getGenColumnsList() {
        return genColumnsList;
    }

    public void setGenColumnsList(List<GenColumns> genColumnsList) {
        this.genColumnsList = genColumnsList;
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

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getMapperXmlPath() {
        return mapperXmlPath;
    }

    public void setMapperXmlPath(String mapperXmlPath) {
        this.mapperXmlPath = mapperXmlPath;
    }
}
