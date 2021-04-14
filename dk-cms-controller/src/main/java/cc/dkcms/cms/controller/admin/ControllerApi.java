package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.vo.ApiTokenVo;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.service.ServiceApiToken;
import com.jfinal.aop.Aop;
import org.apache.commons.lang.StringUtils;

public class ControllerApi extends BaseAdminController {

    ServiceApiToken serviceApiToken = Aop.get(ServiceApiToken.class);

    public void index() {
        set("list", serviceApiToken.getAllToken());
    }

    public void apiList() {

    }

    public void setValid() {
        Integer id = getInt("id");
        if (id == null) {
            failPage("id missing");
            return;

        }
        ApiTokenVo vo = serviceApiToken.getVoById(id);
        if (vo == null) {
            failPage("id valid get null");
            return;
        }
        serviceApiToken.changeValid(id);
        msgPage("修改成功", "#/admin/api/list");
    }


    public void deleteToken() {

        Integer id = getInt("id");
        if (id == null) {
            failPage("id missing");
        }
        serviceApiToken.deleteById(id);
        msgPage("删除成功", "#/admin/api/list");
    }

    public void tokenAdd() {
        if (isPost()) {
            String  name     = get("name");
            Integer validDay = getInt("valid_days", 0);
            if (StringUtils.isEmpty(name)) {
                renderJson(AjaxResponse.fail("请填写Token用途"));
                return;

            }
            serviceApiToken.generateToken(name, validDay, getCurrentAdmin().getId());
            renderJson(AjaxResponse.success());
            return;
        }

    }
}
