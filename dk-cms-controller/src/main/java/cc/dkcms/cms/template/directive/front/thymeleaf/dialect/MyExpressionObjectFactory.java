package cc.dkcms.cms.template.directive.front.thymeleaf.dialect;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MyExpressionObjectFactory implements IExpressionObjectFactory {
    public static final String CMS_EXPRESSION_OBJECT_NAME = "utils";


    public static final Set<String> ALL_EXPRESSION_OBJECT_NAMES;


    static {

        final Set<String> allExpressionObjectNames = new LinkedHashSet<String>();
        allExpressionObjectNames.add(CMS_EXPRESSION_OBJECT_NAME);

        ALL_EXPRESSION_OBJECT_NAMES = Collections.unmodifiableSet(allExpressionObjectNames);

    }
    public MyExpressionObjectFactory() {
        super();
    }





    public Set<String> getAllExpressionObjectNames() {
        return ALL_EXPRESSION_OBJECT_NAMES;
    }



    public Object buildObject(final IExpressionContext context, final String expressionObjectName) {


        if (CMS_EXPRESSION_OBJECT_NAME.equals(expressionObjectName)) {
            return new CmsExpressObject(context);
        }

        return new CmsExpressObject(context);

    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return false;
    }
}
