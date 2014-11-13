package generator;

import db.DBConnector;
import freemarker.template.TemplateException;
import model.DBType;
import model.GenTable;
import util.StringUtil;
import model.GenProperty;
import properties.ConfigPropertiesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description: 生成器的主要生成类，运行该类可以生成对应的model、xml文件
 */
public class GeneratorController {

    private String basePath = null;
    private List<GenTable> genTableList = null;

    public GeneratorController(){
//        basePath = GeneratorController.class.getResource("/").toString();
//        basePath = new File("").getCanonicalPath();
        this.basePath = System.getProperty("user.dir") + "\\" + ConfigPropertiesUtil.getProperty("baseForder"); //初始化生成器的基础路径
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

        GenTable genTable = null;
        String tableNames = ConfigPropertiesUtil.getProperty("tableNames");

        if(tableNames != null && !"".equals(tableNames)){
            for(String tableName : tableNames.split(",")){
                genTable = new GenTable();

                genTable.setTableName(tableName);
                genTable.setClassName(StringUtil.toUpperCaseFristOne(StringUtil.toCamelCase(tableName)));
                genTable.setGenPropertyList(getPropertyList(tableName));
                genTable.setModelPackage(ConfigPropertiesUtil.getProperty("modelPackage"));
                genTable.setMapperPackage(ConfigPropertiesUtil.getProperty("mapperPackage"));
                genTable.setMapperXmlPackage(ConfigPropertiesUtil.getProperty("mapperXmlPackage"));
                genTable.setModelPath(getModelPath(basePath, genTable.getModelPackage(), genTable.getClassName()));
                genTable.setMapperPath(getMapperPath(basePath, genTable.getMapperPackage(), genTable.getClassName()));
                genTable.setMapperXmlPath(getMapperXmlPath(basePath, genTable.getMapperXmlPackage(), genTable.getClassName()));

                genTableList.add(genTable);
            }
        }

        return genTableList;
    }

    /**
     * 获取table字段属性列表
     *
     * @param tableName
     * @return
     */
    private List<GenProperty> getPropertyList(String tableName){

        Map<String, String> fieldMap = new DBConnector().queryField(tableName); // 获取字段和字段对应类型
        List<String> primaryKeyList = new DBConnector().queryPrimarykey(tableName); // 获取主键
        Map<String, String> remarksMap = new DBConnector().queryRemarks(tableName); // 获取注释内容

        //如果数据库字段或者主键为空，则说明之前的查询已经出错，没有继续下去的必要
        if(fieldMap == null || fieldMap.size() == 0){
            throw new RuntimeException(tableName + "该表获取字段失败！");
        }

        if(primaryKeyList == null || primaryKeyList.size() == 0){
            throw new RuntimeException(tableName + "该表获取主键失败！");
        }

        //把所有字段封装到genPropertyList中
        if(fieldMap != null && fieldMap.size() > 0 ){
            List<GenProperty> genPropertyList = new ArrayList<GenProperty>();
            GenProperty genProperty = null;
            String upperFirstCamelStr = null;
            String propertyType = null;

            for(String key : fieldMap.keySet()){
                upperFirstCamelStr = StringUtil.toUpperCaseFristOne(StringUtil.toCamelCase(key));
                propertyType = fieldMap.get(key);

                genProperty = new GenProperty();

                //设置具体的属性到genProperty中
                genProperty.setIsPrimaryKey(isPrimaryKey(key, primaryKeyList));
                genProperty.setPropertyName(StringUtil.toCamelCase(key));
                genProperty.setPropertyType(convertDbType2JavaType(propertyType)); // 根据数据库类型获取JAVA字段类型
                genProperty.setTablePropertyName(key);
                genProperty.setTablePropertyType(propertyType);
                genProperty.setTablePropertyRemarmk(getRemark(key, remarksMap));
                genProperty.setPropertyNameSetStr("set" + upperFirstCamelStr);
                genProperty.setPropertyNameGetStr("get" + upperFirstCamelStr);

                genPropertyList.add(genProperty);
            }

            return genPropertyList;
        }

        return null;
    }

    /**
     * 判断该字段是否是主键
     *
     * @param field 字段名称
     * @param primaryKeyList 主键列表
     * @return
     */
    private boolean isPrimaryKey(String field, List<String> primaryKeyList){
        for(String primaryKey : primaryKeyList){ //设置property是否是主键
            if(primaryKey.equals(field)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取field在表中对应的注释信息
     *
     * @param field
     * @param remarksMap
     * @return
     */
    private String getRemark(String field, Map<String, String> remarksMap){
        for(String key : remarksMap.keySet()){
            if(key.equals(field)){
                return remarksMap.get(key);
            }
        }

        return "";
    }

    /**
     * 根据数据库字段类型转换成java类型
     *
     * @param dbType
     * @return
     */
    private String convertDbType2JavaType(String dbType){

        String propertyType = null;

        //对特殊类型进行处理 如：无符号的类型
        propertyType = DBType.getJavaType(dbType.split(" ").length > 1 ? dbType.split(" ")[0] : dbType);

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
        } catch (Exception e) {
            new RuntimeException("创建" + genTable.getModelPath() + "文件时出错！");
            e.printStackTrace();
        } finally {
            System.out.println(genTable.getModelPath() + "文件创建完毕！");
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
        } catch (Exception e) {
            new RuntimeException("创建" + genTable.getMapperPath() + "文件时出错！");
            e.printStackTrace();
        } finally {
            System.out.println(genTable.getMapperPath() + "文件创建完毕！");
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
        } catch (Exception e) {
            new RuntimeException("创建" + genTable.getMapperXmlPath() + "文件时出错！");
            e.printStackTrace();
        } finally {
            System.out.println(genTable.getMapperXmlPath() + "文件创建完毕！");
        }
    }

    /**
     * 生成genTable对应的相关文件
     *
     * @param genTable
     */
    public void generate(GenTable genTable){
        generateModelFile(genTable);
        generateMapperFile(genTable);
        generateMapperXmlFile(genTable);
    }

    /**
     * 生成所有的文件
     */
    public void generateAll(){
        System.out.println("生成器执行开始...");

        if(genTableList != null && genTableList.size() > 0){
            for(GenTable genTable : genTableList){
                generate(genTable);
            }
        }else{
            System.out.println("没有发现要生成表的相关信息！");
        }

        System.out.println("生成器执行结束...");
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

    }

}
