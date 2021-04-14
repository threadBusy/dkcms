package cc.dkcms.cms.service;

import cc.dkcms.cms.common.define.ArticleRankType;
import cc.dkcms.cms.common.define.ConstArticleDelete;
import cc.dkcms.cms.common.define.ContentListQueryParam;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.ArticleVo;
import cc.dkcms.cms.dao.DaoArticle;
import cc.dkcms.cms.service.converter.ConverterContent;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

public class ServiceContent {


    private final ServiceCategory serviceCategory = Aop.get(ServiceCategory.class);

    private final ConverterContent converterContent = Aop.get(ConverterContent.class);


    public void click(Integer id) {
        DaoArticle.dao.click(id);
    }


    public Integer getTotalPage(Integer categoryId, Integer pageSize, boolean includeChildCategory) {

        return DaoArticle.dao.getTotalPage(categoryId, pageSize, includeChildCategory);
    }

    public Page<ArticleVo> getPage(Integer categoryId, Integer page) {
        ContentListQueryParam queryParam = ContentListQueryParam.builder().build();
        queryParam.getIdList().add(categoryId);
        queryParam.setPage(page);
        queryParam.setIncludeSubCate(false);
        return getPage(queryParam);

    }

    public Page<ArticleVo> getPage(ContentListQueryParam queryParam) {

        if (queryParam == null) {
            return new Page<>();
        }
        if (queryParam.getIncludeSubCate()) {

            if (queryParam.getIdList() != null && queryParam.getIdList().size() == 1) {
                // 递归获取所有 子栏目cid
                Integer       categoryId     = queryParam.getIdList().get(0);
                List<Integer> categoryIdList = new ArrayList<>();
                categoryIdList.add(categoryId);
                categoryIdList.addAll(serviceCategory.getChildIdList(categoryId, true));
                queryParam.getIdList().addAll(categoryIdList);
            }

        }


        return converterContent.convertPage(
                DaoArticle.dao.getArticleByQueryParam(queryParam)
        );
    }


    public boolean save(DaoArticle daoArticle) {
        if (daoArticle == null) {
            return false;
        }
        if (daoArticle.getId() == null) {
            serviceCategory.incArticleNum(daoArticle.getCategoryId());
            return daoArticle.save();
        } else {
            return daoArticle.update();
        }
    }


    public ArticleVo getArticleVo(Integer articleId) {

        return converterContent.convert(DaoArticle.dao.findById(articleId));
    }


    // 进入回收站
    public Result toggleRecycleStatus(Integer id) {
        DaoArticle articleEntity = DaoArticle.dao.findById(id);
        if (articleEntity == null) {
            return Result.err("id missing:" + id);
        }

        int target = ConstArticleDelete.ARTICLE_DELETE_TRUE;
        if (ConstArticleDelete.ARTICLE_DELETE_TRUE == articleEntity.getIsDelete()) {
            target = ConstArticleDelete.ARTICLE_DELETE_FALSE;
        }
        Integer categoryId = articleEntity.getCategoryId();

        articleEntity.clear();
        articleEntity.setId(id);
        articleEntity.setIsDelete(target);

        if (articleEntity.update()) {
            if (target == ConstArticleDelete.ARTICLE_DELETE_TRUE) {
                serviceCategory.desArticleNum(categoryId);
            } else {
                serviceCategory.incArticleNum(categoryId);

            }
            return Result.success();
        }
        return Result.err("dao.update return 0");
    }


    // 草稿箱移出
    public Result draftPublish(Integer id) {
        DaoArticle articleEntity = DaoArticle.dao.findById(id);
        if (articleEntity == null) {
            return Result.err("id missing:" + id);
        }
        articleEntity.clear();
        articleEntity.setId(id);
        articleEntity.setIsDelete(ConstArticleDelete.ARTICLE_DELETE_TRUE);
        if (articleEntity.update()) {
            return Result.success();
        }
        return Result.err("db error");
    }


    public int getTotalNum() {
        return DaoArticle.dao.getTotalNum();
    }


    public Page<ArticleVo> getPageByCategoryIdWithOutSubCat(Integer page, int pageSize, Integer categoryId) {
        ContentListQueryParam queryParam = ContentListQueryParam.builder()
                .page(page)
                .pageSize(pageSize)
                .isDraft(false)
                .orderBy("id")
                .orderSort("desc")
                .includeSubCate(false)
                .build();
        queryParam.getIdList().add(categoryId);
        return __commentGetPage(queryParam);

    }

