package cc.dkcms.cms.service.po2vo;

import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;

public abstract class VoPoConverter<PO, VO> {
    abstract public VO getVo(PO po);


    public Page<VO> getVoPage(Page<PO> pagePo) {
        Page<VO> pageVo = new Page<>();

        pageVo.setPageNumber(pagePo.getPageNumber());
        pageVo.setTotalPage(pagePo.getTotalPage());
        pageVo.setTotalRow(pagePo.getTotalRow());
        pageVo.setPageSize(pagePo.getPageSize());

        List<VO> voList = new ArrayList<>();
        pagePo.getList().forEach(po -> {
            VO vo = getVo(po);
            voList.add(vo);
        });
        pageVo.setList(voList);
        return pageVo;
    }
}
