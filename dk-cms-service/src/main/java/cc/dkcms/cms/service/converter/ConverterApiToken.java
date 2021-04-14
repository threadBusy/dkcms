package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.common.util.BeanCopyUtils;
import cc.dkcms.cms.common.vo.ApiTokenVo;
import cc.dkcms.cms.dao.DaoApiToken;

public class ConverterApiToken extends Do2VoConverter<DaoApiToken, ApiTokenVo> {
    @Override
    public ApiTokenVo convert(DaoApiToken dataObject) {
        ApiTokenVo vo = new ApiTokenVo();
        BeanCopyUtils.copy(dataObject, vo);
        return vo;
    }
}
