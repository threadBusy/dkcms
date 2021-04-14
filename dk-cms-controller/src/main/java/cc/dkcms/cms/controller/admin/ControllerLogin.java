package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.CommonAuth;
import cc.dkcms.cms.common.Constant;
import cc.dkcms.cms.common.define.AccountType;
import cc.dkcms.cms.common.define.AjaxResponse;
import cc.dkcms.cms.common.define.Result;
import cc.dkcms.cms.controller.base.BaseAdminController;
import cc.dkcms.cms.dao.DaoAccount;
import cc.dkcms.cms.interceptor.AdminAccessCheck;
import cc.dkcms.cms.service.ServiceAccount;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;


@Slf4j
public class ControllerLogin extends BaseAdminController {

    @Inject
    ServiceAccount serviceAccount;

    @Clear(AdminAccessCheck.class)
    @ActionKey(value = Constant.REGISTER_URL)
    public void register() {
        if (isPost()) {
            String username = get("u");
            String password = get("p");
            if (StringUtils.isEmpty(username) || username.length() <= 5) {
                renderJson(AjaxResponse.fail("登录邮箱长度不能小于5个字符，请修改"));
                return;
            }
            if (!CommonAuth.rexCheckEmail(username)) {
                renderJson(AjaxResponse.fail("登录邮箱格式不正确，请修改"));
                return;
            }

            if (!CommonAuth.rexCheckPassword(password)) {
                renderJson(AjaxResponse.fail("密码格式不正确，请修改"));
                return;
            }

            if (serviceAccount.checkUserNameExist(username)) {
                renderJson(AjaxResponse.fail("登录邮箱已经被注册，请更换"));
                return;
            }

            Result result = serviceAccount.createUserAccount(username, password);
            if (!result.getIsSuccess()) {
                renderJson(AjaxResponse.fail(result.getMsg()));
                return;
            }
            renderJson(AjaxResponse.success("注册成功"));
            return;
        }

    }

    @Clear(AdminAccessCheck.class)
    @ActionKey(value = Constant.LOGIN_URL)
    public void index() {


        if (isPost()) {
            String username = get("u");
            String password = get("p");
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                renderJson(AjaxResponse.fail("请填写用户名密码"));
                return;
            }

            DaoAccount account = serviceAccount.getAccountByUsernameAndPassword(username, password);
            if (account == null) {
                log.info("登录账号为找到 {}, {}", username, password);
                renderJson(AjaxResponse.fail("登录失败,账号有误"));
                return;
            }
            if (!account.getEnable()) {
                log.info("登录账号被禁止 {}", username);
                renderJson(AjaxResponse.fail("登录失败，禁止登录"));
                return;
            }

            // 记录登录次数
            serviceAccount.logAdminLogin(account, getRequest().getRemoteAddr());
            try {
                String token = CommonAuth.generateToken(account.getId());
                setCookie(Constant.ADMIN_TOKEN_NAME, token, 86400 * 100);
                if(AccountType.of(account.getRole()).equals(AccountType.ADMIN)) {
                    renderJson(AjaxResponse.success(Constant.ADMIN_LOGIN_SUCCESS_JUMP_URL));
                }else{
                    renderJson(AjaxResponse.success(Constant.USER_LOGIN_SUCCESS_JUMP_URL));

                }
            } catch (Exception e) {
                e.printStackTrace();
                renderJson(AjaxResponse.fail("get token fail;" + e.getMessage()));
            }
        }
    }


    @ActionKey("/logout")
    public void logout() {
        removeCookie(Constant.ADMIN_TOKEN_NAME, "/");
        redirect(Constant.LOGIN_URL);
    }

}
