package cc.dkcms.cms.service.install;

import com.alibaba.druid.pool.DruidDataSource;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.InstallParamVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class TaskClean extends InstallTask {

    private static final Logger logger = LoggerFactory.getLogger(TaskClean.class);

    @Override
    public String getSuccessMsg(Result result) {
        return "安装环境清理完毕";
    }

    @Override
    public String getFailMsg(Result result) {
        return "安装环境清理遇到错误";
    }

    @Override
    public Result apply(InstallParamVo installParamVo) {

        try {
            Connection connection = installParamVo.getConnection();
            if (connection != null) {
                logger.debug("连接关闭");
                connection.close();
            }
            DruidDataSource dataSource = installParamVo.getDataSource();
            if (dataSource != null) {
                logger.debug("dataSource 关闭");
                dataSource.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Result.success();
    }
}
