package cc.dkcms.cms.service;

import com.jfinal.plugin.activerecord.Page;

public interface BaseService<M> {

    Page<M> getListByPage(Integer pageNo, Integer pageSize);

}
