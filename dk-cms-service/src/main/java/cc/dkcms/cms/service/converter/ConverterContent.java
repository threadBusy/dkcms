package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.common.helper.UrlHelper;
import cc.dkcms.cms.common.util.CmsUtils;
import cc.dkcms.cms.common.util.JsonUtil;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.dao.DaoArticle;
import cc.dkcms.cms.service.ServiceCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jfinal.aop.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Map;


@Slf4j
public class ConverterContent extends Do2VoConverter<DaoArticle, ArticleVo> {


    @Inject
    ServiceCategory serviceCategory;

    public ArticleVo convert(DaoArticle entity) {
        if (entity == null) {
            return null;
        }
        ArticleVo vo = new ArticleVo();
        copy(entity, vo);
        CategoryVo categoryVo = serviceCategory.getCategoryById(entity.getCategoryId());

        if (categoryVo == null) {
            log.error("categoryVo is null:");
            vo.setCategoryName("错误:无类目");
        } else {
            vo.setCategoryName(categoryVo.getCategoryName());
        }
        vo.setCategoryVo(categoryVo);
        vo.setArticleUrl(
                UrlHelper.getCategoryArticleUrl(vo, categoryVo, CmsUtils.isPreviewMode())
        );


        // 输出 null fileUpload 控件受不了
        if (entity.getCoverImage() == null) {
            vo.setCoverImage("");
        }
        if (entity.getContent() == null) {
            vo.setContent("");
        }


        return vo;

    }
}
