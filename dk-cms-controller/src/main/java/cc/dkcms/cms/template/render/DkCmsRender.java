package cc.dkcms.cms.template.render;

import cc.dkcms.cms.common.Const;
import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.common.util.CmsUtils;
import cc.dkcms.cms.service.ServiceSetting;
import cc.dkcms.cms.service.ServiceTemplateFile;
import com.jfinal.aop.Aop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DkCmsRender {


    ServiceTemplateFile serviceTemplateFile = Aop.get(ServiceTemplateFile.class);


    public DkCmsRender() {
    }


    public Result renderToFile(DkCmsRenderContext context, String fileName) {

        File file = new File(fileName);
        if (!file.exists()) {
            try {
                FileUtils.forceMkdirParent(file);
                if (!file.createNewFile()) {
                    return Result.fail("fail when createNewFile :" + fileName);
                }

            } catch (Exception e) {
                return Result.fail("catch exception when renderToFile:" + e.getMessage());
            }

        }

        if (!file.canWrite()) {
            return Result.fail("file.can not Write" + fileName);
        }

        try {
            return renderToWriter(context, new FileWriter(file));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("catch exception in renderToFile:" + e.getMessage());
        }
    }

    public Result renderToResponse(DkCmsRenderContext context, HttpServletResponse response) {
        Writer writer = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setContentType("text/html");

            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("catch exception in response.getWriter:" + e.getMessage());
        }
        return renderToWriter(context, writer);
    }

    public Result renderToWriter(DkCmsRenderContext context, Writer writer) {

        if (writer == null) {
            return Result.fail("writer is null");
        }
        try {
            return __doRender(context, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("catch exception in renderToWriter:" + e.getMessage());

        }
    }

    private Result __doRender(DkCmsRenderContext dkCmsRenderContext, Writer writer) throws IOException {

        // 1.从context中获取模板信息
        DkCmsRenderTemplate dkTemplate = dkCmsRenderContext.getTemplateInfo();
        log.info("dkTemplate:" + dkTemplate);

        String templateFullPath = dkTemplate.getTemplateBase() + "/" + dkTemplate.getTemplateFile();

        // 模板不存在，获取db中保存的全局默认配置
        if (!(new File(templateFullPath)).exists()) {

            Boolean canUseDefaultTemplate = ServiceSetting.getBool(Const.SETTING_USE_DEFAULT_TEMPLATE, true);
            if (!canUseDefaultTemplate) {
                // 不能使用默认模板
                return Result.fail("template not exist:" + templateFullPath);
            }
            String defaultTemplate = dkCmsRenderContext.getDefaultTemplateFileName();

            String defaultTemplateFullPath = dkTemplate.getTemplateBase() + "/" + defaultTemplate;
            log.error("defaultFullPath:" + defaultTemplateFullPath);

            if (!(new File(defaultTemplateFullPath).exists())) {
                return Result.fail("template not exist:" + templateFullPath);
            }
            dkTemplate.setTemplateFile(defaultTemplate);

        }


        // 2 实例化模板引擎
        DkCmsEngineWrapper ENGINE = new DkCmsEngineWrapper(dkTemplate);
        ENGINE.setDate(dkCmsRenderContext);


        // 3 渲染页面
        try {
            String content = ENGINE.render();
            content = content.replaceFirst("</head>", getLogJs(dkCmsRenderContext) + "\n</head>");

            if (CmsUtils.isPreviewMode()) {
                content = printDebugInfo(content, dkTemplate, dkCmsRenderContext);
            } else {
                content = content.replaceFirst("</head>", Constant.GENERATE_NAME + "\n</head>");
            }
            writer.write(content);
            writer.close();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }

    }

    private String getLogJs(DkCmsRenderContext context) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Constant.LOG_JS_URL + "?type=" + context.getPageType().ordinal());
        if (DkCmsRenderPageType.CATEGORY_ARTICLE.equals(context.getPageType())) {
            stringBuilder.append("&id=" + context.getArticleId());
        } else if (DkCmsRenderPageType.CATEGORY_LIST.equals(context.getPageType()) ||
                DkCmsRenderPageType.CATEGORY_HOME.equals(context.getPageType())) {
            stringBuilder.append("&id=" + context.getCategoryId());
        }
        return "    <script type='text/javascript' " +
                "src='" + stringBuilder.toString() + "'></script>";
    }


    private String printDebugInfo(String content, DkCmsRenderTemplate template, DkCmsRenderContext context) {

        List<String> infoList = new ArrayList<>();
        infoList.add("<!--");
        infoList.add("templateBase:" + template.getTemplateBase());
        infoList.add("templateFile:" + template.getTemplateFile());
        infoList.add("templateType:" + template.getTemplateType().toString());
        infoList.add("pageType:" + context.getPageType());
        context.getDataForRender().forEach((key, value) -> {
            if ("articleList".equals(key)) {
                value = context.getArticleListPageInfo().getList().size();
            }
            if ("article".equals(key) || "articleVo".equals(key)) {
                value = "{mask}";
            }
            infoList.add("val{" + key + ":" + value + "}");
        });

        infoList.add("-->");

        return content + "\n" + StringUtils.join(infoList, "\n");
    }
}