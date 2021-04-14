package cc.dkcms.cms.template.directive.admin;

import cc.dkcms.cms.template.directive.base.AbstractBaseDirective;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class DirectiveAdminFileUploader extends AbstractBaseDirective {
    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        String name         = String.valueOf(exprList.getFirstExpr().eval(scope));
        String contentValue = getString(1, scope);

        if (contentValue == null) {
            contentValue = "";
        }


        String       id   = RandomStringUtils.randomAlphabetic(8);
        List<String> list = new LinkedList<>();
        list.add("");
        list.add("          <!-----fileUploadStart----->                                                    ");
        list.add("          <div class='dkcms-file-uploader' id='" + id + "'>                                     ");
        list.add("              <input type='file' class='hidden'>                                          ");
        list.add("              <input type='hidden' name='" + name + "'  value='" + contentValue + "'>      ");
        list.add("              <div class='btn_01 uploader-btn'> +  添加图片</div>                            ");
        list.add("              <img class='uploader-img hidden' src='' alt=''/>                             ");
        list.add("              <div class='uploader-txt hidden'></div>                                      ");
        list.add("              <div class='uploader-update-btn hidden btn_01'>重新上传</div>                 ");
        list.add("          </div>                                                                           ");
        list.add("          <!-----fileUploadEnd----->                                                       ");
        list.add("");

        write(writer, StringUtils.join(list, "\n"));
    }
}
