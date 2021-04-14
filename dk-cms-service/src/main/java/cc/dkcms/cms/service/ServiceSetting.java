package cc.dkcms.cms.service;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.dao.DaoSetting;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceSetting {


    public static Boolean getBool(String key, Boolean def) {
        DaoSetting setting = DaoSetting.dao.getByKey(key);
        if (setting == null) {
            return def;
        }
        try {
            return Boolean.valueOf(setting.getSvalue());
        } catch (Exception e) {
            e.printStackTrace();
            return def;
        }

    }

    public Result setValue(String key, String value) {
        if (StringUtils.isEmpty(key)) {
            return Result.err("key empty");
        }

        DaoSetting entity = DaoSetting.dao.getByKey(key);
        if (entity == null) {
            entity = new DaoSetting();
            entity.setSkey(key);
            entity.setSvalue(value);
            entity.setSgroup("default");
            entity.setName(key);
            entity.save();
        } else {
            entity.setSvalue(value);
            update(entity);
        }
        return Result.success();
    }

    public DaoSetting getById(Integer id) {
        return DaoSetting.dao.findById(id);
    }

    public String getValue(String key) {
        if (StringUtils.isEmpty(key)) {
            return "";
        }

        DaoSetting entity = DaoSetting.dao.getByKey(key);
        if (entity == null) {
            return "";
        }
        if (StringUtils.isEmpty(entity.getSvalue())) {
            return entity.getSdefault();
        }
        return entity.getSvalue();
    }

    public List<String> getGroup() {
        List<String>     groupList  = new ArrayList<>();
        List<DaoSetting> entityList = DaoSetting.dao.getAllDistinctByGroup();
        if (entityList == null || entityList.size() == 0) {
            return groupList;
        }
        for (DaoSetting entity : entityList) {
            groupList.add(entity.getSgroup());
        }
        return groupList;
    }

    public List<DaoSetting> getAll() {
        return DaoSetting.dao.findAll();

    }

    public boolean insert(DaoSetting settingEntity) {
        return settingEntity.save();
    }

    public boolean update(DaoSetting settingEntity) {
        return settingEntity.update();
    }

    public Map<String, String> getForContext() {

        Map<String, String> ret = new HashMap<>();
        List<DaoSetting>    all = getAll();
        for (DaoSetting entity : all) {
            String val = entity.getSvalue();
            ret.putIfAbsent(
                    entity.getSkey(),
                    StringUtils.isEmpty(val) ? entity.getSdefault() : val
            );

        }
        return ret;
    }


}
