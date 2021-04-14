package cc.dkcms.cms.service;

import cc.dkcms.cms.common.util.BeanCopyUtils;
import cc.dkcms.cms.common.vo.UserModelFieldVo;
import cc.dkcms.cms.common.vo.UserModelVo;
import cc.dkcms.cms.dao.DaoModel;
import cc.dkcms.cms.dao.DaoModelField;
import cc.dkcms.cms.service.converter.ModelConverter;
import cc.dkcms.cms.service.converter.ModelFieldConverter;

import java.util.LinkedList;
import java.util.List;

public class ServiceUserModel {

    ModelFieldConverter fieldConverter = new ModelFieldConverter();
    ModelConverter      modelConverter = new ModelConverter();


    public List<UserModelVo> getAllModel() {

        return modelConverter.convertList(DaoModel.dao.findAll());
    }

    /*
    private UserModelVo entity2Vo(DaoModel entity) {
        UserModelVo vo = new UserModelVo();
        BeanCopyUtils.copy(entity, vo);
        vo.setFieldVoList(getFieldVoList(entity.getId()));
        return vo;
    }*/

    public boolean checkExist(String name) {
        return DaoModel.dao.checkExist(name);
    }

    public boolean save(DaoModel entity) {
        if (entity.getId() != null) {
            return entity.update();
        }
        return entity.save();
    }

    public DaoModel getEntityById(Integer id) {
        return DaoModel.dao.findById(id);
    }

    public Integer getIdByName(String name) {
        return DaoModel.dao.getIdByName(name);
    }



    public UserModelVo getVoById(Integer id) {
        DaoModel entity = getEntityById(id);
        if (entity == null) {
            return null;
        }
        return modelConverter.convert(entity);
    }

    public List<UserModelFieldVo> getFieldVoList(Integer modelId) {
        List<UserModelFieldVo> list = new LinkedList<>();


        List<DaoModelField> entityList = DaoModelField.dao.getListByModelId(modelId);
        if (entityList == null) {
            return list;
        }

        for (DaoModelField entity : entityList) {
            list.add(fieldConverter.convert(entity));
        }
        return list;
    }

    public boolean saveField(DaoModelField entity) {
        if (entity.getId() == null || entity.getId() == 0) {
            return entity.save();
        }
        return entity.update();
    }

    public UserModelFieldVo getFieldVo(Integer fieldId) {
        return fieldConverter.convert(DaoModelField.dao.findById(fieldId));
    }

    public void deleteField(Integer fieldId) {
        DaoModelField field = new DaoModelField();
        field.setId(fieldId);
        field.delete();
    }

}

