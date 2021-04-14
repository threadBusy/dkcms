package cc.dkcms.cms.controller.admin;

import cc.dkcms.cms.common.define.WebSocketCmd;
import cc.dkcms.cms.common.utils.JacksonUtils;
import cc.dkcms.cms.controller.utils.UtilsPageMaker;
import com.jfinal.aop.Aop;
import com.jfinal.kit.JsonKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


@Slf4j
@ServerEndpoint(ControllerPageGenerate.URL_PATH)
public class ControllerPageGenerate {
    public static final String URL_PATH = "/admin/template/socketServer";

    private static final UtilsPageMaker utilsPageMaker = Aop.get(UtilsPageMaker.class);

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("连接打开了OPEN");
        //System.out.println("session:" + session);
        //System.out.println("this:" + this);
    }


    public void sendMessage(String cmd, String payLoad) {
        try {

            WebSocketCmd webSocketCmd = new WebSocketCmd(cmd, payLoad);
            log.info("send message:" + webSocketCmd);
            session.getBasicRemote().sendText(JsonKit.toJson(webSocketCmd));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息时触发
     */
    @OnMessage
    public void onMessage(Session session, String body) {
        log.info("收到消息" + body);

        WebSocketCmd webSocketCmd = JacksonUtils.readValue(body, WebSocketCmd.class);
        if (webSocketCmd == null || StringUtils.isEmpty(webSocketCmd.getCmd())) {
            sendMessage("bye", "cmd missing");
            return;
        }
        utilsPageMaker.setLogger(this::sendMessage);
        utilsPageMaker.clearPagePool();

        try {
            String cmd = webSocketCmd.getCmd();
            if ("generatePage".equals(cmd)) {
                utilsPageMaker.makePage(webSocketCmd);
            } else if ("generateAllPage".equals(cmd)) {
                utilsPageMaker.makeAllPage();
            } else if ("generateIndex".equals(cmd)) {
                utilsPageMaker.makeIndex();
            } else if ("generateCategory".equals(cmd)) {
                utilsPageMaker.makeCategory(webSocketCmd);
            } else if ("generateContent".equals(cmd)) {
                utilsPageMaker.makeContent(webSocketCmd);
            } else if ("generateSite".equals(cmd)) {
                utilsPageMaker.makeSite();
            } else if ("generateSiteMap".equals(cmd)) {
                utilsPageMaker.makeSiteMapFile();
            } else {
                sendMessage("echo", "未知指令:" + webSocketCmd.getCmd());
            }
        } catch (Exception e) {
            sendMessage("echo", "onMessage 异常:" + e.getMessage());
            e.printStackTrace();
        } finally {
            sendMessage("bye", "");
        }
    }

    /**
     * 异常时触发
     */
    @OnError
    public void onError(Throwable throwable, Session session) {
        log.info("error:" + throwable.getMessage());
    }

    /**
     * 关闭连接时触发
     */
    @OnClose
    public void onClose(Session session) {
        log.info("连接关闭了~~~~(>_<)~~~~");
    }

}
