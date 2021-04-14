package cc.dkcms.cms.service;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.render.TemplateType;
import cc.dkcms.cms.common.vo.TemplateFileVo;
import cc.dkcms.cms.common.vo.TemplateVo;
import com.jfinal.aop.Aop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

import static cc.dkcms.cms.common.Constant.PATH_WEBROOT;

@Slf4j
public class ServiceTemplateFile {
    private ServiceSetting serviceSetting = Aop.get(ServiceSetting.class);


    private static final String PATH_TEMPLATE    =
            Constant.PATH_TEMPLATE.endsWith("/") ?
                    Constant.PATH_TEMPLATE :
                    Constant.PATH_TEMPLATE + "/";
    private static       String CURRENT_TEMPLATE = "";

    private static final String SETTING_TEMPLATE_KEY = "template";

    private static final Map<String, TemplateVo> TEMPLATE_LIST = new HashMap<>();

    public ServiceTemplateFile() {
        scanTemplate();
    }


    private void scanTemplate() {
        File     dir   = new File(PATH_TEMPLATE);
        String[] files = dir.list(DirectoryFileFilter.INSTANCE);
        if (files == null) {
            return;
        }
        TEMPLATE_LIST.clear();
        for (String file : files) {
            TemplateVo vo = loadTemplateInfo(file);
            if (vo != null) {
                copyScreenShotImage(vo.getTemplatePath());
                TEMPLATE_LIST.putIfAbsent(vo.getTemplatePath(), vo);
            }
        }
        CURRENT_TEMPLATE = getCurrentTemplate();
    }

    public Result copyScreenShotImage(String templatePath) {
        try {
            File to = new File(PATH_WEBROOT + "template/" + templatePath + "/screenshot.png");
            if (to.exists()) {
                return Result.success();
            }
            File from = new File(PATH_TEMPLATE + templatePath + "/screenshot.png");
            if (!from.exists()) {
                return Result.fail("no screenshot");
            }
            FileUtils.copyFile(from, to);
            return Result.success();
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result changeTemplate(String tplName) {
        if (StringUtils.isEmpty(tplName)) {
            return Result.fail("name missing");
        }
        if (!TEMPLATE_LIST.containsKey(tplName)) {
            return Result.fail("tpl not exist");
        }

        serviceSetting.setValue(SETTING_TEMPLATE_KEY, tplName);
        CURRENT_TEMPLATE = tplName;


        try {
            File fromDir = new File(PATH_TEMPLATE + tplName);
            File desDir  = new File(PATH_WEBROOT + "template/" + tplName);
            FileUtils.copyDirectory(fromDir, desDir, f -> !(f.getName().endsWith(".html") || f.getName().endsWith(".info")));
            return Result.success();
        } catch (IOException e) {
            return Result.fail(e.getMessage());
        }
    }

    public void refreshTemplateList() {
        scanTemplate();
    }

    public String getCurrentTemplate() {
        return serviceSetting.getValue(SETTING_TEMPLATE_KEY);
    }

    public TemplateVo getCurrentTemplateVo() {
        String path = getCurrentTemplate();
        return TEMPLATE_LIST.get(path);
    }

    public Map<String, TemplateVo> getTemplateList() {
        return TEMPLATE_LIST;
    }

    private TemplateVo loadTemplateInfo(String templatePath) {
        File templateInfoFile = new File(PATH_TEMPLATE + templatePath + "/tpl.info");
        if (!templateInfoFile.exists()) {
            return null;
        }

        Properties properties = new Properties();
        try {
            properties.load(new FileReader(templateInfoFile));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        TemplateVo vo = new TemplateVo();
        vo.setTemplatePath(templatePath);
        vo.setAuthor(properties.getProperty("author"));
        vo.setAuthorInfo(properties.getProperty("authorInfo"));
        vo.setDescription(properties.getProperty("description"));
        vo.setSnapshot(properties.getProperty("snapshot"));
        vo.setTemplateName(properties.getProperty("name"));
        vo.setType(properties.getProperty("type"));
        String tag = properties.getProperty("tag");

        List<String> tagList = new ArrayList<>();
        if (!StringUtils.isEmpty(tag)) {
            tag = tag.replaceAll("[,，、|;；]", "|");
            tagList.addAll(Arrays.asList(StringUtils.split(tag, "|")));
        }
        vo.setTagList(tagList);

        if (StringUtils.isEmpty(vo.getType())) {
            vo.setType(TemplateType.THYMELEAF.toString());
        }

        return vo;
    }

    public String getFileContent(String fileName) {
        try {
            File file = new File(PATH_TEMPLATE + CURRENT_TEMPLATE + "/" + fileName);
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean checkFileExist(String fileName) {


        return (new File(PATH_TEMPLATE + CURRENT_TEMPLATE + "/" + fileName)).exists();

    }

    public List<TemplateFileVo> getAllFile() {
        final List<TemplateFileVo> list = new ArrayList<>();


        File templateDir = new File(PATH_TEMPLATE + CURRENT_TEMPLATE);
        if (!templateDir.exists() || !templateDir.isDirectory()) {
            log.error("Template Path not directory");
            return list;
        }
        Collection<File> templateFile = FileUtils.listFiles(templateDir, null, true);
        for (File file : templateFile) {
            TemplateFileVo vo               = new TemplateFileVo();
            String         fileNameWithPath = file.getPath().replace(PATH_TEMPLATE + CURRENT_TEMPLATE, "");
            vo.setFileName(fileNameWithPath);
            vo.setLastModify(fromLong(file.lastModified()));
            list.add(vo);
        }

        return list;

    }


    private LocalDateTime fromLong(long l) {
        return LocalDateTime.ofEpochSecond(
                l / 1000,
                0,
                OffsetDateTime.now().getOffset()
        );
    }

    public Result saveFileContent(String fileName, String templateContent) {
        try {
            File file = new File(PATH_TEMPLATE + CURRENT_TEMPLATE + "/" + fileName);
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("createNewFile fail");
            }
            FileUtils.write(file, templateContent, StandardCharsets.UTF_8);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}
