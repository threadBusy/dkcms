package cc.dkcms.cms.service;

import cc.dkcms.cms.common.Const;
import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.vo.TemplateVo;
import com.jfinal.aop.Aop;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static cc.dkcms.cms.common.Constant.CONFIG_FILE;

public class ServiceDashboard {

    ServiceTemplateFile serviceTemplateFile = Aop.get(ServiceTemplateFile.class);

    public static Map<String, String> sysInfo = new LinkedHashMap<>();

    static {

        int     mb = 1024 * 1024;
        Runtime r  = Runtime.getRuntime();
        sysInfo.put("totalMemory", (r.totalMemory() / mb) + " MB");
        sysInfo.put("freeMemory", (r.freeMemory() / mb) + " MB");
        sysInfo.put("maxMemory", (r.maxMemory() / mb) + " MB");
        sysInfo.put("availableProcessors", String.valueOf(r.availableProcessors()));

        sysInfo.put("java.version", System.getProperty("java.version"));
        sysInfo.put("java.vendor", System.getProperty("java.vendor"));
        sysInfo.put("java.home", System.getProperty("java.home"));
        sysInfo.put("java.class.path", System.getProperty("java.class.path"));
        sysInfo.put("java.io.tmpdir", System.getProperty("java.io.tmpdir"));
        sysInfo.put("os.name", System.getProperty("os.name"));
        sysInfo.put("os.version", System.getProperty("os.version"));
        sysInfo.put("user.dir", System.getProperty("user.dir"));

    }


    public static Map<String, String> getCmsRequire() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("webroot", Constant.PATH_WEBROOT);
        map.put("template", Constant.PATH_TEMPLATE);
        map.put("webroot目录检查", checkDir(Constant.PATH_WEBROOT));
        map.put("template目录检查", checkDir(Constant.PATH_TEMPLATE));
        return map;
    }

    private static String checkDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            return "<span style='color:red;'>目录不存在,请创建</span>";
        }
        if (!file.canWrite()) {
            return "<span style='color:red;'>不可写入,请修改权限</span>";
        }
        return "<span style='color:green;'>正常</span>";
    }


    public Map<String, String> getContentInfo() {

        ServiceContent  serviceContent  = Aop.get(ServiceContent.class);
        ServiceCategory serviceCategory = Aop.get(ServiceCategory.class);

        Map<String, String> map = new HashMap<>();
        map.put("articleTotalNum", String.valueOf(serviceContent.getTotalNum()));
        map.put("categoryTotalNum", String.valueOf(serviceCategory.getTotalNum()));

        TemplateVo templateVo = serviceTemplateFile.getCurrentTemplateVo();
        map.put("template", templateVo.getTemplateName() + "(" + templateVo.getTemplatePath() + ")");

        return map;
    }

    public Map<String, String> getConfig() {
        Prop prop = PropKit.use(CONFIG_FILE);

        Map<String, String> map = new HashMap<>();
        map.put("JDBC_URL", prop.get(Const.JDBC_URL));
        map.put("JDBC_USERNAME", prop.get(Const.JDBC_USERNAME));
        //map.put("JDBC_PASSWORD", prop.get(Const.JDBC_PASSWORD));
        map.put("JDBC_IS_SHOW_SQL", prop.get(Const.JDBC_IS_SHOW_SQL, "false"));

        map.put("TOKEN_SALT", prop.get(Const.TOKEN_SALT));
        map.put("TOKEN_VERSION", prop.get(Const.TOKEN_VERSION));


        return map;
    }
}
