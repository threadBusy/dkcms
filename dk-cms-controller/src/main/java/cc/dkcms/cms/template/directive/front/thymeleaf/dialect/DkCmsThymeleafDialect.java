package cc.dkcms.cms.template.directive.front.thymeleaf.dialect;

import cc.dkcms.cms.template.directive.front.thymeleaf.*;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashSet;
import java.util.Set;

public class DkCmsThymeleafDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {
    private static final String DIALECT_NAME   = "Cms PageGeneratorDialect";
    private static final String DIALECT_PREFIX = "cms";

    public DkCmsThymeleafDialect() {

        super(DIALECT_NAME, DIALECT_PREFIX, StandardDialect.PROCESSOR_PRECEDENCE);

    }

    public Set<IProcessor> getProcessors(final String dialectPrefix) {

        Set<IProcessor> processors = new HashSet<>();
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        processors.add(new CategoryList(dialectPrefix));
        processors.add(new CategoryDetail(dialectPrefix));
        processors.add(new ArticleList(dialectPrefix));
        processors.add(new ArticleRank(dialectPrefix));
        processors.add(new ArticleDetail(dialectPrefix));
        processors.add(new Navbar(dialectPrefix));
        processors.add(new PageNav(dialectPrefix));
        processors.add(new Tag(dialectPrefix));
        processors.add(new TagList(dialectPrefix));
        processors.add(new SinglePageUrl(dialectPrefix));
        processors.add(new SinglePageList(dialectPrefix));
        processors.add(new CategoryUrl(dialectPrefix));
        processors.add(new ArticleUrl(dialectPrefix));
        processors.add(new HomePageUrl(dialectPrefix));

        return processors;
    }

    private IExpressionObjectFactory expressionObjectFactory = null;

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        if (this.expressionObjectFactory == null) {
            this.expressionObjectFactory = new MyExpressionObjectFactory();
        }
        return this.expressionObjectFactory;
    }


}