package cc.dkcms.cms.template.directive.front.enjoy.base;

import cc.dkcms.cms.template.directive.common.DirectiveGetSetting;
import cc.dkcms.cms.template.directive.front.enjoy.*;
import com.jfinal.kit.StrKit;
import com.jfinal.template.Engine;

public class EnjoyTemplateFactory {

    public static Engine get() {
        Engine ENJOY_ENGINE = new Engine();
        ENJOY_ENGINE.setDevMode(true);
        ENJOY_ENGINE.addSharedMethod(StrKit.class);
        ENJOY_ENGINE.addDirective("getSetting", DirectiveGetSetting.class);


        //@Deprecated
        ENJOY_ENGINE.addDirective("contentList", ArticleList.class);

        ENJOY_ENGINE.addDirective("articleList", ArticleList.class);
        ENJOY_ENGINE.addDirective("articleDetail", ArticleDetail.class);
        ENJOY_ENGINE.addDirective("articleRank", ArticleRank.class);
        ENJOY_ENGINE.addDirective("articleRelevant", ArticleRelevant.class);
        ENJOY_ENGINE.addDirective("articleUrl", ArticleUrl.class);

        ENJOY_ENGINE.addDirective("ad", Ad.class);
        ENJOY_ENGINE.addDirective("breadcrumb", Breadcrumb.class);
        ENJOY_ENGINE.addDirective("mianbaoxie", Breadcrumb.class);
        ENJOY_ENGINE.addDirective("pageNav", PageNav.class);
        ENJOY_ENGINE.addDirective("homePageUrl", HomePageUrl.class);

        ENJOY_ENGINE.addDirective("categoryList", CategoryList.class);
        ENJOY_ENGINE.addDirective("categoryDetail", CategoryDetail.class);
        ENJOY_ENGINE.addDirective("categoryUrl", CategoryUrl.class);

        ENJOY_ENGINE.addDirective("tagArticle", TagArticleList.class);
        ENJOY_ENGINE.addDirective("tagList", TagList.class);


        ENJOY_ENGINE.addDirective("pageList", SinglePageList.class);
        ENJOY_ENGINE.addDirective("pageUrl", SinglePageUrl.class);

        // map param
        ENJOY_ENGINE.addDirective("commentList", CommentList.class);

        return ENJOY_ENGINE;
    }
}
