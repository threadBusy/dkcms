package cc.dkcms.cms.service;

import cc.dkcms.cms.dao.DaoTag;
import cc.dkcms.cms.common.define.TagType;
import cc.dkcms.cms.service.converter.TagConverter;
import cc.dkcms.cms.common.vo.TagVo;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

public class ServiceTag {


    TagConverter tagConverter = new TagConverter();

    public List<TagVo> getListByType(TagType type) {
        List<DaoTag> list = DaoTag.dao.getListByType(type.getCode());
        return tagConverter.convertList(list);


    }


    public Page<TagVo> getPage(Integer pageNo, Integer pageSize) {
        Page<DaoTag> page = DaoTag.dao.getPage(pageNo, pageSize);

        return tagConverter.convertPage(page);

    }


    public boolean saveTag(DaoTag tagEntity) {
        if (tagEntity == null) {
            return false;
        }
        if (tagEntity.getId() != null && tagEntity.getId() > 0) {
            return tagEntity.update();
        }
        return tagEntity.save();
    }


    public TagVo getTag(Integer id) {
        return tagConverter.convert(DaoTag.dao.findById(id));
    }


    public boolean delete(Integer id) {
        DaoTag daoTag = new DaoTag();
        daoTag.setId(id);
        return daoTag.delete();
    }

}
