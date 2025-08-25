# Docker Swarm Spring Boot 微服务演示项目

这是一个专门为Docker Swarm集群部署实验设计的Spring Boot微服务项目。项目包含完整的微服务架构，包括服务注册中心、配置中心、API网关和业务服务。

## 项目架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Eureka Server │    │  Config Server  │    │  Gateway Service│
│   (注册中心)     │    │   (配置中心)     │    │   (API网关)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                       │
                              ┌────────────────────────┼────────────────────────┐
                              │                        │                        │
                    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
                    │  User Service   │    │  Order Service  │    │     MySQL       │
                    │   (用户服务)     │    │   (订单服务)     │    │    & Redis      │
                    └─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 服务端口分配

| 服务 | 端口 | 描述 |
|------|------|------|
| Eureka Server | 8761 | 服务注册中心 |
| Config Server | 8888 | 配置中心 |
| Gateway Service | 8080 | API网关 |
| User Service | 8081 | 用户服务 |
| Order Service | 8082 | 订单服务 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |

## 快速开始

### 1. 环境要求
- Docker 20.10+
- Docker Compose 2.0+
- Java 11+
- Maven 3.6+

### 2. 本地开发环境启动
```bash
# 克隆项目
git clone https://github.com/islanderrrrr/docker-swarm-springboot-demo.git
cd docker-swarm-springboot-demo

# 构建所有服务
./scripts/build-images.sh

# 启动开发环境
docker-compose up -d
```

### 3. Docker Swarm集群部署

#### 初始化Swarm集群
```bash
# 初始化集群
./scripts/init-swarm.sh

# 或手动初始化
docker swarm init
```

#### 部署服务栈
```bash
# 部署到Swarm集群
./scripts/deploy-stack.sh

# 或手动部署
docker stack deploy -c docker-stack.yml microservices
```

#### 查看服务状态
```bash
# 查看所有服务
docker service ls

# 查看特定服务详情
docker service ps microservices_user-service

# 查看服务日志
docker service logs microservices_user-service
```

### 4. 服务扩缩容
```bash
# 扩展用户服务到3个实例
docker service scale microservices_user-service=3

# 扩展订单服务到2个实例
docker service scale microservices_order-service=2
```

## API接口测试

### 用户服务 API
```bash
# 创建用户
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"张三","email":"zhangsan@example.com","age":25}'

# 获取用户列表
curl http://localhost:8080/api/users

# 获取用户详情
curl http://localhost:8080/api/users/1
```

### 订单服务 API
```bash
# 创建订单
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"productName":"商品A","quantity":2,"price":99.99}'

# 获取订单列表
curl http://localhost:8080/api/orders

# 获取用户订单
curl http://localhost:8080/api/orders/user/1
```

## 监控和管理

### 健康检查
- Eureka: http://localhost:8761
- Config Server: http://localhost:8888/actuator/health
- Gateway: http://localhost:8080/actuator/health
- User Service: http://localhost:8081/actuator/health
- Order Service: http://localhost:8082/actuator/health

### 服务发现
访问 http://localhost:8761 查看所有注册的服务

## Docker Swarm 实验场景

### 1. 服务发现实验
- 启动服务并观察在Eureka中的注册情况
- 停止服务实例并观察自动移除

### 2. 负载均衡实验
- 扩展服务实例数量
- 通过网关访问服务，观察负载分布

### 3. 故障恢复实验
- 手动停止容器，观察Swarm自动重启
- 模拟节点故障，观察服务迁移

### 4. 滚动更新实验
- 更新服务镜像版本
- 观察零停机时间的滚动更新过程

### 5. 网络隔离实验
- 创建自定义网络
- 测试服务间网络通信

## 故障排查

### 常见问题
1. **服务无法注册到Eureka**
   - 检查网络连接
   - 确认Eureka服务先启动

2. **数据库连接失败**
   - 检查MySQL容器状态
   - 确认数据库初始化完成

3. **服务间调用失败**
   - 检查服务注册状态
   - 确认网络配置

### 日志查看
```bash
# 查看容器日志
docker logs <container_id>

# 查看服务日志
docker service logs <service_name>

# 实时跟踪日志
docker service logs -f <service_name>
```

## 项目结构说明

```
├── eureka-server/          # 服务注册中心
├── config-server/          # 配置中心
├── gateway-service/        # API网关
├── user-service/           # 用户服务
├── order-service/          # 订单服务
├── scripts/                # 部署脚本
├── docker-compose.yml      # 开发环境编排
└── docker-stack.yml        # 生产环境Stack配置
```

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License
