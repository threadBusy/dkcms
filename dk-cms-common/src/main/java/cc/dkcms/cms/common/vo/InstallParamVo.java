package cc.dkcms.cms.common.vo;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

import java.sql.Connection;

@Data
public class InstallParamVo {
    private String          host;
    private String          username;
    private String          password;
    private String          dbName;
    private String          adminName;
    private String          adminPwd;
    private Connection      connection;
    private DruidDataSource dataSource;
}
