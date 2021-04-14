package cc.dkcms.cms.common.util;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class BeanCopyUtils {

    public static void copy(Object src, Object des) {
        try {
            BeanUtils.copyProperties(des, src);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
