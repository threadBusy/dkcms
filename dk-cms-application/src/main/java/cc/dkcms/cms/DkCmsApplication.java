package cc.dkcms.cms;

import cc.dkcms.cms.config.InstallConfig;
import cc.dkcms.cms.config.NormalConfig;
import cc.dkcms.cms.controller.admin.ControllerPageGenerate;
import cc.dkcms.cms.service.ServiceInstall;
import com.jfinal.server.undertow.UndertowServer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DkCmsApplication {


    public static void main(String[] args) {

        DkCmsApplication application = new DkCmsApplication();
        application.load();
    }

    private void load() {
        if (!ServiceInstall.testInstallDone()) {
            log.info("has not install; wait instatll");
            UndertowServer installServer = UndertowServer.create(InstallConfig.class);
            installServer.start();
            while (true) {
                sleep();
                if (ServiceInstall.testInstallDone()) {
                    break;
                }
            }
            installServer.stop();
        }

        UndertowServer.create(NormalConfig.class)
                .configWeb(webBuilder -> {
                    webBuilder.addWebSocketEndpoint(ControllerPageGenerate.class);
                }).start();
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
