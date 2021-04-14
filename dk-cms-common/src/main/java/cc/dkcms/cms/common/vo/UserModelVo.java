package cc.dkcms.cms.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserModelVo {
    private Integer id;

    private String modelName;

    private List<UserModelFieldVo> fieldVoList;
}
