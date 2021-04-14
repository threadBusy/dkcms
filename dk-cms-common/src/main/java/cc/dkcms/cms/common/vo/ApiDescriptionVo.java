package cc.dkcms.cms.common.vo;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ApiDescriptionVo {
    private String              id;
    private String              name;
    private String              apiUrl;
    @Builder.Default
    private Map<String, String> inputParameters = new HashMap<>();
    @Builder.Default
    private Map<String, String> outputData      = new HashMap<>();

    private String remark;
}
