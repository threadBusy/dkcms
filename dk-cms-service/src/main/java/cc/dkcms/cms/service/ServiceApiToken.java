package cc.dkcms.cms.service;

import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.common.vo.ApiTokenVo;
import cc.dkcms.cms.dao.DaoApiToken;
import cc.dkcms.cms.service.converter.ConverterApiToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
public class ServiceApiToken {

    ConverterApiToken converterApiToken = new ConverterApiToken();

    public List<ApiTokenVo> getAllToken() {

        return converterApiToken.convertList(DaoApiToken.dao.findAll());
    }

    public Result generateToken(String name, Integer days, Integer managerId) {
        DaoApiToken token = new DaoApiToken();
        token.setName(name);
        token.setToken(RandomStringUtils.randomAlphanumeric(32));
        token.setCreatedAt(new Date());
        token.setManagerId(managerId);
        LocalDateTime expiredAt;
        if (days == null || days < 0) {
            expiredAt = LocalDateTime.now().plusDays(1);
        } else if (days.equals(0)) {
            expiredAt = LocalDateTime.now().plusYears(3);
        } else {
            expiredAt = LocalDateTime.now().plusDays(days);
        }
        token.setExpiredAt(Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant()));
        token.setIsValid(Constant.TRUE);
        if (!token.save()) {
            return Result.fail("db error");
        }
        return Result.success();
    }

    public DaoApiToken getEntityByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return DaoApiToken.dao.getTokenByTokenString(token);
    }

    public boolean isTokenValid(String token) {
        DaoApiToken entity = getEntityByToken(token);
        if (entity == null) {
            return false;
        }
        System.out.println(entity);
        if (Constant.FALSE == entity.getIsValid()) {
            return false;
        }
        return entity.getExpiredAt().before(new Date());
    }


    public ApiTokenVo getVoById(Integer id) {
        return converterApiToken.convert(DaoApiToken.dao.findById(id));
    }

    public Result changeValid(Integer id) {
        DaoApiToken entity = DaoApiToken.dao.findById(id);
        if (entity == null) {
            return Result.fail("id error");
        }
        Integer validTarget = entity.getIsValid().equals(Constant.TRUE) ? Constant.FALSE : Constant.TRUE;
        entity.clear();
        entity.setId(id);
        entity.setIsValid(validTarget);
        if (entity.update()) {
            return Result.success();
        }
        return Result.fail("db error");
    }

    public void deleteById(Integer id) {
        DaoApiToken apiToken = new DaoApiToken();
        apiToken.setId(id);
        apiToken.delete();
    }

    public Result verify(HttpServletRequest request) {
        if (request == null) {
            return Result.err("request is null");
        }
        if (!"POST".equals(request.getMethod())) {
            return Result.err("request should post");
        }

        SortedMap<String, String> sortedMap = new TreeMap<>();

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key : parameterMap.keySet()) {
            if (parameterMap.get(key) != null && parameterMap.get(key).length > 0) {
                sortedMap.put(key, parameterMap.get(key)[0]);
            }
        }

        if (!sortedMap.containsKey("token") || StringUtils.isEmpty(sortedMap.get("token"))) {
            return Result.err("token missing");
        }
        String token = sortedMap.get("token");
        if (!isTokenValid(token)) {
            return Result.err("token invalid");
        }

        if (!sortedMap.containsKey("sign") || StringUtils.isEmpty(sortedMap.get("sign"))) {
            return Result.err("sign missing");
        }
        String signInput = sortedMap.get("sign");
        sortedMap.remove("sign");


        LinkedList<String> stringBuilder = new LinkedList<>();
        sortedMap.forEach((key, val) -> {
            stringBuilder.addLast(key + "=" + val);
        });

        String payload = StringUtils.join(stringBuilder, "&");
        log.info("token verify payload:" + payload);

        String sign = DigestUtils.md5Hex(payload);
        log.info("token verify sign:" + sign);
        if (signInput.equals(sign)) {
            return Result.success();
        }
        return Result.err("sign error");
    }
}
