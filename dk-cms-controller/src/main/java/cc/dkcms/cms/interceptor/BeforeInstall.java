package cc.dkcms.cms.interceptor;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.service.ServiceInstall;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeforeInstall implements Interceptor {
    private final static Logger logger = LoggerFactory.getLogger(BeforeInstall.class);


    @Override
    public void intercept(Invocation inv) {

        if (!ServiceInstall.testInstallDone()) {
            logger.error("need install; goto install");
            inv.getController().redirect(Constant.INSTALL_PAGE_URL);
        } else {
            inv.invoke();
        }

    }

}
