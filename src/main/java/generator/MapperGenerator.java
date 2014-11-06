package generator;

import freemarker.FMTemplateFactory;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import model.GenProperty;
import model.GenTable;
import org.apache.commons.io.FileUtils;
import util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description:
 */
public class MapperGenerator implements Generator {

//    private static Logger logger = LoggerFactory.getLogger(MapperGenerator.class);

    private GenTable genTable = null;

    public MapperGenerator(GenTable genTable){
        this.genTable = genTable;
    }

    private String getModelPath(){
        return genTable.getModelPackage() + "." + genTable.getClassName();
    }

    private String getPrimaryKeyType(){

        int primaryKeyCount = 0;
        GenProperty property = null;
        for(GenProperty genProperty : genTable.getGenPropertyList()){
            if(genProperty.getIsPrimaryKey() != null && genProperty.getIsPrimaryKey()){
                property = genProperty;
                primaryKeyCount++;
            }
        }

        if(primaryKeyCount > 1){
            return "Map";
        }

        return property.getPropertyType();
    }

    @Override
    public void generateFile(Writer out) throws IOException, TemplateException {
        Template temp = FMTemplateFactory.getTemplate("mapper.ftl");

        Map root = new HashMap();
        root.put("package", genTable.getMapperPackage());
        root.put("modelPath", getModelPath());
        root.put("className", genTable.getClassName());
        root.put("primaryKeyType", getPrimaryKeyType());

        temp.process(root, out);
        out.flush();
    }

    @Override
    public void generateFile(String filePath) throws IOException, TemplateException {
        Writer out = new OutputStreamWriter(FileUtils.openOutputStream(new File(filePath)));
        generateFile(out);
        out.flush();
    }

    @Override
    public void generateFile() throws IOException, TemplateException {
        generateFile(genTable.getMapperPath());
    }

    public GenTable getGenTable() {
        return genTable;
    }

    public void setGenTable(GenTable genTable) {
        this.genTable = genTable;
    }
}
