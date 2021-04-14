package cc.dkcms.cms.service;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.CategoryVo;
import cc.dkcms.cms.dao.DaoCategory;
import cc.dkcms.cms.service.category.CategoryVoCompare;
import cc.dkcms.cms.service.converter.CategoryConverter;
import com.jfinal.aop.Aop;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cc.dkcms.cms.common.Constant.ROOT_CATEGORY_ID;


@Slf4j
public class ServiceCategoryTree {

    // todo
    // 验证一下 Aop.get 是不是单例的，和 inject 注解的区别
    private final DaoCategory              dao       = Aop.get(DaoCategory.class);
    private final CategoryConverter        converter = Aop.get(CategoryConverter.class);
    private final Map<Integer, CategoryVo> voMap     = new ConcurrentHashMap<>();

    public ServiceCategoryTree() {
        log.debug("ServiceCategoryTree init");
        reBuild();
        log.debug("ServiceCategoryTree over");
    }

    public void reBuild() {
        voMap.clear();
        // 初始化rootNode
        voMap.put(ROOT_CATEGORY_ID,
                converter.convert(dao.getRootCategory())
        );

        // 从db中加载所有category,并组装
        long st = System.currentTimeMillis();
        log.info("ServiceCategoryTree build start");


        List<DaoCategory> allCategory = dao.findAll();
        for (DaoCategory entity : allCategory) {
            log.info("ServiceCategoryTree put into:" + entity.getId() + "|" + entity.getCategoryName());
            voMap.put(entity.getId(), converter.convert(entity));
        }
        log.info("ServiceCategoryTree category total " + voMap.size());

        for (Integer id : voMap.keySet()) {
            connectParentAndChildren(id);
        }
        for (Integer id : voMap.keySet()) {
            assembleUrl(id);
        }
        assembleLevel(voMap.get(ROOT_CATEGORY_ID));
        log.info("ServiceCategoryTree build end;time:" + (System.currentTimeMillis() - st) + "ms");
    }

    private void assembleLevel(CategoryVo vo) {
        if (vo.getId() == ROOT_CATEGORY_ID) {
            vo.setLevel(0);
        } else {
            vo.setLevel(voMap.get(vo.getParentId()).getLevel() + 1);
        }
        if (vo.getChild().size() == 0) {
            return;
        }
        vo.getChild().forEach(this::assembleLevel);
    }

    private void connectParentAndChildren(Integer id) {
        if (id == null) {
            return;
        }
        CategoryVo vo       = voMap.get(id);
        Integer    parentId = vo.getParentId();

        if (id == ROOT_CATEGORY_ID) {
            return;
        }
        try {
            // 1. 找父节点,
            CategoryVo parentVo = voMap.get(parentId);
            if (parentVo == null) {
                log.error("getVoById return null; id:" + parentId);
                parentVo = voMap.get(ROOT_CATEGORY_ID);
            }
            // 2. 将自己的 parentVo 设置好
            vo.setParentVo(parentVo);
            vo.setParentName(parentVo.getCategoryName());

            // 3. 把自己放到父节点的 children 列表中
            List<CategoryVo> children = parentVo.getChild();
            assert children != null;
            children.add(vo);
            children.sort(new CategoryVoCompare());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void assembleUrl(Integer id) {
        // do nothing
        // 瓶装url的工作,放在 vo 的ge方法里做
    }


    public List<Integer> getChildIdList(Integer categoryId, Boolean isRecursion) {
        List<CategoryVo> listVo = getChildVoList(categoryId);
        if (listVo == null || listVo.size() == 0) {
            return new ArrayList<>();
        }
        if (isRecursion == null) {
            isRecursion = false;
        }
        List<Integer> ret = new ArrayList<>();
        for (CategoryVo vo : listVo) {
            ret.add(vo.getId());
            if (isRecursion) {
                ret.addAll(getChildIdList(vo.getId(), true));
            }
        }
        return ret;

    }

    public List<CategoryVo> getChildVoList(Integer categoryId) {
        if (categoryId == null) {
            return null;
        }
        CategoryVo vo = voMap.get(categoryId);

        // 业务端会传入各种id，模板随意写，或者切换模板
        // 所以这里，一定会有vo == null 的时候
        if (vo == null) {
            return new ArrayList<>();
        }
        return vo.getChild();
    }

    public CategoryVo getVoById(Integer id) {
        if (voMap.containsKey(id)) {
            return voMap.get(id);
        }
        return null;
    }

    public CategoryVo getParentVo(Integer id) {
        if (id == null) {
            return null;
        }
        CategoryVo vo = voMap.get(id);
        if (vo == null) {
            return null;
        }
        return voMap.get(vo.getParentId());
    }

    public Result delete(Integer id) {
        CategoryVo vo = getVoById(id);
        if (vo == null) {
            return Result.err("get null by id:" + id);
        }

        CategoryVo parentVo = getParentVo(id);
        if (parentVo != null) {
            parentVo.getChild().remove(vo);
            parentVo.getChild().sort(new CategoryVoCompare());

        }

        List<Integer> allChildren = getChildIdList(id, true);
        if (allChildren != null) {
            for (Integer cid : allChildren) {
                voMap.remove(cid);
            }
        }
        return Result.success("");
    }

    public Result insert(Integer id) {
        CategoryVo newVo = converter.convert(dao.findById(id));
        voMap.put(id, newVo);
        connectParentAndChildren(id);
        assembleUrl(id);
        return Result.success();
    }

    public void incArticleNum(Integer id) {
        assert id != null;
        CategoryVo vo = voMap.get(id);
        if (vo != null) {
            vo.setTotalCount(vo.getTotalCount() + 1);
        }
    }

    public void desArticleNum(Integer id) {
        assert id != null;
        CategoryVo vo = voMap.get(id);
        if (vo != null) {
            vo.setTotalCount(vo.getTotalCount() - 1);
        }
    }

    public Result update(Integer id) {

        CategoryVo oldVo = voMap.get(id);
        if (oldVo == null) {
            return Result.err("oldVo null");
        }
        CategoryVo newVo = converter.convert(dao.findById(id));
        // 更新vo内容
        // 业务提交上来的entity 里字段不全
        voMap.replace(id, newVo);

        // 找到父亲，删除老的vo
        CategoryVo parentVo = oldVo.getParentVo();
        if (parentVo != null) {
            parentVo.getChild().remove(oldVo);
        }

        // 如果新老parentId不同，好到新的parentVo,并把自己加入
        if (!newVo.getParentId().equals(oldVo.getParentId())) {
            parentVo = voMap.get(newVo.getParentId());
        }

        // 建立和父亲的关系
        assert parentVo != null;
        assert parentVo.getChild() != null;
        parentVo.getChild().add(newVo);
        parentVo.getChild().sort(new CategoryVoCompare());
        newVo.setParentVo(parentVo);

        // 找到子元素，建立和和孩子的关系
        List<CategoryVo> children = oldVo.getChild();
        newVo.setChild(children);
        if (children != null && children.size() > 0) {
            for (CategoryVo c : children) {
                c.setParentVo(newVo);
            }
        }


        // 更新url
        assembleUrl(id);
        oldVo = null;
        return Result.success("");
    }

    public int getTotalNum() {
        return voMap.size();
    }

    public Result updateSort(Integer id, int sort) {
        CategoryVo oldVo = voMap.get(id);
        if (oldVo == null) {
            return Result.err("oldVo null");
        }
        oldVo.setSort(sort);
        return Result.success();
    }
}
