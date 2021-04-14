package cc.dkcms.cms.service.install;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.InstallParamVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.List;

@Slf4j
public class TaskImportDb extends InstallTask {
    @Override
    public Result apply(InstallParamVo installParamVo) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/install/init.sql");
            if (inputStream == null) {
                return Result.fail("read init.sql fail");

            }
            List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            if (lines.size() == 0) {
                return Result.fail("read init.sql empty");
            }

            String sqlContent = StringUtils.join(lines, "\n");
            log.info("sqlContent:");
            log.info(sqlContent);
            String[] sqlList = StringUtils.split(sqlContent, ";");

            Statement statement = installParamVo.getConnection().createStatement();

            for (String sql : sqlList) {
                log.info(sql);
                if (StringUtils.isEmpty(sql)) {
                    continue;
                }
                int result = statement.executeUpdate(sql);
                log.info(String.valueOf(result));
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("import db sql fail " + e.getMessage());
        }

    }

    @Override
    public String getSuccessMsg(Result result) {
        return "导入数据库结构完成";
    }

    @Override
    public String getFailMsg(Result result) {
        return "导入数据库结构失败:" + result.getMsg();
    }
}
