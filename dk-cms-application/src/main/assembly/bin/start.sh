#!/bin/bash
APP_NAME=dk-cms
APP_PID=bin/run.appid
MAIN_CLASS=cc.dkcms.cms.DkCmsApplication

[[ . == $(dirname $0) ]] && cd ..;

echo "pwd:`pwd`"

#使用说明，用来提示输入参数
usage() {
     echo "Usage: sh start.sh [start|stop|restart|status]"
     exit 1
}

#检查程序是否在运行
is_exist(){
    #如果不存在返回1，存在返回0
    if [[ -f "${APP_PID}" ]]; then
        return 1
    else
         return 0
    fi
}

#启动方法
start(){
    is_exist
    if [[ $? -eq "1" ]]; then
        echo "${APP_PID}  file is already running. pid=`cat ${APP_PID}` ."
    else
         JAVA_OPT=' -cp ./lib/*:./config/* -Dfile.encoding=UTF-8 -Xms64m -Xmx64m'
         java ${JAVA_OPT} ${MAIN_CLASS}
         echo $!>${APP_PID}
         echo "${APP_NAME} start success;Pid: $!"
    fi
}

#停止方法
stop(){
    is_exist
    if [[ $? -eq "1" ]]; then
         kill -9 `cat ${APP_PID}`
         mv ${APP_PID} ${APP_PID}'.absort'
    else
        echo " already stop."
    fi
}

#输出运行状态
status(){
    is_exist
    if [[ $? -eq "0" ]]; then
         echo "${APP_NAME} is running. Pid is ${pid}"
    else
         echo "${APP_NAME} is NOT running."
    fi
}

#重启
restart(){
    stop
    start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
    "start")
         start
     ;;
    "stop")
         stop
     ;;
    "status")
         status
     ;;
    "restart")
         restart
     ;;
    *)
         usage
     ;;
esac