package cc.dkcms.cms.handler;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.render.DkCmsRenderPageType;
import cc.dkcms.cms.service.ServiceContent;
import com.jfinal.aop.Aop;
import com.jfinal.handler.Handler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class HandlerWebLogJs extends Handler {

    private final ServiceContent serviceContent = Aop.get(ServiceContent.class);

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {

        if (Constant.LOG_JS_URL.equals(target)) {

            try {
                int     type = Integer.valueOf(request.getParameter("type"));
                Integer id   = Integer.valueOf(request.getParameter("id"));

                DkCmsRenderPageType pageType = DkCmsRenderPageType.of(type);
                if (pageType != null || id > 1) {
                    if (DkCmsRenderPageType.CATEGORY_ARTICLE.equals(pageType)) {
                        serviceContent.click(id);
                    } else if (DkCmsRenderPageType.CATEGORY_LIST.equals(pageType) ||
                            DkCmsRenderPageType.CATEGORY_HOME.equals(pageType)) {

                        // DO SOME
                    } else if (DkCmsRenderPageType.SITE_HOMEPAGE.equals(pageType)) {
                        // DO SOME
                    }
                }

                PrintWriter writer = response.getWriter();
                writer.println("console.log('log success')");
                writer.close();
            } catch (Exception e) {
                log.error("cate exception in HandlerWebLogJs:" + e.getMessage());
            }
            return;
        }

        next.handle(target, request, response, isHandled);
    }
}
