package cc.dkcms.cms.service.install;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.InstallParamVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static cc.dkcms.cms.common.Constant.CONFIG_FILE;
import static cc.dkcms.cms.common.Constant.PATH_CONFIG;

@Slf4j
public class TaskWriteConfig extends InstallTask {
    @Override
    public String getSuccessMsg(Result result) {
        return "写配置文件名成功";
    }

    @Override
    public String getFailMsg(Result result) {
        return "写配置文件名失败:" + result.getMsg();
    }

    @Override
    public Result apply(InstallParamVo installParamVo) {
        try {

            log.info("start write config file:" + PATH_CONFIG + CONFIG_FILE);
            File file = new File(PATH_CONFIG + CONFIG_FILE);
            FileUtils.writeStringToFile(file, getConfig(installParamVo), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Result.fail("writeConfig fail:" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("writeConfig fail:" + e.getMessage());

        }
        return Result.success("writeConfig success");
    }


    private String getConfig(InstallParamVo vo) {

        List<String> config = new LinkedList<>();
        config.add("token.salt=" + RandomStringUtils.randomAlphanumeric(32));
        config.add("token.version=1");
        config.add("");
        config.add("jdbc.url=jdbc:mysql://" + vo.getHost() + "/" + vo.getDbName() + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        config.add("jdbc.username=" + vo.getUsername());
        config.add("jdbc.password=" + vo.getPassword());
        config.add("jdbc.show-sql=true");
        config.add("");

        config.add("install-data=" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return StringUtils.join(config, "\n");
    }
}
