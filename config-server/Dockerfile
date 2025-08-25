FROM openjdk:11-jre-slim

LABEL maintainer="islanderrrrr"
LABEL description="Config Server for Docker Swarm Demo"

# 创建应用目录
WORKDIR /app

# 复制Maven构建的jar文件
COPY target/config-server-1.0.0.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建非root用户
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# 暴露端口
EXPOSE 8888

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8888/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["java", \
           "-Djava.security.egd=file:/dev/./urandom", \
           "-Dspring.profiles.active=docker", \
           "-jar", \
           "app.jar"]