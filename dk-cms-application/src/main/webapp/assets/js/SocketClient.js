function SocketClient() {
    this.waitList = [];
    this.isConnected = false;
    this.webSocket = null;
    this.cmdHandler = {};
}


SocketClient.prototype.constructor = function () {
    this.regHandler('onerror', this.defaultHandler);
    this.regHandler('onopen', this.defaultHandler);
    this.regHandler('onclose', this.defaultHandler);
};

SocketClient.prototype.defaultHandler = function (e) {
    console.log("default handler type[" + e.type + "]");
};
SocketClient.prototype.connect = function (wsServer) {
    var self = this;


    this.webSocket = new WebSocket(wsServer);
    this.webSocket.onerror = function (e) {
        self.runCmd('onerror', e);
    };
    this.webSocket.onopen = function (e) {
        self.isConnected = true;
        self.runCmd('onopen', e);
    };
    this.webSocket.onclose = function (e) {
        self.runCmd('onclose', e);
    };
    this.webSocket.onmessage = function (event) {
        var data = {};
        try {
            data = JSON.parse(event.data);
        } catch (e) {
            //console.err(e);
        }
        if (!data.hasOwnProperty('cmd') || data['cmd'] === '') {
            console.error("onmessage data cmd missing:" + event.data);
            return;
        }
        console.log("收到消息:" + event.data);
        self.runCmd(data['cmd'], data['msg']);
    };
};
SocketClient.prototype.regHandler = function (cmd, cb) {
    this.cmdHandler[cmd] = cb;
};
SocketClient.prototype.runCmd = function (cmd, argv) {
    if (this.cmdHandler.hasOwnProperty(cmd)) {
        this.cmdHandler[cmd](argv);
    } else {
        console.error("not found handler; so cmd[" + cmd + "] do nothing");
    }
};
SocketClient.prototype.close = function () {
    this.webSocket != null && this.webSocket.close();
};
SocketClient.prototype.sendMsg = function (cmd, payLoad) {
    if (payLoad === undefined) {
        payLoad = {}
    }
    payLoad['cmd'] = cmd;
    var content = JSON.stringify(payLoad);

    if (!this.isConnected) {
        console.log("消息排队:" + content);
        this.waitList.push(content)
    } else {
        console.log("发送消息:" + content);
        this.webSocket.send(content);
    }

};
