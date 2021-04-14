package cc.dkcms.cms.template.render;


import cc.dkcms.cms.common.render.TemplateType;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class DkCmsRenderTemplate {
    private String       templateName;
    private String       templateBase;
    private String       templateFile;
    private TemplateType templateType;

    public String getFullPath() {
        return templateBase +
                ((StringUtils.endsWith(templateBase, "/") || StringUtils.startsWith(templateFile, "/")) ? "" : "/") + templateFile;
    }
}
