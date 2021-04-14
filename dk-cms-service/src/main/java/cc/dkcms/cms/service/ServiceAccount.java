package cc.dkcms.cms.service;

import cc.dkcms.cms.common.CommonAuth;
import cc.dkcms.cms.common.define.AccountType;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.dao.DaoAccount;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;

public class ServiceAccount {

    DaoAccount daoAccount = Aop.get(DaoAccount.class);


    public Page<DaoAccount> getListByPage(Integer pageNo, Integer pageSize, AccountType role) {
        return daoAccount.getListByPage(pageNo, pageSize, role);
    }

    public DaoAccount getById(Long id) {
        return daoAccount.findById(id);
    }


    public void toggleEnable(Integer id) {
        DaoAccount admin = daoAccount.findById(id);

        if (admin.getEnable()) {
            admin.setEnable(false);
        } else {
            admin.setEnable(true);
        }
        admin.update();
    }

    public Result updatePassword(Integer id, String password) {

        DaoAccount admin = daoAccount.findById(id);
        if (admin == null) {
            return Result.fail("id error");
        }
        admin.setPassword(CommonAuth.generatePassword(admin.getUsername(), password));
        if (admin.update())
            return Result.success();
        return Result.fail("db error");
    }

    public DaoAccount getAccountByUsernameAndPassword(String username, String password) {

        return daoAccount.getAccountByUsernameAndPassword(
                username, CommonAuth.generatePassword(username, password));
    }

    public void logAdminLogin(DaoAccount admin, String ip) {

        DaoAccount account = new DaoAccount();
        account.setId(admin.getId());
        account.setLastIp(ip);
        account.setLastLoginAt(new Date());
        account.setLoginTimes(admin.getLoginTimes() + 1);
        account.update();


    }

    public boolean checkUserNameExist(String username) {
        return DaoAccount.dao.checkUsernameExist(username);
    }

    public Result createUserAccount(String username, String password) {
        DaoAccount account = new DaoAccount();
        account.setUsername(username);
        account.setPassword(CommonAuth.generatePassword(username, password));
        account.setEmail(username);
        account.setRegTime(new Date());
        account.setLoginTimes(0L);
        account.setRole(AccountType.USER.getVal());
        if (account.save()) {
            return Result.success();
        }
        return Result.fail("db error");

    }
}
