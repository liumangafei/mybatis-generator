package generator;

import freemarker.FMTemplateFactory;
import model.GenTables;
import org.apache.commons.io.FileUtils;
import util.StringUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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
public class MapperXMLGenerator implements Generator {

//    private static Logger logger = LoggerFactory.getLogger(MapperXMLGenerator.class);

    private GenTables genTables = null;

    public MapperXMLGenerator(GenTables genTables){
        this.genTables = genTables;
    }

    private String getModelResultMap(){
        return StringUtil.toLowerCaseFristOne(genTables.getClassName()) + "ResultMap";
    }

    private String getModelPath(){
        return genTables.getModelPackage() + "." + genTables.getClassName();
    }

    @Override
    public void generateFile(Writer out) throws IOException, TemplateException {
        Template temp = FMTemplateFactory.getTemplate("mapperXml.ftl");

        Map root = new HashMap();
        root.put("tableName", genTables.getTableName());
        root.put("mapperClassName", genTables.getMapperClassName());
        root.put("propertyList", genTables.getGenColumnsList());
        root.put("mapperPackage", genTables.getMapperPackage());
        root.put("modelResultMap", getModelResultMap());
        root.put("modelPath", getModelPath());

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
        generateFile(genTables.getMapperXmlPath());
    }

    public GenTables getGenTables() {
        return genTables;
    }

    public void setGenTables(GenTables genTables) {
        this.genTables = genTables;
    }
}
