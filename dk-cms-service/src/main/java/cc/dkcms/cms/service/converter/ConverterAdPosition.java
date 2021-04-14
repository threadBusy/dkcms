package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.common.vo.AdPositionVo;
import cc.dkcms.cms.dao.DaoAdPosition;

public class ConverterAdPosition extends Do2VoConverter<DaoAdPosition, AdPositionVo> {

    @Override
    public AdPositionVo convert(DaoAdPosition dataObject) {
        AdPositionVo vo = new AdPositionVo();
        copy(dataObject, vo);
        return vo;
    }
}
