package cc.dkcms.cms.controller.base;


import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.dao.DaoAccount;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;

public class BaseAdminController extends Controller {


    @NotAction
    protected boolean isPost() {
        return getRequest().getMethod().equals("POST");
    }

    @NotAction
    protected DaoAccount getCurrentAdmin() {
        return (DaoAccount) getSessionAttr("__ADMIN__");
    }

    @NotAction
    protected void failPage(String msg) {
        failPage(msg, Constant.URL_GO_BACK);
    }

    @NotAction
    protected void failPage(String msg, String url) {
        if (url != null && !url.startsWith("#")) {
            url = "#" + url;
        }
        set("failMsg", msg);
        set("url", url);
        render("/view/admin/common/error.html");
    }

    @NotAction
    protected void msgPage(String msg, String url) {
        if (url != null && !url.startsWith("#")) {
            url = "#" + url;
        }
        set("msg", msg);
        set("url", url);
        render("/view/admin/common/msg.html");
    }


}
