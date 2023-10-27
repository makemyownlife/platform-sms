![](doc/images/gongzhonghaonew.png)


# 1 简介

一个简单易用的短信网关服务，提供客户端 SDK ，支持阿里云，腾讯云，亿美短信发送，适用于中小型公司。

功能如下：

- 应用管理
- 渠道管理
- 模版管理
- 单发短信

# 2 工作原理

![](doc/images/smsliucheng.png)

核心流程如下：

1. 应用程序使用短信平台 SDK ，前端调用应用服务接口发送短信；
2. 应用服务收到短信请求后，调用 SDK 接口根据模版发送短信；
3. 短信平台服务收到请求，根据路由算法选择配置的渠道（比如阿里云、腾讯云）发送短信；
4. 短信成功发送到用户手机。

# 3 环境准备

短信平台服务是 JAVA 应用，所以必须安装 JDK（1.8或更高版本），安装并配置环境变量JAVA_HOME，然后将包含在 JAVA_HOME 中的 bin 目录追加到 PATH 变量中。

如果您的环境中已经存在这些设置，您可以跳过这一步。 

短信平台服务依赖外部 **MySQL** 和 **Redis** 两个服务 ，所以在部署之前必须安装好前置数据库。

# 4 部署流程

## 4.1 创建数据库以及相关表

创建数据库`tech_platform` ，执行`  doc/sql` 目录下的 ` tech_platform.sql`。

执行后效果如下：

![](doc/images/tables.png)

## 4.1 修改部署包配置

从 Release 下载 `platform-sms-admin.tar` ，解压缩后，进入 `conf `目录 。

![](doc/images/adminconfdir.png)

编辑 `application.yml ` 文件：

![](doc/images/prepare.png)

进入 bin 目录，启动服务：

```sh
bin/startup.sh
```

# 5 操作流程

## 5.1 登录页面











## 5.2 新建应用











## 5.3 新建渠道











## 5.4 创建模版











## 5.5 发送短信



























