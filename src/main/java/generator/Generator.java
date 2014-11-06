package generator;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Writer;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description:
 */
public interface Generator {

    public void generateFile() throws IOException, TemplateException;

    public void generateFile(Writer out) throws IOException, TemplateException;

    public void generateFile(String filePath) throws IOException, TemplateException;

}
