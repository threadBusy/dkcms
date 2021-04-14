package cc.dkcms.cms.interceptor;

import cc.dkcms.cms.common.CommonAuth;
import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AccountType;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.dao.DaoAccount;
import cc.dkcms.cms.service.ServiceAccount;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminAccessCheck implements Interceptor {
    private final static Logger logger = LoggerFactory.getLogger(AdminAccessCheck.class);

    private ServiceAccount serviceAdministrator = Aop.get(ServiceAccount.class);

    public void intercept(Invocation inv) {

        Controller controller = inv.getController();
        Result     result     = CommonAuth.checkLogin(controller);
        if (!result.getIsSuccess()) {
            logger.info("AdminAccessCheck check login fail:" + result.getMsg());
            controller.redirect(Constant.LOGIN_URL);
            return;
        }
        Long       adminId = (Long) result.getData();
        DaoAccount admin   = serviceAdministrator.getById(adminId);
        if (!AccountType.of(admin.getRole()).equals(AccountType.ADMIN)) {
            logger.info("AdminAccessCheck check login fail not admin");
            controller.redirect(Constant.LOGIN_URL);
            return;
        }

        //TODO 不太雅，看是否服务器全局了，要验证一下
        controller.set("__ADMIN__", admin);
        controller.setSessionAttr("__ADMIN__", admin);
        inv.invoke();
    }
}
