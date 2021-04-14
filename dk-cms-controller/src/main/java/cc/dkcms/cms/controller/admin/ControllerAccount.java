package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.CommonAuth;
import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AccountType;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.utils.forms.FormAdministrator;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.dao.DaoAccount;
import cc.dkcms.cms.service.ServiceAccount;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class ControllerAccount extends BaseAdminController {

    ServiceAccount serviceAccount = Aop.get(ServiceAccount.class);

    public void index() {
        __accountList(AccountType.ADMIN);

    }

    public void user() {
        __accountList(AccountType.USER);
    }

    private void __accountList(AccountType role) {
        int              pageNo   = getParaToInt("page", 1);
        Page<DaoAccount> pageList = serviceAccount.getListByPage(pageNo, Constant.pageSize,role);

        if (pageList == null) {
            failPage("pageList is null");
            return;
        }
        set("page", pageList);
        set("list", pageList.getList());
        set("itemList", FormAdministrator.get());
    }

    public void save() {
        if (!isPost()) {
            renderJson(AjaxResponse.fail("no post"));
            return;
        }
        DaoAccount admin = getModel(DaoAccount.class, "");
        if (admin.getId() == null) {
            if (StringUtils.isEmpty(admin.getUsername()) || StringUtils.isEmpty(admin.getPassword())) {
                renderJson(AjaxResponse.fail("请填写完整用户名和密码"));
                return;
            }

            if (!CommonAuth.rexCheckUsername(admin.getUsername())) {
                renderJson(AjaxResponse.fail("用户名应该为 6-20 位,字母和数字"));
                return;
            }

            if (!CommonAuth.rexCheckPassword(admin.getPassword())) {
                renderJson(AjaxResponse.fail("密码应该为  6-20 位，字母、数字、字符"));
                return;
            }

            admin.setPassword(CommonAuth.generatePassword(admin.getUsername(), admin.getPassword()));
            admin.setRegTime(new Date());
            admin.setLoginTimes(0L);
            admin.setRole(AccountType.ADMIN.getVal());
            admin.save();
        } else {

            // TODO
            // user info update
        }
        renderJson(AjaxResponse.success());

    }

    public void updatePassword() {
        Integer id       = getInt("id");
        String  password = get("password");

        if (id == null || StringUtils.isEmpty(password)) {
            renderJson("参数错误");
            return;
        }
        if (!CommonAuth.rexCheckPassword(password)) {
            renderJson(AjaxResponse.fail("密码应该为  6-20 位，字母、数字、字符"));
            return;
        }
        Result result = serviceAccount.updatePassword(id, password);
        if (result.getIsSuccess()) {
            renderJson(AjaxResponse.success());
            return;
        }
        renderJson(AjaxResponse.fail(result.getMsg()));
    }


    public void toggleEnable() {
        Integer id = getInt("id");
        serviceAccount.toggleEnable(id);
        renderJson(AjaxResponse.success());
    }
}