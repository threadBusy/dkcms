package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.interceptor.AdminAccessCheck;
import cc.dkcms.cms.service.ServiceInstall;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.upload.UploadFile;

import java.io.File;

public class ControllerCommon extends BaseAdminController {


    // 站点首页
    @Clear(AdminAccessCheck.class)
    @ActionKey("/")
    public void siteIndexPage() {

        if (!ServiceInstall.testInstallDone()) {
            redirect(Constant.INSTALL_PAGE_URL);
            return;
        }

        if (new File(Constant.PATH_WEBROOT + "/index.html").exists()) {
            redirect("/index.html");
            return;
        }
        redirect(Constant.PREVIEW_URL_PATH);
    }

    public void fileUpload() {
        AjaxResponse response = new AjaxResponse();
        UploadFile   file     = getFile("uploadFile");

        response.setMsg("/upload/" + file.getFileName());
        renderJson(response);
    }


}
