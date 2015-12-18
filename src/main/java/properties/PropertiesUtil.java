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
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	private static Object propertiesLock = new Object();
	private static volatile Properties config = null;
	
	static {
        if(config == null) {
            synchronized (propertiesLock) {
                if(config == null) {
					loadPropertyByName("config.properties");
                }
            }
        }
	}
	
	public static void loadPropertyByName(String propertiesFileName) {
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFileName);
            config = new Properties();
            config.clear();
            config.load(is);
		} catch (IOException e) {
            logger.info("加载{}文件出现错误！", propertiesFileName);
			e.printStackTrace();
		}finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getProperty(String key) {
		return config.getProperty(key);
	}
}
