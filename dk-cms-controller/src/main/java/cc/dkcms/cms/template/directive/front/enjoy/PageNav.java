package cc.dkcms.cms.template.directive.front.enjoy;


import cc.dkcms.cms.common.util.PageNavUtils;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.template.directive.front.enjoy.base.BaseEnjoyTemplateDirective;
import cc.dkcms.cms.template.render.DkCmsRenderContext;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PageNav extends BaseEnjoyTemplateDirective {

    @Override
    public void exec(Env env, Scope scope, Writer writer) {


        // 调用pageNav之前应该调用过articleList,否着这里 获取不到pageInfo
        // 这个pageInfo 不是内置的，是articleList标签设置的
        // 所有，只有在articleList之后，才能调用这个pageInfo
        DkCmsRenderContext dkCmsRenderContext = getDkCmsRenderContext(scope);
        Page<ArticleVo>    pageInfo           = dkCmsRenderContext.getArticleListPageInfo();

        if (pageInfo == null || pageInfo.getList().size() == 0) {
            failMsg(writer, "get pageInfo empty");
            return;
        }

        int currentPage = pageInfo.getPageNumber();

        if (currentPage < 1) {
            currentPage = 1;
        }

        if (pageInfo.getTotalPage() == 1) {
            failMsg(writer, "only 1 page");
            return;
        }

        CategoryVo categoryVo = dkCmsRenderContext.getCategoryVo();
        if (categoryVo == null) {
            failMsg(writer, "categoryVo is null in pageNav");
            return;
        }


        write(writer,
                PageNavUtils.pageNav(
                        pageInfo.getTotalPage(),
                        currentPage,
                        2,
                        categoryVo
                )
        );
        dkCmsRenderContext.setFlagNeedMakeList(true);
    }
}