package cc.dkcms.cms.service.install;

import com.alibaba.druid.pool.DruidDataSource;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.InstallParamVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class TaskInitConnection extends InstallTask {

    public static final Logger logger = LoggerFactory.getLogger(TaskInitConnection.class);

    @Override
    public String getSuccessMsg(Result result) {
        return "连接数据库成功";
    }

    @Override
    public String getFailMsg(Result result) {
        return "连接数据库失败:" + result.getMsg();
    }

    @Override
    public Result apply(InstallParamVo vo) {
        String dsn = String.format(
                "jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
                vo.getHost(),
                vo.getDbName());

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dsn);
        dataSource.setUsername(vo.getUsername());
        dataSource.setPassword(vo.getPassword());
        dataSource.setConnectionErrorRetryAttempts(0);
        dataSource.setInitialSize(1);
        dataSource.setFailFast(true);
        dataSource.setBreakAfterAcquireFailure(true);

        try {

            Connection connection = dataSource.getConnection();
            vo.setDataSource(dataSource);
            vo.setConnection(connection);

        } catch (SQLException e) {

            logger.error(e.getMessage());
            dataSource.close();
            return Result.fail(e.getMessage());
        }
        return Result.success();
    }
}
