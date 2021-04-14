package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.common.util.BeanCopyUtils;
import cc.dkcms.cms.common.vo.UserModelVo;
import cc.dkcms.cms.dao.DaoModel;
import cc.dkcms.cms.dao.DaoModelField;

import java.util.List;

public class ModelConverter extends Do2VoConverter<DaoModel, UserModelVo> {


    // 防止互相依赖
    ModelFieldConverter modelFieldConverter = new ModelFieldConverter();


    @Override
    public UserModelVo convert(DaoModel dataObject) {
        UserModelVo vo = new UserModelVo();
        BeanCopyUtils.copy(dataObject, vo);

        List<DaoModelField> listField = DaoModelField.dao.getListByModelId(dataObject.getId());
        vo.setFieldVoList(modelFieldConverter.convertList(listField));
        return vo;
    }
}
