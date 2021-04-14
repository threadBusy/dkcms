package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.helper.FileNameHelper;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.service.ServiceTemplateFile;
import com.jfinal.aop.Aop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ControllerTemplate extends BaseAdminController {


    ServiceTemplateFile templateService = Aop.get(ServiceTemplateFile.class);


    /*
        AjaxResponse ajaxNew() {

            String fileName        = get("fileName");
            String templateContent = get("templateContent");

            AjaxResponse response = new AjaxResponse();

            if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(templateContent)) {
                return response.setError("param missing");
            }

            if (templateService.checkFileExist(fileName)) {
                return response.setError("fileName already exists");
            }
            try {
                templateService.saveFileContent(fileName, templateContent);
            } catch (IOException e) {
                return response.setError(e.getMessage());
            }
            return response;
        }
*/
    public void updateFile() {

        String fileName        = get("fileName");
        String templateContent = get("templateContent");


        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(templateContent)) {
            renderJson(AjaxResponse.fail("param missing"));
            return;
        }

        Result result = templateService.saveFileContent(fileName, templateContent);
        if (result.getIsSuccess()) {
            renderJson(AjaxResponse.success());
            return;
        }
        renderJson(AjaxResponse.fail(result.getMsg()));
    }


    public void index() {
        set("currentTemplateName", templateService.getCurrentTemplate());
        set("list", templateService.getTemplateList().values());
    }

    public void generate() {
        set("pathPolicy", FileNameHelper.POLICY_PATH.getDesc());
    }

    public void seo() {
    }

    public void robots() {
        File file = new File(Constant.PATH_WEBROOT + "/robots.txt");

        if (isPost()) {

            String content = get("content");
            try {
                FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                renderJson(AjaxResponse.fail(e.getMessage()));
                return;
            }
            renderJson(AjaxResponse.success());
            return;
        }

        set("fileExists", file.exists());
        try {
            set("content", FileUtils.readFileToString(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            set("content", "文件不存在");
        }


    }

    public void file() {

        set("template", templateService.getCurrentTemplateVo());
        set("list", templateService.getAllFile());

    }

    public void add() {

    }

    public void edit() {

        String fileName = get("fileName");
        if (StringUtils.isEmpty(fileName)) {
            failPage("fileName missing");
            return;
        }
        if (!templateService.checkFileExist(fileName)) {
            failPage("fileName not exists");
            return;
        }
        set("fileName", fileName);
        set("templateContent", templateService.getFileContent(fileName));

    }

    public void refresh() {
        templateService.refreshTemplateList();
        msgPage("刷新成功", "#/admin/template/index");
    }

    public void change() {
        String tplName = get("tpl");
        Result result = templateService.changeTemplate(tplName);
        if(result.getIsSuccess()) {
            msgPage("切换成功", "#/admin/template/listTemplate");
        }else{
            failPage(result.getMsg());
        }
    }

}
