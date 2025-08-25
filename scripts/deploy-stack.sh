#!/bin/bash

# 检查是否已初始化Swarm
if ! docker info | grep -q "Swarm: active"; then
    echo "Docker Swarm未初始化，正在初始化..."
    docker swarm init
fi

echo "部署微服务栈到Docker Swarm..."

# 部署栈
docker stack deploy -c docker-stack.yml microservices

echo "等待服务启动..."
sleep 10

# 显示服务状态
echo "服务状态："
docker service ls

echo "部署完成！"
echo "访问地址："
echo "- Eureka Dashboard: http://localhost:8761"
echo "- API Gateway: http://localhost:8080"
echo "- User Service: http://localhost:8080/api/users"
echo "- Order Service: http://localhost:8080/api/orders"