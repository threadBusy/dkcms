package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.InstallParamVo;
import cc.dkcms.cms.interceptor.AdminAccessCheck;
import cc.dkcms.cms.interceptor.BeforeInstall;
import cc.dkcms.cms.service.ServiceInstall;
import cc.dkcms.cms.service.install.InstallTask;
import cc.dkcms.cms.service.install.TaskClean;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ControllerInstall extends Controller {

    private final static Logger logger = LoggerFactory.getLogger(ControllerInstall.class);


    private static Boolean isConfigTestDone = false;

    private ServiceInstall serviceInstall = Aop.get(ServiceInstall.class);


    @Clear({BeforeInstall.class, AdminAccessCheck.class})
    @ActionKey(Constant.INSTALL_PAGE_URL)
    public void install() {

        if (ServiceInstall.testInstallDone()) {
            redirect(Constant.LOGIN_URL);
            return;
        }

        if (!getRequest().getMethod().equals("POST")) {
            return;
        }

        InstallParamVo vo     = getBean(InstallParamVo.class, "");
        List<String>   output = new ArrayList<>();
        for (InstallTask task : serviceInstall.getTaskList(isConfigTestDone)) {
            logger.info("run install task:" + task);
            Result result = task.apply(vo);
            if (result.getIsSuccess()) {
                output.add(task.getSuccessMsg(result));
            } else {
                String errMsg = task.getFailMsg(result);
                if (StrKit.isBlank(errMsg)) {
                    errMsg = "error:" + task;
                }
                renderJson(AjaxResponse.fail(errMsg));
                return;
            }
        }
        //always run clean
        new TaskClean().apply(vo);
        if (!isConfigTestDone) {
            isConfigTestDone = true;
        } else {
            // 安装完成，需要初始化一下数据库dao和categoryTree
            output.add("安装完成");
        }
        renderJson(AjaxResponse.success(StringUtils.join(output, "<br>")));


    }


}

