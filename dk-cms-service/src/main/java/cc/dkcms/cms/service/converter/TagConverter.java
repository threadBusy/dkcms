package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.common.define.TagType;
import cc.dkcms.cms.common.vo.TagVo;
import cc.dkcms.cms.dao.DaoTag;
import cc.dkcms.cms.service.ServiceCategory;
import cc.dkcms.cms.service.ServiceContent;
import com.jfinal.aop.Aop;

public class TagConverter extends Do2VoConverter<DaoTag, TagVo> {

    private final ServiceCategory serviceCategory = Aop.get(ServiceCategory.class);
    private final ServiceContent  serviceContent  = Aop.get(ServiceContent.class);

    @Override
    public TagVo convert(DaoTag entity) {
        if (entity == null) {
            return null;
        }
        TagVo vo = new TagVo();
        copy(entity, vo);


        if (TagType.CATEGORY.getCode() == entity.getTagType()) {
            vo.setCategoryVoList(serviceCategory.getCategoryListByTagId(entity.getId()));
        } else if (TagType.ARTICLE.getCode() == entity.getTagType()) {
            vo.setArticleVoList(serviceContent.getListByTagId(entity.getId()));
        }

        return vo;
    }
}
