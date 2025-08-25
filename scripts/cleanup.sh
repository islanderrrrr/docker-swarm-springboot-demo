#!/bin/bash

echo "清理Docker Swarm环境..."

# 停止并删除服务栈
echo "删除微服务栈..."
docker stack rm microservices

# 等待服务完全停止
echo "等待服务停止..."
sleep 30

# 清理未使用的容器
echo "清理未使用的容器..."
docker container prune -f

# 清理未使用的镜像
echo "清理未使用的镜像..."
docker image prune -f

# 清理未使用的网络
echo "清理未使用的网络..."
docker network prune -f

# 清理未使用的卷
echo "清理未使用的卷..."
docker volume prune -f

echo "清理完成！"

# 显示剩余资源
echo "剩余Docker资源："
echo "镜像："
docker images
echo ""
echo "容器："
docker ps -a
echo ""
echo "网络："
docker network ls
echo ""
echo "卷："
docker volume ls