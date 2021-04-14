package cc.dkcms.cms.template.directive.front.thymeleaf;

import cc.dkcms.cms.common.define.ArticleRankType;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.service.ServiceContent;
import com.jfinal.aop.Aop;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.List;

public class ArticleRank extends AbstractPageTagProcessor {


    private static final String ATTR_NAME = "articleRank";


    public ArticleRank(final String dialectPrefix) {

        super(TemplateMode.HTML,
                dialectPrefix,
                null,
                false,
                ATTR_NAME,
                true,
                StandardDialect.PROCESSOR_PRECEDENCE,
                true);
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue,
                             IElementTagStructureHandler structureHandler) {

        String          typeString      = getAttribute(tag, "type", "");
        ArticleRankType articleRankType = ArticleRankType.getType(typeString);

        Integer pageSize   = getIntAttribute(tag, "size", 10);
        Integer categoryId = getIntAttribute(tag, "categoryId", 0);


        ServiceContent  serviceContent = Aop.get(ServiceContent.class);
        List<ArticleVo> list           = serviceContent.getRankList(articleRankType, pageSize, categoryId, true);

        // 迭代
        structureHandler.iterateElement("item", "status", list);


    }
}