#!/bin/bash
. /etc/profile

min_heap_size="400m"
max_heap_size="1024m"

#!/bin/sh

#加载环境变量
source /etc/profile
W_EXIT_STATUS=65
number_of_expected_args=1

# Define some constants
PROJECT_PATH=$PWD
JAR_PATH=$PROJECT_PATH/lib/
BIN_PATH=$PROJECT_PATH/bin/
SRC_PATH=$PROJECT_PATH/src/

classpath=.:$BIN_PATH:$PROJECT_PATH/config:$JAR_PATH*

start()
{
  java -Xms$min_heap_size -Xmx$max_heap_size -XX:PermSize=128m -Xloggc:$PROJECT_PATH/logs/gc.log -XX:+PrintGCTimeStamps -XX:-PrintGCDetails -cp $classpath com.taogu.App &
  echo $! >$PROJECT_PATH/pid/app.pid
}
stop()
{
  kill  `cat $PROJECT_PATH/pid/app.pid`
}


case $1 in
"restart")
   stop
   start
;;
"start")
   start
;;
"stop")
   stop
;;
*) echo "only accept params start|stop|restart" ;;
esac
