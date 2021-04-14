package cc.dkcms.cms.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class TemplateVo {
    // enjoy thymeleaf
    private String       type;
    private String       templatePath;
    private String       templateName;
    private String       author;
    private String       authorInfo;
    private String       snapshot;
    private String       description;
    private List<String> tagList;
}
