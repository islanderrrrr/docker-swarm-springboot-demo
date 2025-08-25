FROM openjdk:11-jre-slim

LABEL maintainer="islanderrrrr"
LABEL description="Order Service for Docker Swarm Demo"

# 安装curl用于健康检查
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 创建应用目录
WORKDIR /app

# 复制Maven构建的jar文件
COPY target/order-service-1.0.0.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建非root用户
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# 暴露端口
EXPOSE 8082

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["java", \
           "-Djava.security.egd=file:/dev/./urandom", \
           "-Dspring.profiles.active=docker", \
           "-Xmx512m", \
           "-Xms256m", \
           "-jar", \
           "app.jar"]