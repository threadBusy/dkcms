package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.service.ServiceDashboard;
import com.jfinal.aop.Aop;
import com.jfinal.core.ActionKey;

public class ControllerDashboard extends BaseAdminController {


    ServiceDashboard serviceDashboard = Aop.get(ServiceDashboard.class);

    @ActionKey(Constant.ADMIN_LOGIN_SUCCESS_JUMP_URL)
    public void index() {

    }

    public void main() {
        set("sysInfo", ServiceDashboard.sysInfo);
        set("contentInfo", serviceDashboard.getContentInfo());
        set("cmsRequire", ServiceDashboard.getCmsRequire());
        set("config", serviceDashboard.getConfig());

    }
}
