package properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description:
 */
public class ConfigPropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(ConfigPropertiesUtil.class);

	private static Properties config = null;
	
	static {
        if(config == null) {
            synchronized (ConfigPropertiesUtil.class) {
                if(config == null) {
                    loadConfigProperty();
                }
            }
        }
	}
	
	public static void loadConfigProperty() {
		try {
			InputStream is = ConfigPropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
            config = new Properties();
            config.clear();
            config.load(is);
		} catch (IOException e) {
            logger.info("加载config.properties文件出现错误！");
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return config.getProperty(key);
	}
}
