package cc.dkcms.cms.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemplateFileVo {
    private Integer       id;
    private String        fileName;
    private LocalDateTime lastModify;
}
