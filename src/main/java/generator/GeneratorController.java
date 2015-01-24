package generator;

import db.DBConnector;
import model.*;
import org.apache.log4j.Logger;
import util.StringUtil;
import properties.ConfigPropertiesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 生成器的主要生成类，运行该类可以生成对应的model、xml文件
 */
public class GeneratorController {

    protected Logger logger = Logger.getLogger(this.getClass().getName());

    private String basePath = null;
    private List<GenTable> genTableList = null;

    public GeneratorController(){
//        basePath = GeneratorController.class.getResource("/").toString();
//        basePath = new File("").getCanonicalPath();
        String baseForder = ConfigPropertiesUtil.getProperty("baseForder");
        if(baseForder == null || "".equals(baseForder.trim())){
            baseForder = "generatedFile";
        }
        this.basePath = System.getProperty("user.dir") + "\\" + baseForder; //初始化生成器的基础路径
        this.genTableList = initGenTableList();
    }

    public GeneratorController(String basePath){
        this.basePath = basePath;
        this.genTableList = initGenTableList();
    }

    public GeneratorController(String basePath, List<GenTable> genTableList){
        this.basePath = basePath;
        this.genTableList = genTableList;
    }

    /**
     * 初始化数据库表对应的对象
     */
    private List<GenTable> initGenTableList(){

        List<GenTable> genTableList = new ArrayList<GenTable>();

        //获取数据库表名列表GenTable对象列表
        List<String> tableNameList = getTableNameList();

        logger.info("---------------开始初始化数据库表：---------------");
        //根据数据库表名列表获取
        if(tableNameList != null && tableNameList.size() > 0){
            for(String tableName : tableNameList){
                logger.info("正在初始化表："+tableName);
                genTableList.add(getGenTableByTableName(tableName));
            }
        }
        logger.info("---------------初始化数据库表已结束---------------");

        return genTableList;
    }

    /**
     * 获取数据库中所有的表对象
     *
     * @return
     */
    public List<Tables> getAllTablesList(){
        return new DBConnector().queryTables();
    }

    /**
     * 获取表名列表
     *
     * @return
     */
    public List<String> getTableNameList(){
        List<String> tableNameList = new ArrayList<String>();
        String tableNames = ConfigPropertiesUtil.getProperty("tableNames");

        if(tableNames == null || "".equals(tableNames.trim())) {
            // 把获取数据库表对象列表，然后获取表名填充进tableNameList中
            List<Tables> tablesLit = getAllTablesList();
            if(tablesLit != null) {
                for (Tables tables : tablesLit) {
                    tableNameList.add(tables.getTableName());
                }
            }
        }else{
            for(String tableName : tableNames.trim().split(",")){
                tableNameList.add(tableName);
            }
        }

        return tableNameList;
    }

    /**
     * 获取拥有主键对象列表
     *
     * @return
     */
    public List<Columns> getPrimaryKeyList(List<Columns> columnsList){
        List<Columns> primaryKeyColumnsList = new ArrayList<Columns>();
        for(Columns columns : columnsList){
            if(isPrimaryKey(columns)){
                primaryKeyColumnsList.add(columns);
            }
        }

        return primaryKeyColumnsList;
    }

    /**
     * 获取table字段属性列表
     *
     * @param tableName
     * @return
     */
    private List<GenProperty> getPropertyList(String tableName){

        List<Columns> columnsList = new DBConnector().queryColumnsByTableName(tableName);
        List<Columns> primaryKeyColumnsList = getPrimaryKeyList(columnsList);

        //如果数据库字段或者主键为空，则说明之前的查询已经出错，没有继续下去的必要
        if(columnsList == null || columnsList.size() == 0){
            throw new RuntimeException(tableName + "该表获取字段失败！");
        }

        //获取主键列表，如果没有主键则会报错
        if(primaryKeyColumnsList == null || primaryKeyColumnsList.size() == 0){
            throw new RuntimeException(tableName + "该表获取主键失败！");
        }

        return columnsList2GenPropertyList(columnsList);

    }

    /**
     * Columns对象转换成GenProperty对象
     *
     * @param columns
     * @return
     */
    private GenProperty columns2GenProperty(Columns columns){
        String columnName = columns.getColumnName();
        String columnNameCamelCaseStr = StringUtil.toCamelCase(columnName);
        String upperFirstCamelStr = StringUtil.toUpperCaseFristOne(columnNameCamelCaseStr);
        String propertyType = columns.getDataType();
        String propertyJavaType = convertDbType2JavaType(propertyType);

        GenProperty genProperty = new GenProperty();
        genProperty.setIsPrimaryKey(isPrimaryKey(columns));
        genProperty.setPropertyName(columnNameCamelCaseStr);
        genProperty.setPropertyType(propertyJavaType); // 根据数据库类型获取JAVA字段类型
        genProperty.setTablePropertyName(columnName);
        genProperty.setTablePropertyType(propertyType);
        genProperty.setTablePropertyRemarmk(columns.getColumnComment());
        genProperty.setPropertyNameSetStr("set" + upperFirstCamelStr);
        genProperty.setPropertyNameGetStr("get" + upperFirstCamelStr);

        return genProperty;
    }

