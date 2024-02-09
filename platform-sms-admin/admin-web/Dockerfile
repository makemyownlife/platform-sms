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
RUN chmod +x /app/bin/startup.sh

# 暴露需要的端口
EXPOSE 8089

# 启动你的 Java 应用
CMD ["/app/bin/app.sh"]

# 使用 docker build -t platformsms . 打包