    public Page<ArticleVo> getPageByCategoryIdWithSubCat(Integer page, int pageSize, Integer categoryId) {
        ContentListQueryParam queryParam = ContentListQueryParam.builder()
                .page(page)
                .pageSize(pageSize)
                .isDraft(false)
                .orderBy("id")
                .orderSort("desc")
                .includeSubCate(true)
                .build();

        // 递归获取所有 子栏目cid
        List<Integer> categoryIdList = new ArrayList<>();
        categoryIdList.add(categoryId);
        categoryIdList.addAll(serviceCategory.getChildIdList(categoryId, true));
        queryParam.getIdList().addAll(categoryIdList);

        return __commentGetPage(queryParam);

    }


    public Page<ArticleVo> getRecyclePage(Integer page, Integer pageSize) {

        ContentListQueryParam listQueryParam = ContentListQueryParam.builder()
                .page(page)
                .pageSize(pageSize)
                .isDelete(ConstArticleDelete.ARTICLE_DELETE_TRUE)
                .build();
        return __commentGetPage(listQueryParam);

    }

    public Page<ArticleVo> getDraftPage(Integer page, Integer pageSize) {
        ContentListQueryParam listQueryParam = ContentListQueryParam.builder()
                .page(page)
                .pageSize(pageSize)
                .isDraft(true)
                .build();

        return __commentGetPage(listQueryParam);

    }

    private Page<ArticleVo> __commentGetPage(ContentListQueryParam queryParam) {
        return converterContent.convertPage(DaoArticle.dao.getArticleByQueryParam(queryParam));

    }


    public List<ArticleVo> getRankList(
            ArticleRankType articleRankType,
            Integer pageSize,
            Integer categoryId,
            Boolean withChild) {

        ContentListQueryParam listQueryParam = ContentListQueryParam.builder()
                .page(1)
                .pageSize(pageSize)
                .build();
        listQueryParam.getIdList().add(categoryId);

        if (withChild) {
            // 递归获取所有 子栏目cid
            List<Integer> categoryIdList = new ArrayList<>();
            categoryIdList.addAll(serviceCategory.getChildIdList(categoryId, true));
            listQueryParam.getIdList().addAll(categoryIdList);
        }


        if (ArticleRankType.HOT.equals(articleRankType)) {
            listQueryParam.setOrderBy("click");
        } else if (ArticleRankType.LATEST.equals(articleRankType)) {
            listQueryParam.setOrderBy("publish_date");

        } else {
            listQueryParam.setOrderBy("id");
            listQueryParam.setIsRecommend(true);
        }


        Page<ArticleVo> pageInfo = __commentGetPage(listQueryParam);
        return pageInfo.getList();

    }

    public Page<ArticleVo> getListByKeyword(String keyword, Integer page) {
        ContentListQueryParam listQueryParam = ContentListQueryParam.builder()
                .page(page)
                .pageSize(20)
                .isNeedDealKeyword(true)
                .isKeywordLikeSearch(true)
                .keywordField("title")
                .keyword(keyword)
                .build();
        return __commentGetPage(listQueryParam);
    }

    public List<ArticleVo> getListByTagId(Integer tagId) {
        ContentListQueryParam listQueryParam = ContentListQueryParam.builder()
                .page(1)
                .pageSize(20)
                .tagId(tagId)
                .build();
        return converterContent.convertList(DaoArticle.dao.getListByQueryParam(listQueryParam));
    }

    public void setNeighborContent(ArticleVo articleVo) {
        if (articleVo == null) {
            return;
        }
        articleVo.setPreviousArticle(getPrevious(articleVo.getId(), articleVo.getCategoryId()));
        articleVo.setNextArticle(getNext(articleVo.getId(), articleVo.getCategoryId()));
    }

    private ArticleVo getPrevious(Integer id, Integer categoryId) {
        return converterContent.convert(DaoArticle.dao.getPrevious(id, categoryId));
    }

    private ArticleVo getNext(Integer id, Integer categoryId) {
        return converterContent.convert(DaoArticle.dao.getNext(id, categoryId));


    }

    // todo
    public List<ArticleVo> getRelevantList(Integer articleId, Integer limit) {
        return converterContent.convertList(DaoArticle.dao.getRelevantList(articleId, limit));
    }


}
