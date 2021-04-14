package cc.dkcms.cms.service.converter;

import cc.dkcms.cms.common.util.BeanCopyUtils;
import cc.dkcms.cms.common.vo.AccountSimpleVo;
import cc.dkcms.cms.common.vo.CommentVo;
import cc.dkcms.cms.dao.DaoAccount;
import cc.dkcms.cms.dao.DaoComment;
import com.jfinal.ext.kit.DateKit;

import java.util.HashMap;
import java.util.Map;

public class ConverterComment extends Do2VoConverter<DaoComment, CommentVo> {


    public Map<Integer, AccountSimpleVo> accountInfo = new HashMap<>();


    @Override
    public CommentVo convert(DaoComment dataObject) {
        CommentVo vo = new CommentVo();
        BeanCopyUtils.copy(dataObject, vo);

        vo.setPublishAtStr(DateKit.toStr(dataObject.getPublishAt(), "yyyy-MM-dd HH:mm"));

        Integer accountId = vo.getAccountId();
        if (accountInfo.containsKey(accountId)) {
            vo.setLogo(accountInfo.get(accountId).getLogo());
            vo.setNickname(accountInfo.get(accountId).getUsername());
        } else {
            DaoAccount daoAccount = DaoAccount.dao.findById(accountId);
            vo.setLogo(daoAccount.getLogo());
            vo.setNickname(daoAccount.getUsername());

            AccountSimpleVo accountSimpleVo = new AccountSimpleVo();
            accountSimpleVo.setLogo(daoAccount.getLogo());
            accountSimpleVo.setUsername(daoAccount.getUsername());
            accountSimpleVo.setTelephone(daoAccount.getTelephone());
            accountSimpleVo.setId(accountId);
            accountInfo.put(accountId, accountSimpleVo);
        }
        return vo;
    }
}
