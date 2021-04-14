package cc.dkcms.cms.common.define;

import lombok.Data;

@Data
public class CmsVariableHolder {

    String queryString;
    String requestUri;
    Object debugToken;
    Object userInfo;

}
