package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.common.vo.AdCategoryVo;
import cc.dkcms.cms.dao.DaoAdCategory;

public class ConverterAdCategory extends Do2VoConverter<DaoAdCategory, AdCategoryVo> {
    @Override
    public AdCategoryVo convert(DaoAdCategory dataObject) {
        AdCategoryVo vo = new AdCategoryVo();
        copy(dataObject, vo);
        return vo;
    }
}