    /**
     * Columns对象列表转换成GenProperty对象列表
     *
     * @param columnsList
     * @return
     */
    private List<GenProperty> columnsList2GenPropertyList(List<Columns> columnsList){
        List<GenProperty> genPropertyList = new ArrayList<GenProperty>();

        for(Columns columns : columnsList){
            genPropertyList.add(columns2GenProperty(columns));
        }

        return genPropertyList;
    }

    /**
     * 根据数据库表名获取GenTable对象
     *
     * @param tableName
     * @return
     */
    private GenTable getGenTableByTableName(String tableName){
        GenTable genTable = new GenTable();

        genTable.setTableName(tableName);
        genTable.setClassName(StringUtil.toUpperCaseFristOne(StringUtil.toCamelCase(tableName)));
        genTable.setGenPropertyList(getPropertyList(tableName));
        genTable.setModelPackage(ConfigPropertiesUtil.getProperty("modelPackage"));
        genTable.setMapperPackage(ConfigPropertiesUtil.getProperty("mapperPackage"));
        genTable.setMapperXmlPackage(ConfigPropertiesUtil.getProperty("mapperXmlPackage"));
        genTable.setModelPath(getModelPath(basePath, genTable.getModelPackage(), genTable.getClassName()));
        genTable.setMapperPath(getMapperPath(basePath, genTable.getMapperPackage(), genTable.getClassName()));
        genTable.setMapperXmlPath(getMapperXmlPath(basePath, genTable.getMapperXmlPackage(), genTable.getClassName()));
        return genTable;
    }

    /**
     * 判断该字段是否是主键
     *
     * @return
     */
    private boolean isPrimaryKey(Columns columns){
        if(columns.getColumnKey() != null && "PRI".equals(columns.getColumnKey())){
            return true;
        }
        return false;
    }

    /**
     * 根据数据库字段类型转换成java类型
     *
     * @param dbType
     * @return
     */
    private String convertDbType2JavaType(String dbType){

        String propertyType = null;
        dbType = dbType.toUpperCase();

        //对特殊类型进行处理 如：无符号的类型
        propertyType = DBType.getJavaType(dbType);

        if(propertyType == null){
            throw new RuntimeException(dbType + "类型匹配不正确！");
        }

        return propertyType;
    }

    /**
     * 获取最终要生成的model文件的路径
     *
     * @return
     */
    public String getModelPath(String basePath, String modelPackage, String className){
        return basePath + "\\" + modelPackage.replace(".", "\\") +"\\" + className + ".java";
    }

    /**
     * 获取最终要生成的mapper文件的路径
     *
     * @return
     */
    public String getMapperPath(String basePath, String mapperPackage, String className){
        return basePath + "\\" + mapperPackage.replace(".", "\\") +"\\" + className + "Mapper.java";
    }

    /**
     * 获取最终要生成的mapperXml文件的路径
     *
     * @return
     */
    public String getMapperXmlPath(String basePath, String mapperXmlPackage, String className){
        return basePath + "\\" + mapperXmlPackage.replace(".", "\\") +"\\" + StringUtil.toLowerCaseFristOne(className) + "-mapper.xml";
    }

    /**
     * 生成一个model文件
     *
     * @param genTable
     */
    public void generateModelFile(GenTable genTable) {
        try {
            //创建model文件
            Generator generator = new ModelGenerator(genTable); // model生成器
            generator.generateFile();
            logger.info(genTable.getModelPath() + "文件创建完毕！");
        } catch (Exception e) {
            new RuntimeException("创建" + genTable.getModelPath() + "文件时出错！");
            e.printStackTrace();
        }
    }

    /**
     * 生成一个mapper文件
     *
     * @param genTable
     */
    public void generateMapperFile(GenTable genTable) {
        try {
            Generator generator = new MapperGenerator(genTable); // mapper生成器
            generator.generateFile(); // 创建mapper文件
            logger.info(genTable.getMapperPath() + "文件创建完毕！");
        } catch (Exception e) {
            new RuntimeException("创建" + genTable.getMapperPath() + "文件时出错！");
            e.printStackTrace();
        }
    }

    /**
     * 生成一个mapperXml文件
     *
     * @param genTable
     */
    public void generateMapperXmlFile(GenTable genTable) {
        try {
            Generator generator = new MapperXMLGenerator(genTable); // mapperXml生成器
            generator.generateFile(); // 创建mapperXml文件
            logger.info(genTable.getMapperXmlPath() + "文件创建完毕！");
        } catch (Exception e) {
            new RuntimeException("创建" + genTable.getMapperXmlPath() + "文件时出错！");
            e.printStackTrace();
        }
    }

    /**
     * 生成所有的文件
     */
    public void generateAll(){
        logger.info("===============生成器开始执行===============");

        if(genTableList != null && genTableList.size() > 0){
            for(GenTable genTable : genTableList){
                generateModelFile(genTable);
                generateMapperFile(genTable);
                generateMapperXmlFile(genTable);
            }
        }else{
            logger.info("没有发现要生成表的相关信息！");
        }

        logger.info("===============生成器执行结束===============");
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<GenTable> getGenTableList() {
        return genTableList;
    }

    public void setGenTableList(List<GenTable> genTableList) {
        this.genTableList = genTableList;
    }




    public static void main(String[] args) throws Exception{

        //生成所有的表，对应的所有文件
        new GeneratorController().generateAll();
//        new GeneratorController("D:\\generatedFile").generateAll();

    }

}
