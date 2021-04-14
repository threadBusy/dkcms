package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.RandomStringUtils;

public class DirectiveAdminRichText extends AbstractBaseDirective {

    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        String name         = getString(0, scope);
        String contentValue = getString(1, scope);
        String id           = "editor" + RandomStringUtils.randomAlphabetic(6);


        final String html = "<!--cms:richText-->\n" +
                "<script src=\"/assets/vendor/ckeditor/ckeditor.js\"></script>\n" +
                "<textarea id='" + name + "' name='" + name + "'>" + contentValue + "</textarea>\n" +
                "<script type='text/javascript'>\n" +
                "   var " + id + " = CKEDITOR.replace('" + name + "',{'height':500});\n" +
                "   " + id + ".on('change', function(ev) {\n" +
                "        document.getElementById('" + name + "').innerHTML = " + id + ".getData();\n" +
                "   });" +
                "</script>\n" +
                "<!--cms:richText-->\n";

        write(writer, html);
    }
}
