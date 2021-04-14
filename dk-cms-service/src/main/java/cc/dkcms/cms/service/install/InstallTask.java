package cc.dkcms.cms.service.install;

import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.InstallParamVo;

import java.util.function.Function;

public abstract class InstallTask implements Function<InstallParamVo, Result> {

    abstract public String getSuccessMsg(Result result);

    abstract public String getFailMsg(Result result);

}
