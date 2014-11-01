package freemarker;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class FMTemplateFactory {

    private static Logger logger = LoggerFactory.getLogger(FMTemplateFactory.class);

    public static Template getTemplate(String templateName) {
        try {
            return FMConfiguration.getInstance().getTemplate(templateName);
        } catch (IOException e) {
            logger.info("FreeMarker Templates File Path Error!");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        /* 获取或创建模板*/
        Template temp = FMTemplateFactory.getTemplate("model.ftl");
        /* 创建数据模型 */
        Map root = new HashMap();
        root.put("user", "Big Joe");
        Map latest = new HashMap();
        root.put("latestProduct", latest);
        latest.put("url", "products/greenmouse.html");
        latest.put("name", "green mouse");
        /* 将模板和数据模型合并 */
        Writer out = new OutputStreamWriter(System.out);
        temp.process(root, out);
        out.flush();

    }

}
