package cc.dkcms.cms.service.install;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.InstallParamVo;
import org.apache.commons.lang.StringUtils;

public class TaskCheckParam extends InstallTask {
    @Override
    public String getSuccessMsg(Result result) {
        return "配置检测完成:配置合法";
    }

    @Override
    public String getFailMsg(Result result) {
        return "配置错误:" + result.getMsg();
    }

    @Override
    public Result apply(InstallParamVo vo) {
        if (StringUtils.isEmpty(vo.getHost())) {
            return Result.fail("host empty");
        }
        if (StringUtils.isEmpty(vo.getDbName())) {
            return Result.fail("dbName empty");
        }
        if (StringUtils.isEmpty(vo.getUsername())) {
            return Result.fail("username empty");
        }
        if (StringUtils.isEmpty(vo.getPassword())) {
            return Result.fail("password empty");
        }
        if (StringUtils.isEmpty(vo.getAdminName())) {
            return Result.fail("adminName empty");
        }
        if (StringUtils.isEmpty(vo.getAdminPwd())) {
            return Result.fail("adminPwd empty");
        }
        return Result.success();
    }
}
