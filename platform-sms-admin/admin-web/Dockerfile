# 基础镜像为 openjdk:8-jre-slim，它是一个包含了 OpenJDK 8 JRE 的精简版镜像。该镜像将作为构建当前镜像的基础。
FROM openjdk:8-jre-slim
MAINTAINER courage

# 设置工作目录
WORKDIR /app

# 复制 bin、logs、plugins、lib、conf 目录到 Docker 镜像中
COPY target/platform-sms-admin/platform-sms-admin/bin /app/bin
COPY target/platform-sms-admin/platform-sms-admin/logs /app/logs
COPY target/platform-sms-admin/platform-sms-admin/plugin /app/plugin
COPY target/platform-sms-admin/platform-sms-admin/lib /app/lib
COPY target/platform-sms-admin/platform-sms-admin/conf /app/conf

# 给启动脚本添加可执行权限
RUN chmod +x /app/bin/app.sh

# 暴露需要的端口
EXPOSE 8089

# 启动你的 Java 应用
CMD ["/app/bin/app.sh"]

# 使用 docker build -t platformsms . 打包
# 启动时，本地需要挂载 application.yml 文件 然后执行如下命令即可。
# docker run --name sms --mount type=bind,source=/Users/zhangyong/docker/platformsms/conf/application.yml,target=/app/conf/application.yml --workdir=/app -p 8089:8089  --runtime=runc -d platformsms:latest


