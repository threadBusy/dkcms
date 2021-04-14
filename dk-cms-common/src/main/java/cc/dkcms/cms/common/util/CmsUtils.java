package cc.dkcms.cms.common.util;

import cc.dkcms.cms.common.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CmsUtils {

    public static Boolean isPreviewMode() {
        return !isStaticPageMode();
    }

    public static Boolean isStaticPageMode() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {

            //log.error("stack:" + element.getClassName());
            if (element.getClassName().endsWith("ControllerSite")) {
                return false;
            }
        }
        return true;
    }
}
