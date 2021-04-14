package cc.dkcms.cms.common.vo;

import lombok.Data;

@Data
public class UserModelFieldVo {
    private int    id;
    private int    modelId;
    private int    type;
        private String typeName;
    private String name;
    private String fieldName;
    private String options;
    private String defaultValue;
}
