package cc.dkcms.cms.config;


import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.controller.admin.ControllerCommon;
import cc.dkcms.cms.controller.admin.ControllerInstall;
import cc.dkcms.cms.interceptor.TemplateVariables;
import com.jfinal.config.*;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InstallConfig extends JFinalConfig {

    public static Boolean hasLoad = false;

    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setInjectDependency(true);
        me.setI18nDefaultBaseName("messages");
    }

    public void configRoute(Routes me) {
        log.info("config install now");
        hasLoad = true;
        me.add(new Routes() {
            @Override
            public void config() {
                addInterceptor(new TemplateVariables());
                setBaseViewPath("/view/");
                add("/admin/install", ControllerInstall.class);
                add("/admin/common", ControllerCommon.class);
            }
        });
    }

    public void configEngine(Engine engine) {
        engine.setDevMode(true);
        engine.setBaseTemplatePath(null);
        engine.setToClassPathSourceFactory();
        engine.addSharedMethod(new StrKit());
        engine.addSharedObject("Constant", new Constant());
        Engine.setFastMode(true);
    }

    public void configPlugin(Plugins me) {

    }

    public void configInterceptor(Interceptors me) {
    }

    public void configHandler(Handlers handlers) {
    }

    @Override
    public void onStart() {
    }
}

