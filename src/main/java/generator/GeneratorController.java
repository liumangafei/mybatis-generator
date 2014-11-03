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

    private GenTable genTable = null;
    private String basePath = null;

    public GeneratorController(){
//        basePath = GeneratorController.class.getResource("/").toString();
//        basePath = new File("").getCanonicalPath();
        basePath = System.getProperty("user.dir") + "\\" + ConfigPropertiesUtil.getProperty("baseForder"); //初始化生成器的基础路径
        initGenTable(); //初始化genTable对象
    }

    public GeneratorController(GenTable genTable){
        this.genTable = genTable;
    }

    /**
     * 初始化数据库表对应的对象
     */
    private void initGenTable(){
        genTable = new GenTable();
        genTable.setTableName(ConfigPropertiesUtil.getProperty("tableName"));
        genTable.setClassName(StringUtil.toUpperCaseFristOne(StringUtil.toCamelCase(genTable.getTableName())));
        genTable.setGenPropertyList(getPropertyList(genTable.getTableName()));
        genTable.setModelPackage(ConfigPropertiesUtil.getProperty("modelPackage"));
        genTable.setMapperPackage(ConfigPropertiesUtil.getProperty("mapperPackage"));
        genTable.setMapperXmlPackage(ConfigPropertiesUtil.getProperty("mapperXmlPackage"));
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

        //如果数据库字段或者主键为空，则说明之前的查询已经出错，没有继续下去的必要
        if(fieldMap == null || fieldMap.size() == 0){
            throw new RuntimeException("数据库获取字段失败！");
        }

        if(primaryKeyList == null || primaryKeyList.size() == 0){
            throw new RuntimeException("数据库获取主键失败！");
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
     * 获取最终要生成的model文件名称，包括后缀
     *
     * @return
     */
    public String getClassFileName(){
        return genTable.getClassName() + ".java";
    }

    /**
     * 获取最终要生成的mapper文件名称，包括后缀
     *
     * @return
     */
    public String getMapperFileName(){
        return StringUtil.toLowerCaseFristOne(genTable.getClassName()) + "Mapper.java";
    }

    /**
     * 获取最终要生成的mapperXml文件名称，包括后缀
     *
     * @return
     */
    public String getMapperXmlFileName(){
        return StringUtil.toLowerCaseFristOne(genTable.getClassName()) + "-Mapper.xml";
    }

    /**
     * 获取最终要生成的model文件的路径
     *
     * @return
     */
    public String getModelPath(){
        return basePath + "\\" + genTable.getModelPackage().replace(".", "\\") +"\\" + getClassFileName();
    }

    /**
     * 获取最终要生成的mapper文件的路径
     *
     * @return
     */
    public String getMapperPath(){
        return basePath + "\\" + genTable.getMapperXmlPackage().replace(".", "\\") +"\\" + getMapperFileName();
    }

    /**
     * 获取最终要生成的mapperXml文件的路径
     *
     * @return
     */
    public String getMapperXmlPath(){
        return basePath + "\\" + genTable.getMapperXmlPackage().replace(".", "\\") +"\\" + getMapperXmlFileName();
    }

    /**
     * model类生成
     *
     * @return
     */
    public Generator createModelGenerator(){
        return new ModelGenerator(genTable);
    }

    /**
     * mapperXML文件生成
     *
     * @return
     */
    public Generator createMapperGenerator(){
        return new MapperGenerator(genTable);
    }

    /**
     * mapperXML文件生成
     *
     * @return
     */
    public Generator createMapperXMLGenerator(){
        return new MapperXMLGenerator(genTable);
    }


    public GenTable getGenTable() {
        return genTable;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setGenTable(GenTable genTable) {
        this.genTable = genTable;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }



    public void generate(){
        System.out.println("生成器执行开始...");

        //创建需要的生成器
        Generator modelGenerator = createModelGenerator(); // model生成器
        Generator mapperGenerator = createMapperGenerator(); //mapper生成器
        Generator mapperXMLGenerator = createMapperXMLGenerator(); //mapperXml生成器

        try {
            //执行生成文件
            modelGenerator.generateFile(getModelPath());
            mapperGenerator.generateFile(getMapperPath());
            mapperXMLGenerator.generateFile(getMapperXmlPath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        System.out.println("生成器执行结束...");
    }


    public static void main(String[] args) throws Exception{

        new GeneratorController().generate(); //生成器控制类

    }

}
