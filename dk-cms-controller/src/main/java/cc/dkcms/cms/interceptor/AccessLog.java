package cc.dkcms.cms.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessLog implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(AccessLog.class);

    @Override
    public void intercept(Invocation inv) {

        HttpServletRequest  request  = inv.getController().getRequest();
        HttpServletResponse response = inv.getController().getResponse();

        long start = System.currentTimeMillis();
        inv.invoke();

        logger.info("[ACCESS][{}][{}][{}][{}]",
                request.getRequestURL(),
                ((System.currentTimeMillis() - start) / 1000) + "s",
                response.getStatus()
        );
    }
}
