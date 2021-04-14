package cc.dkcms.cms.common.define;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebSocketCmd {


    public WebSocketCmd(String cmd) {
        this(cmd, -1, false);
    }

    public WebSocketCmd(String cmd, String msg) {
        this(cmd, -1, false);
        this.msg = msg;
    }

    public WebSocketCmd(String cmd, Integer categoryId, Boolean withChild) {
        this.cmd = cmd;
        this.categoryId = categoryId;
        this.withChild = withChild;
        this.page = -1;
    }


    private String  cmd;
    private Integer categoryId;
    private Boolean withChild;
    private Integer page;
    private String  msg;

}
