package cc.dkcms.cms.template.render;

import cc.dkcms.cms.common.render.TemplateType;
import cc.dkcms.cms.template.directive.front.enjoy.base.EnjoyTemplateFactory;
import cc.dkcms.cms.template.directive.front.thymeleaf.dialect.DkCmsThymeleafDialect;
import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DkCmsEngineWrapper {

    private DkCmsRenderTemplate template;
    private TemplateType        type;

    private TemplateEngine THYMELEAF_ENGINE;
    private Context        THYMELEAF_CONTEXT;

    private Engine ENJOY_ENGINE;
    private Kv     ENJOY_CONTEXT;


    public DkCmsEngineWrapper(DkCmsRenderTemplate dkCmsRenderTemplate) {
        this.template = dkCmsRenderTemplate;
        this.type = dkCmsRenderTemplate.getTemplateType();

        if (TemplateType.THYMELEAF.equals(type)) {
            __initThymeleafEngine();
        } else if (TemplateType.ENJOY.equals(type)) {
            __initEnjoyEngine();
        } else {
            log.error("templateType unknown:" + template);
        }
    }

    public void setDate(DkCmsRenderContext renderContext) {

        Map<String, Object> data = renderContext.getDataForRender();
        data.put("templatePath", "/template/" + template.getTemplateName() + "/");
        if (TemplateType.THYMELEAF.equals(type)) {
            THYMELEAF_CONTEXT.setVariables(data);
        } else if (TemplateType.ENJOY.equals(type)) {
            ENJOY_CONTEXT.set(data);

        } else {
            log.error("templateType unknown:" + template);
        }

    }

    public String render() {

        if (TemplateType.THYMELEAF.equals(type)) {
            return THYMELEAF_ENGINE.process(
                    template.getTemplateFile(),
                    THYMELEAF_CONTEXT);
        } else if (TemplateType.ENJOY.equals(type)) {

            Template enjoyTemplate = ENJOY_ENGINE.getTemplate(template.getFullPath());
            return enjoyTemplate.renderToString(ENJOY_CONTEXT);

        } else {
            log.error("templateType unknown:" + template);
        }
        return "render Result:" + template.getTemplateFile();

    }


    private void __initThymeleafEngine() {
        THYMELEAF_ENGINE = new TemplateEngine();


        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(template.getTemplateBase() + "/");
        resolver.setSuffix(".html");
        resolver.setCacheable(false);

        THYMELEAF_CONTEXT = new Context();
        THYMELEAF_ENGINE.setTemplateResolver(resolver);


        Set<IDialect> dialectSet = new HashSet<>();

        dialectSet.add(new StandardDialect());
        dialectSet.add(new DkCmsThymeleafDialect());


        THYMELEAF_ENGINE.setDialects(dialectSet);

    }

    private void __initEnjoyEngine() {
        ENJOY_ENGINE = EnjoyTemplateFactory.get();
        ENJOY_CONTEXT = new Kv();

    }
}
