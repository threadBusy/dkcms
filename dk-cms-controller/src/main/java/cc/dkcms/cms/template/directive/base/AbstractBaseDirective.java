package cc.dkcms.cms.template.directive.base;

import com.jfinal.template.Directive;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
abstract public class AbstractBaseDirective extends Directive {


    protected void write(Writer writer, String msg) {
        try {
            writer.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void failMsg(Writer writer, String msg) {
        write(writer, "<!--" + msg + "-->");
    }


    /**
     * 以下 get 系列函数，在前台模板指令中不能使用
     *
     * @param index
     * @param scope
     * @param def
     * @return
     */

    protected Boolean getBool(Integer index, Scope scope, Boolean def) {
        try {
            String val = getString(index, scope);
            if (val == null) {
                return def;
            }
            return Boolean.valueOf(val);
        } catch (Exception e) {
            log.error("catch exception in AbstractBaseDirective.getInt " + e.getMessage());
            return def;
        }
    }

    protected Integer getInt(Integer index, Scope scope) {
        return getInt(index, scope, 0);
    }

    protected Integer getInt(Integer index, Scope scope, Integer def) {
        try {
            return Integer.valueOf(getString(index, scope));
        } catch (Exception e) {
            log.error("catch exception in AbstractBaseDirective.getInt " + e.getMessage());
            return def;
        }
    }


    protected String getString(Integer index, Scope scope, String def) {
        try {
            Expr expr = exprList.getExpr(index);
            if (expr == null) {
                return def;
            }
            Object val = expr.eval(scope);
            if (val == null) {
                return def;
            }
            return String.valueOf(val);
        } catch (Exception e) {
            log.error("catch exception in AbstractBaseDirective.getString " + e.getMessage());

            return def;
        }
    }

    protected String getString(Integer index, Scope scope) {
        return getString(index, scope, null);
    }

    protected Integer getInt(Object object) {
        return getInt(object, 0);
    }

    protected Integer getInt(Object object, Integer def) {

        if (object == null) {
            return def;
        }

        if (def == null) {
            def = 0;
        }
        try {
            return Integer.valueOf(String.valueOf(object));
        } catch (Exception e) {
            return def;
        }
    }
}
