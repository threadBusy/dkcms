package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.dao.DaoSinglePage;
import cc.dkcms.cms.common.util.BeanCopyUtils;
import cc.dkcms.cms.common.vo.SinglePageVo;

public class ConverterSinglePage extends Do2VoConverter<DaoSinglePage, SinglePageVo> {
    @Override
    public SinglePageVo convert(DaoSinglePage dataObject) {
        SinglePageVo vo = new SinglePageVo();
        BeanCopyUtils.copy(dataObject, vo);
        return vo;
    }
}
