package freemarker;

import freemarker.cache.MruCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Author: liumangafei
 * Date: 2014/10/25
 * Project Name: generator
 * Description:
 */
public class FMConfiguration {

    private static Logger logger = LoggerFactory.getLogger(FMConfiguration.class);

    private static final String TEMPLATES_DIRECTORY = "templates"; //模板基础文件夹
    private static final int MAX_STRONG_APPOINT_NUM = 20; //最大的强引用对象数
    private static final int MAX_WEAK_APPOINT_NUM = 250; //最大的弱引用对象数
//    private static final int CACHE_CYCLE = 5; //缓存周期，默认为5s

    private static Configuration cfg = null;

    public static Configuration getInstance() {
        if (cfg == null) {
            synchronized (FMConfiguration.class){
                if (cfg == null) {
                    try {
                        String templatesPath = FMTemplateFactory.class.getClassLoader().getResource(TEMPLATES_DIRECTORY).getPath();

                        cfg = new Configuration();
                        cfg.setDirectoryForTemplateLoading(new File(templatesPath));
                        cfg.setDefaultEncoding("UTF-8");
                        cfg.setObjectWrapper(new DefaultObjectWrapper());
                        cfg.setCacheStorage(new MruCacheStorage(MAX_STRONG_APPOINT_NUM, MAX_WEAK_APPOINT_NUM)); //最近最多用策略缓存(第一个参数是最大的强引用对象数，第二个为最大的弱引用对象数)
                        //cfg.setSetting(Configuration.CACHE_STORAGE_KEY, "strong: 20, soft: 250"); //另一种写法，因默认用的MruCacheStorage，所以能在配置文件中这么写
                        //cfg.setTemplateUpdateDelay(CACHE_CYCLE);
                    } catch (IOException e) {
                        logger.info("FreeMarker Init Configuration Error!");
                        e.printStackTrace();
                    }
                }
            }
        }

        return cfg;
    }

}
