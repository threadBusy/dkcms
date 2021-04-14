package cc.dkcms.cms.config;

import cc.dkcms.cms.common.Const;
import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.StaticPagePathPolicy;
import cc.dkcms.cms.common.helper.FileNameHelper;
import cc.dkcms.cms.controller.admin.*;
import cc.dkcms.cms.controller.front.ControllerOpenApi;
import cc.dkcms.cms.controller.front.ControllerSite;
import cc.dkcms.cms.dao._MappingKit;
import cc.dkcms.cms.handler.HandlerWebLogJs;
import cc.dkcms.cms.handler.WebSocketHandler;
import cc.dkcms.cms.interceptor.AccessLog;
import cc.dkcms.cms.interceptor.AdminAccessCheck;
import cc.dkcms.cms.interceptor.BeforeInstall;
import cc.dkcms.cms.interceptor.TemplateVariables;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.service.ServiceInstall;
import cc.dkcms.cms.service.ServiceSetting;
import cc.dkcms.cms.service.ServiceTemplateFile;
import cc.dkcms.cms.template.directive.admin.*;
import cc.dkcms.cms.template.directive.common.DirectiveGetSetting;
import com.jfinal.aop.Aop;
import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cc.dkcms.cms.common.Constant.CONFIG_FILE;

public class NormalConfig extends JFinalConfig {

    private final static Logger logger = LoggerFactory.getLogger(NormalConfig.class);


    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setInjectDependency(true);
        me.setI18nDefaultBaseName("messages");
    }

    public void configRoute(Routes me) {
        me.add(new Routes() {
            @Override
            public void config() {
                addInterceptor(new TemplateVariables());
                setBaseViewPath("/view/front");
                add(Constant.PREVIEW_URL_PATH, ControllerSite.class);
                add("/openapi/", ControllerOpenApi.class);
            }
        });
        me.add(new Routes() {
            @Override
            public void config() {

                addInterceptor(new AdminAccessCheck());
                addInterceptor(new TemplateVariables());
                setBaseViewPath("/view/");
                // viewPath 由 thymeleaf 里写死了


                add("/admin/login", ControllerLogin.class);
                add("/admin/dashboard", ControllerDashboard.class);
                add("/admin/account", ControllerAccount.class);


                add("/admin/category", ControllerCategory.class);
                add("/admin/content", ControllerContent.class);
                add("/admin/tag", ControllerTag.class);
                add("/admin/setting", ControllerSetting.class);
                add("/admin/singlePage", ControllerSinglePage.class);
                add("/admin/template", ControllerTemplate.class);
                add("/admin/ad", ControllerAd.class);
                add("/admin/model", ControllerUserModel.class);
                add("/admin/api", ControllerApi.class);


                logger.error("InstallConfig.hasLoad:" + InstallConfig.hasLoad);
                if (!InstallConfig.hasLoad) {
                    add("/admin/common", ControllerCommon.class);
                }


            }
        });

    }

    public void configEngine(Engine engine) {


        try {
            engine.setDevMode(true);

            engine.setToClassPathSourceFactory();
            if (!InstallConfig.hasLoad) {
                engine.addSharedMethod(new StrKit());
                engine.addSharedObject("Constant", new Constant());
            }
            engine.setBaseTemplatePath(null);
            engine.addSharedFunction("/view/function/layout.html");
            engine.addSharedFunction("/view/function/imageUploader.html");
            engine.addSharedFunction("/view/function/printFormItems.html");

            engine.addDirective("getSetting", DirectiveGetSetting.class);
            engine.addDirective("getMessage", DirectiveAdminGetI18nMessage.class);
            engine.addDirective("adminCategorySelect", DirectiveAdminCategorySelect.class);
            engine.addDirective("adminCategoryTypeSelect", DirectiveAdminCategoryTypeSelect.class);

            engine.addDirective("adminTagSelect", DirectiveAdminTagSelect.class);
            engine.addDirective("adminCheckBox", DirectiveAdminCheckBox.class);
            engine.addDirective("adminFileUploader", DirectiveAdminFileUploader.class);
            engine.addDirective("adminPageNav", DirectiveAdminPageNav.class);
            engine.addDirective("adminRichText", DirectiveAdminRichText.class);
            engine.addDirective("adminSinglePageSelect", DirectiveAdminSinglePageSelect.class);
            engine.addDirective("adminTemplateFileSelect", DirectiveAdminTemplateFileSelect.class);
            engine.addDirective("adminUserModelField", DirectiveAdminUserModelField.class);
            engine.addDirective("adminUserModelFieldTypeSelect", DirectiveAdminUserModelFieldTypeSelect.class);

            Engine.setFastMode(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void configPlugin(Plugins me) {

        if (!ServiceInstall.testInstallDone()) {
            logger.error("need login;so skip init ActiveRecordPlugin");
            return;
        }
        try {
            Prop    prop      = PropKit.use(CONFIG_FILE);
            String  url       = prop.get(Const.JDBC_URL);
            String  username  = prop.get(Const.JDBC_USERNAME);
            String  password  = prop.get(Const.JDBC_PASSWORD);
            Boolean isShowSql = prop.getBoolean(Const.JDBC_IS_SHOW_SQL, false);

            if (StrKit.isBlank(url) || StrKit.isBlank(username)) {
                throw new Exception("jdbc.url or jdbc.username is empty in config file");
            }

            logger.info("init DruidPlugin");
            DruidPlugin druidPlugin = new DruidPlugin(url, username, password);
            me.add(druidPlugin);

            ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
            _MappingKit.mapping(arp);


            arp.setShowSql(isShowSql);

            me.add(arp);


        } catch (Exception e) {
            logger.error("load config file fail; DruidPlugin not init:" + e.getMessage());
        }


    }

    public void configInterceptor(Interceptors me) {
        // 以下两行代码配置作用于控制层的全局拦截器

        try {
            me.addGlobalActionInterceptor(new BeforeInstall());
            me.addGlobalActionInterceptor(new AccessLog());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 以下一行代码配置业务层全局拦截器
        //me.addGlobalServiceInterceptor(new BbbInterceptor());
    }

    public void configHandler(Handlers handlers) {
        if (ServiceInstall.testInstallDone()) {
            handlers.add(new WebSocketHandler(ControllerPageGenerate.URL_PATH));
            handlers.add(new HandlerWebLogJs());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ServiceInstall.testInstallDone()) {
            // 构建 category Tree
            ServiceCategory serviceCategory = Aop.get(ServiceCategory.class);
            logger.error("on start serviceCategory:" + serviceCategory);

            // 设置当前模板
            ServiceSetting serviceSetting = Aop.get(ServiceSetting.class);
            FileNameHelper.POLICY_PATH = StaticPagePathPolicy.of(
                    serviceSetting.getValue(Const.SETTING_STATIC_PAGE_FILE_PATH_POLICY)
            );

            ServiceTemplateFile templateService = Aop.get(ServiceTemplateFile.class);
            // 安装一下基本款模板的静态文件
            templateService.changeTemplate(templateService.getCurrentTemplate());
        }
    }
}
