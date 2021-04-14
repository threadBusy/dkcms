package cc.dkcms.cms.template.directive.front.enjoy;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.util.CmsUtils;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

public class HomePageUrl extends BaseEnjoyTemplateDirective {
    private static final String ATTR_NAME = "homePageUrl";

    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        String url = CmsUtils.isPreviewMode() ? Constant.PREVIEW_URL_PATH : "/index.html";
        write(writer, url);
    }

}