#!/bin/bash

echo "推送所有微服务镜像到Docker Hub..."

# 设置镜像前缀和标签
IMAGE_PREFIX="islanderrrrr"
IMAGE_TAG="latest"

# 检查是否已登录Docker Hub
if ! docker info | grep -q "Username"; then
    echo "请先登录Docker Hub: docker login"
    exit 1
fi

# 推送镜像
echo "推送 Eureka Server 镜像..."
docker push ${IMAGE_PREFIX}/eureka-server:${IMAGE_TAG}

echo "推送 Config Server 镜像..."
docker push ${IMAGE_PREFIX}/config-server:${IMAGE_TAG}

echo "推送 Gateway Service 镜像..."
docker push ${IMAGE_PREFIX}/gateway-service:${IMAGE_TAG}

echo "推送 User Service 镜像..."
docker push ${IMAGE_PREFIX}/user-service:${IMAGE_TAG}

echo "推送 Order Service 镜像..."
docker push ${IMAGE_PREFIX}/order-service:${IMAGE_TAG}

echo "所有镜像推送完成！"
echo "推送完成时间: $(date)"