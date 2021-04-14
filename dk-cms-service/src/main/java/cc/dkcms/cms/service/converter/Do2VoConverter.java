package cc.dkcms.cms.service.converter;

import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Do2VoConverter<DO, VO> {
    public abstract VO convert(DO dataObject);


    boolean copy(DO d, VO v) {
        try {
            BeanUtils.copyProperties(v, d);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Page<VO> convertPage(Page<DO> pageDo) {
        Page<VO> pageVo = new Page<>();
        pageVo.setList(convertList(pageDo.getList()));
        pageVo.setPageSize(pageDo.getPageSize());
        pageVo.setTotalRow(pageDo.getTotalRow());
        pageVo.setTotalPage(pageDo.getTotalPage());
        pageVo.setPageNumber(pageDo.getPageNumber());
        return pageVo;

    }

    public List<VO> convertList(List<DO> list) {
        List<VO> voList = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return voList;
        }
        for (DO d : list) {
            voList.add(convert(d));
        }
        return voList;
    }
}