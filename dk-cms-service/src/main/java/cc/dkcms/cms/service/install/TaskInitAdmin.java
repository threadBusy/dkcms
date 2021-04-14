package cc.dkcms.cms.service.install;

import cc.dkcms.cms.common.CommonAuth;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.InstallParamVo;

import java.sql.Date;
import java.sql.PreparedStatement;

public class TaskInitAdmin extends InstallTask {

    @Override
    public String getSuccessMsg(Result result) {
        return "建立管理员账号成功";
    }

    @Override
    public String getFailMsg(Result result) {
        return "建立管理员账号失败";
    }

    @Override
    public Result apply(InstallParamVo vo) {
        try {
            String sql = "INSERT INTO dao_account " +
                    "(username,password,enable,reg_time,role) " +
                    "VALUES (?,?,?,?,?)";

            PreparedStatement statement = vo.getConnection().prepareStatement(sql);
            statement.setString(1, vo.getAdminName());
            statement.setString(2, CommonAuth.generatePassword(vo.getAdminName(), vo.getAdminPwd()));
            statement.setBoolean(3, true);
            statement.setDate(4, new Date(System.currentTimeMillis()));
            statement.setInt(5, 2);

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
        return Result.success();
    }
}
