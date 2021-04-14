package cc.dkcms.cms.common.define;

import lombok.Data;

@Data
public class RequestVar {
    private String fullUrl;
    private String requestUri;
    private String queryString;
}
