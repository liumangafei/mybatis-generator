package freemarker;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
            logger.error("FreeMarker Templates File Path Error!");
            e.printStackTrace();
        }
        return null;
    }

}
