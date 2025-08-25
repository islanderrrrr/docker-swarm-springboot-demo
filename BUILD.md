# 构建和部署指南

本文档详细说明如何构建和部署Docker Swarm Spring Boot微服务演示项目。

## 前置要求

### 系统要求
- **操作系统**: Linux (推荐 Ubuntu 18.04+) 或 macOS
- **内存**: 最少4GB，推荐8GB+
- **磁盘空间**: 至少10GB可用空间
- **网络**: 稳定的互联网连接

### 软件要求
- **Docker**: 20.10+ 
- **Docker Compose**: 2.0+
- **Java**: JDK 11+
- **Maven**: 3.6+
- **Git**: 2.0+
- **curl**: 7.0+ (用于API测试)
- **jq**: 1.6+ (JSON处理，可选)

### 环境验证
```bash
# 检查Docker版本
docker --version
docker-compose --version

# 检查Java版本
java -version
javac -version

# 检查Maven版本
mvn -version

# 检查Git版本
git --version
```

## 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/islanderrrrr/docker-swarm-springboot-demo.git
cd docker-swarm-springboot-demo
```

### 2. 设置脚本权限
```bash
chmod +x scripts/*.sh
```

### 3. 构建镜像
```bash
./scripts/build-images.sh
```

### 4. 启动开发环境
```bash
# 使用Docker Compose
docker-compose up -d

# 或者使用Docker Swarm
./scripts/init-swarm.sh
./scripts/deploy-stack.sh
```

### 5. 验证部署
```bash
# 检查服务状态
docker service ls

# 测试API接口
./scripts/test-apis.sh
```

## 详细构建步骤

### 步骤1: 编译Java项目

每个微服务都需要单独编译：

```bash
# 编译Eureka Server
cd eureka-server
mvn clean package -DskipTests
cd ..

# 编译Config Server
cd config-server
mvn clean package -DskipTests
cd ..

# 编译Gateway Service
cd gateway-service
mvn clean package -DskipTests
cd ..

# 编译User Service
cd user-service
mvn clean package -DskipTests
cd ..

# 编译Order Service
cd order-service
mvn clean package -DskipTests
cd ..
```

### 步骤2: 构建Docker镜像

```bash
# 构建所有镜像
./scripts/build-images.sh

# 或单独构建
docker build -t islanderrrrr/eureka-server:latest ./eureka-server
docker build -t islanderrrrr/config-server:latest ./config-server
docker build -t islanderrrrr/gateway-service:latest ./gateway-service
docker build -t islanderrrrr/user-service:latest ./user-service
docker build -t islanderrrrr/order-service:latest ./order-service
```

### 步骤3: 推送镜像到仓库（可选）

```bash
# 登录Docker Hub
docker login

# 推送镜像
./scripts/push-images.sh
```

## 部署选项

### 选项1: Docker Compose（开发环境）

适用于单机开发和测试：

```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

### 选项2: Docker Swarm（生产环境）

适用于集群环境和生产部署：

```bash
# 初始化Swarm集群
./scripts/init-swarm.sh

# 部署服务栈
./scripts/deploy-stack.sh

# 查看服务状态
docker service ls
docker service ps microservices_user-service

# 查看日志
docker service logs microservices_user-service

# 扩展服务
docker service scale microservices_user-service=3

# 删除服务栈
docker stack rm microservices
```

## 服务配置

### 环境变量配置

| 变量名 | 默认值 | 描述 |
|--------|--------|------|
| EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE | http://localhost:8761/eureka | Eureka服务地址 |
| CONFIG_SERVER_URL | http://localhost:8888 | 配置中心地址 |
| SPRING_DATASOURCE_URL | jdbc:mysql://localhost:3306/microservices_db | 数据库连接URL |
| SPRING_DATASOURCE_USERNAME | dbuser | 数据库用户名 |
| SPRING_DATASOURCE_PASSWORD | dbpassword | 数据库密码 |
| SPRING_REDIS_HOST | localhost | Redis主机地址 |
| USER_SERVICE_URL | http://localhost:8081 | 用户服务地址 |

### 服务依赖关系

```
MySQL & Redis (基础服务)
    ↓
Eureka Server (服务注册中心)
    ↓
Config Server (配置中心)
    ↓
Gateway Service (API网关)
    ↓
User Service & Order Service (业务服务)
```

## 监控和运维

### 服务监控
```bash
# 实时监控
./scripts/monitor.sh

# 查看服务状态
docker service ls
docker service ps <service-name>

# 查看节点状态
docker node ls

# 查看网络状态
docker network ls
```

### 日志管理
```bash
# 查看服务日志
docker service logs -f microservices_user-service

# 查看容器日志
docker logs <container-id>

# 查看特定时间段日志
docker service logs --since 2h microservices_user-service
```

### 性能优化

#### JVM参数调优
在Dockerfile中调整JVM参数：
```dockerfile
ENTRYPOINT ["java", \
           "-Djava.security.egd=file:/dev/./urandom", \
           "-Xmx512m", \
           "-Xms256m", \
           "-XX:+UseG1GC", \
           "-XX:MaxGCPauseMillis=200", \
           "-jar", \
           "app.jar"]
```

#### 资源限制
在docker-stack.yml中设置资源限制：
```yaml
deploy:
  resources:
    limits:
      memory: 1G
      cpus: '0.5'
    reservations:
      memory: 512M
      cpus: '0.25'
```

## 故障排查

### 常见问题

1. **服务无法启动**
   ```bash
   # 检查端口占用
   netstat -tulpn | grep 8080
   
   # 检查镜像是否存在
   docker images | grep islanderrrrr
   
   # 查看详细错误信息
   docker service logs microservices_gateway-service
   ```

2. **数据库连接失败**
   ```bash
   # 检查MySQL容器状态
   docker ps | grep mysql
   
   # 检查数据库连接
   docker exec -it <mysql-container> mysql -u dbuser -p
   ```

3. **服务注册失败**
   ```bash
   # 检查Eureka服务状态
   curl http://localhost:8761/actuator/health
   
   # 查看服务注册情况
   curl http://localhost:8761/eureka/apps
   ```

4. **网络连接问题**
   ```bash
   # 检查Docker网络
   docker network ls
   docker network inspect <network-name>
   
   # 测试服务间连通性
   docker exec -it <container> ping <service-name>
   ```

### 调试技巧

1. **进入容器调试**
   ```bash
   # 进入运行中的容器
   docker exec -it <container-id> /bin/bash
   
   # 查看容器内进程
   docker exec -it <container-id> ps aux
   ```

2. **查看配置信息**
   ```bash
   # 查看配置中心配置
   curl http://localhost:8888/user-service/default
   
   # 查看actuator端点
   curl http://localhost:8081/actuator/env
   ```

3. **网络诊断**
   ```bash
   # 测试服务连通性
   curl http://localhost:8080/actuator/health
   curl http://localhost:8081/users/health
   ```

## 清理环境

### 停止并清理所有资源
```bash
# 使用清理脚本
./scripts/cleanup.sh

# 或手动清理
docker stack rm microservices
docker system prune -a -f
docker volume prune -f
```

### 重置Swarm集群
```bash
# 离开Swarm集群
docker swarm leave --force

# 重新初始化
docker swarm init
```

## 扩展和自定义

### 添加新的微服务

1. 创建新的服务目录
2. 添加Spring Boot项目
3. 更新docker-compose.yml和docker-stack.yml
4. 修改构建脚本

### 配置外部数据库

1. 修改环境变量
2. 更新连接字符串
3. 配置数据库初始化脚本

### 集成外部监控

1. 添加Prometheus配置
2. 集成Grafana仪表板
3. 配置告警规则

## 最佳实践

1. **安全性**
   - 使用非root用户运行容器
   - 定期更新基础镜像
   - 配置防火墙规则

2. **可靠性**
   - 设置健康检查
   - 配置重启策略
   - 实施备份策略

3. **性能**
   - 合理设置资源限制
   - 使用多阶段构建
   - 优化镜像大小

4. **维护性**
   - 统一日志格式
   - 使用配置管理
   - 实施版本控制