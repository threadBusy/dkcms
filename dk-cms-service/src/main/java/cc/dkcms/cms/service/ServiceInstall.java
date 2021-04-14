package cc.dkcms.cms.service;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.service.install.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static cc.dkcms.cms.common.Constant.CONFIG_FILE;
import static cc.dkcms.cms.common.Constant.PATH_CONFIG;

@Slf4j
public class ServiceInstall {

    public List<InstallTask> getTaskList(Boolean isConfigTestDone) {
        List<InstallTask> taskList = new LinkedList<>();
        taskList.add(new TaskCheckParam());
        taskList.add(new TaskInitConnection());

        if (isConfigTestDone) {
            taskList.add(new TaskImportDb());
            taskList.add(new TaskInitAdmin());
            taskList.add(new TaskWriteConfig());
        }
        return taskList;
    }

    public static boolean testInstallDone() {
        try {
            File configFile = new File(PATH_CONFIG + CONFIG_FILE);
            if (!configFile.exists()) {
                return false;
            }
            Prop prop = PropKit.use(configFile);
            if (prop != null) {
                return true;
            }
            return true;
        } catch (Exception e) {
            log.error("load {} fail; {}", Constant.CONFIG_FILE, e.getMessage());
        }
        return false;
    }
}
