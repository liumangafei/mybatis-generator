package generator;

import db.DBConnector;
import model.*;
import org.slf4j.LoggerFactory;
import util.StringUtil;
import properties.ConfigPropertiesUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 生成器的主要生成类，运行该类可以生成对应的model、xml文件
 */
public class GeneratorController {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(GeneratorController.class);

    private String basePath = null;
    private List<GenTables> genTablesList = null;

    public GeneratorController(){
//        basePath = GeneratorController.class.getResource("/").toString();
//        basePath = new File("").getCanonicalPath();
        String baseForder = ConfigPropertiesUtil.getProperty("baseForder");
        if(baseForder == null || "".equals(baseForder.trim())){
            baseForder = "generatedFile";
        }
        this.basePath = System.getProperty("user.dir") + "\\" + baseForder; //初始化生成器的基础路径
        this.genTablesList = initGenTablesList();
    }

    public GeneratorController(String basePath){
        this.basePath = basePath;
        this.genTablesList = initGenTablesList();
    }

    public GeneratorController(String basePath, List<GenTables> genTablesList){
        this.basePath = basePath;
        this.genTablesList = genTablesList;
    }

    /**
     * 初始化数据库表对应的对象
     */
    private List<GenTables> initGenTablesList(){
        logger.info("---------------开始初始化数据库表：---------------");

        List<GenTables> genTablesList = new ArrayList<GenTables>();

        //获取数据库表名列表GenTables对象列表
        try {
            List<Tables> tablesList = getTablesList();

            //根据数据库表名列表获取
            if(tablesList != null && tablesList.size() > 0){
                for(Tables tables : tablesList){
                    logger.info("正在初始化表：" + tables.getTableName());
                    genTablesList.add(getGenTablesByTableName(tables));
                }
            }else{
                throw new RuntimeException("未找到需要初始化的数据库表！");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        logger.info("---------------初始化数据库表已结束---------------");

        return genTablesList;
    }

    /**
     * 获取表名列表
     *
     * @return
     */
    public List<Tables> getTablesList() throws Exception{
        List<Tables> tablesList = new ArrayList<Tables>();
        String tableNames = ConfigPropertiesUtil.getProperty("tableNames");

        try {
            if(tableNames == null || "".equals(tableNames.trim())) {
                // 把获取数据库所有表对象列表
                return new DBConnector().queryAllTables();
            }else{
                for(String tableName : tableNames.replaceAll(" ", "").split(",")){
                    tablesList.add(new DBConnector().queryTables(tableName));
                }
            }
        }catch (Exception e){
            throw e;
        }


        return tablesList;
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
    private List<GenColumns> getPropertyList(String tableName) throws ClassNotFoundException, SQLException {

        List<Columns> columnsList = new DBConnector().queryColumnsByTableName(tableName);
        List<Columns> primaryKeyColumnsList = getPrimaryKeyList(columnsList);

        //如果数据库字段或者主键为空，则说明之前的查询已经出错，没有继续下去的必要
        if(columnsList == null || columnsList.size() == 0){
            throw new SQLException(tableName + "表:获取字段失败！");
        }

        //获取主键列表，如果没有主键则会报错
        if(primaryKeyColumnsList == null || primaryKeyColumnsList.size() == 0){
            throw new SQLException(tableName + "表:获取主键失败！");
        }

        return columnsList2GenPropertyList(columnsList);

    }

    /**
     * Columns对象转换成GenProperty对象
     *
     * @param columns
     * @return
     */
    private GenColumns columns2GenProperty(Columns columns){
        String columnName = columns.getColumnName();
        String columnNameCamelCaseStr = StringUtil.toCamelCase(columnName);
        String upperFirstCamelStr = StringUtil.toUpperCaseFristOne(columnNameCamelCaseStr);
        String propertyType = columns.getDataType();
        String propertyJavaType = convertDbType2JavaType(propertyType);

        GenColumns genColumns = new GenColumns();
        genColumns.setIsPrimaryKey(isPrimaryKey(columns));
        genColumns.setPropertyName(columnNameCamelCaseStr);
        genColumns.setPropertyType(propertyJavaType); // 根据数据库类型获取JAVA字段类型
        genColumns.setTablePropertyName(columnName);
        genColumns.setTablePropertyType(propertyType);
        genColumns.setTablePropertyRemarmk(columns.getColumnComment());
        genColumns.setPropertyNameSetStr("set" + upperFirstCamelStr);
        genColumns.setPropertyNameGetStr("get" + upperFirstCamelStr);

        return genColumns;
    }

    /**
     * Columns对象列表转换成GenProperty对象列表
     *
     * @param columnsList
     * @return
     */
    private List<GenColumns> columnsList2GenPropertyList(List<Columns> columnsList){
        List<GenColumns> genColumnsList = new ArrayList<GenColumns>();

        for(Columns columns : columnsList){
            genColumnsList.add(columns2GenProperty(columns));
        }

        return genColumnsList;
    }

    /**
     * 根据数据库表名获取GenTables对象
     *
     * @param tables
     * @return
     */
    private GenTables getGenTablesByTableName(Tables tables) throws ClassNotFoundException, SQLException {
        String tableName = tables.getTableName();
        String className = StringUtil.toUpperCaseFristOne(StringUtil.toCamelCase(tableName));

        GenTables genTables = new GenTables();
        genTables.setTableName(tableName);
        genTables.setClassName(className);
        genTables.setTableComment(tables.getTableComment());
        genTables.setMapperClassName(genTables.getClassName() + "Mapper");
        genTables.setGenColumnsList(getPropertyList(tableName));
        genTables.setModelPackage(ConfigPropertiesUtil.getProperty("modelPackage"));
        genTables.setMapperPackage(ConfigPropertiesUtil.getProperty("mapperPackage"));
        genTables.setMapperXmlPackage(ConfigPropertiesUtil.getProperty("mapperXmlPackage"));
        genTables.setModelPath(getModelPath(basePath, genTables.getModelPackage(), genTables.getClassName()));
        genTables.setMapperPath(getMapperPath(basePath, genTables.getMapperPackage(), genTables.getClassName()));
        genTables.setMapperXmlPath(getMapperXmlPath(basePath, genTables.getMapperXmlPackage(), genTables.getClassName()));
        return genTables;
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
     * @param genTables
     */
    public void generateModelFile(GenTables genTables) throws Exception {
        try {
            //创建model文件
            Generator generator = new ModelGenerator(genTables); // model生成器
            generator.generateFile();
            logger.info(genTables.getModelPath() + "文件创建完毕！");
        } catch (Exception e) {
            throw new Exception("创建" + genTables.getModelPath() + "文件时出错！");
        }
    }

    /**
     * 生成一个mapper文件
     *
     * @param genTables
     */
    public void generateMapperFile(GenTables genTables) throws Exception {
        try {
            Generator generator = new MapperGenerator(genTables); // mapper生成器
            generator.generateFile(); // 创建mapper文件
            logger.info(genTables.getMapperPath() + "文件创建完毕！");
        } catch (Exception e) {
            throw new Exception("创建" + genTables.getMapperPath() + "文件时出错！");
        }
    }

    /**
     * 生成一个mapperXml文件
     *
     * @param genTables
     */
    public void generateMapperXmlFile(GenTables genTables) throws Exception {
        try {
            Generator generator = new MapperXMLGenerator(genTables); // mapperXml生成器
            generator.generateFile(); // 创建mapperXml文件
            logger.info(genTables.getMapperXmlPath() + "文件创建完毕！");
        } catch (Exception e) {
            throw new Exception("创建" + genTables.getMapperXmlPath() + "文件时出错！");
        }
    }

    /**
     * 生成所有的文件
     */
    public void generateAll(){
        logger.info("===============生成器开始执行===============");

        try {
            if(genTablesList != null && genTablesList.size() > 0){
                for(GenTables genTables : genTablesList){
                    generateModelFile(genTables);
                    generateMapperFile(genTables);
                    generateMapperXmlFile(genTables);
                }
            }else{
                logger.info("没有发现要生成表的相关信息！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        logger.info("===============生成器执行结束===============");
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<GenTables> getGenTablesList() {
        return genTablesList;
    }

    public void setGenTablesList(List<GenTables> genTablesList) {
        this.genTablesList = genTablesList;
    }




    public static void main(String[] args) throws Exception{

        //生成所有的表，对应的所有文件
        new GeneratorController().generateAll();
//        new GeneratorController("D:\\generatedFile").generateAll();

    }

}
