#!/bin/bash

echo "开始构建所有微服务镜像..."

# 设置镜像前缀
IMAGE_PREFIX="islanderrrrr"

# 构建 Eureka Server
echo "构建 Eureka Server..."
cd eureka-server
docker build -t ${IMAGE_PREFIX}/eureka-server:latest .
cd ..

# 构建 Config Server
echo "构建 Config Server..."
cd config-server
docker build -t ${IMAGE_PREFIX}/config-server:latest .
cd ..

# 构建 Gateway Service
echo "构建 Gateway Service..."
cd gateway-service
docker build -t ${IMAGE_PREFIX}/gateway-service:latest .
cd ..

# 构建 User Service
echo "构建 User Service..."
cd user-service
docker build -t ${IMAGE_PREFIX}/user-service:latest .
cd ..

# 构建 Order Service
echo "构建 Order Service..."
cd order-service
docker build -t ${IMAGE_PREFIX}/order-service:latest .
cd ..

echo "所有镜像构建完成！"

# 显示镜像列表
echo "镜像列表："
docker images | grep ${IMAGE_PREFIX}