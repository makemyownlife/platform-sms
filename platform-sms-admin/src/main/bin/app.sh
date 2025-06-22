#!/bin/bash

current_path=`pwd`
case "`uname`" in
    Linux)
		bin_abs_path=$(readlink -f $(dirname $0))
		;;
	*)
		bin_abs_path=`cd $(dirname $0); pwd`
		;;
esac
base=${bin_abs_path}/..
export LANG=en_US.UTF-8
export BASE=$base

if [ ! -d $base/logs ] ; then
	mkdir -p $base/logs
fi

## set java path
if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
fi

JAVA_OPTS="$JAVA_OPTS -Xss256k -XX:+AggressiveOpts -XX:-UseBiasedLocking -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$base/logs"

JAVA_OPTS="-server -Xms512m -Xmx512m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:MaxPermSize=256m $JAVA_OPTS"

JAVA_OPTS=" $JAVA_OPTS -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
APP_OPTS="-DappName=sms-admin"

for i in $base/lib/*;
    do CLASSPATH=$i:"$CLASSPATH";
done

if [ ! -d $base/logs ] ; then
    mkdir -p $base/logs
fi

# 定义日志文件路径
LOG_FILE=$base/logs/console.log

CLASSPATH="$base/conf:$CLASSPATH";

echo "cd to $bin_abs_path for workaround relative path"
cd $bin_abs_path

$JAVA $JAVA_OPTS $JAVA_DEBUG_OPT $APP_OPTS -classpath .:$CLASSPATH cn.javayong.platform.sms.admin.SmsAdminApplication

