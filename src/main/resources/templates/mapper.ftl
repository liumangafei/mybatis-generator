package ${package};

import ${modelPath};
import org.springframework.stereotype.Repository;

/**
* Author: ${sysUsername}
* Date: ${.now?string("yyyy-MM-dd HH:mm:ss")}
* Description: ${tableName}表对应的Mapper类
*/
@Repository
public interface ${className}Mapper extends BaseMapper<${className}, ${primaryKeyType}> {

}