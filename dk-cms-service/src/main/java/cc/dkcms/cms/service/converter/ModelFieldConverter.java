package cc.dkcms.cms.service.converter;


import cc.dkcms.cms.common.define.UserModelFieldType;
import cc.dkcms.cms.common.vo.UserModelFieldVo;
import cc.dkcms.cms.dao.DaoModelField;

public class ModelFieldConverter extends Do2VoConverter<DaoModelField, UserModelFieldVo> {

    public UserModelFieldVo convert(DaoModelField entity) {
        if (entity == null) {
            return null;
        }
        UserModelFieldVo vo = new UserModelFieldVo();
        copy(entity, vo);
        vo.setTypeName(UserModelFieldType.getNameByCode(entity.getType()));
        return vo;
    }
}
