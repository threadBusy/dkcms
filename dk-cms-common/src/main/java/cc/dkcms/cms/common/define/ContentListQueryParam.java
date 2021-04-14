package cc.dkcms.cms.common.define;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ContentListQueryParam {


    public ContentListQueryParam addCategoryId(Integer cid) {
        this.idList.add(cid);
        return this;
    }

    @Builder.Default
    List<Integer> idList = new ArrayList<>();

    @Builder.Default
    Integer page = 1;

    @Builder.Default
    Integer pageSize = 10;

    @Builder.Default
    String orderBy = "id";

    @Builder.Default
    String orderSort = "desc";

    @Builder.Default
    Boolean includeSubCate = false;

    @Builder.Default
    Boolean isRecommend = false;

    @Builder.Default
    Boolean isShow = true;

    @Builder.Default
    Integer isDelete = ConstArticleDelete.ARTICLE_DELETE_FALSE;

    @Builder.Default
    Boolean isDraft = false;

    @Builder.Default
    Boolean hasCoverImage = false;

    @Builder.Default
    Boolean isNeedDealKeyword = false;

    @Builder.Default
    String keyword;

    @Builder.Default
    String keywordField;

    @Builder.Default
    Boolean isKeywordLikeSearch = false;

    @Builder.Default
    Integer tagId;


    public String getWhere() {

        List<String>  sqlWhere = new ArrayList<>();
        List<Integer> idList   = getIdList();


        if (idList != null && idList.size() > 0) {
            if (idList.size() == 1) {
                sqlWhere.add(" category_id = " + idList.get(0));
            } else {
                sqlWhere.add(" category_id IN (" + StringUtils.join(idList, ",") + ")");
            }
        }

        // 草稿箱 列表
        if (getIsDraft()) {
            sqlWhere.add(" is_delete = " + ConstArticleDelete.ARTICLE_DELETE_DRAFT);
        } else {
            // 回收站，或者 普通列表
            sqlWhere.add(" is_delete = " + getIsDelete());
        }


        if (getIsRecommend()) {
            sqlWhere.add(" is_recommend = 1");
        }
        if (getHasCoverImage()) {
            sqlWhere.add(" cover_image != '' ");
        }

        if (getIsNeedDealKeyword()) {
            if (getIsKeywordLikeSearch()) {
                sqlWhere.add(
                        getKeywordField() + " LIKE '%" + getKeyword() + "%' "
                );

            } else {
                sqlWhere.add(
                        getKeywordField() + " = " + getKeyword()
                );
            }
        }

        if (getTagId() != null) {
            sqlWhere.add(" FIND_IN_SET(" + getTagId() + ",REPLACE(tags,'|',',')) > 0  ");
        }

        if (sqlWhere.size() == 0) {
            sqlWhere.add(" 1=1 ");
        }

        return StringUtils.join(sqlWhere, " AND ");

    }
}
