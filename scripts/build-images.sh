#!/bin/bash

echo "开始构建所有微服务镜像..."

# 设置镜像前缀
IMAGE_PREFIX="islanderrrrr"
IMAGE_TAG="latest"

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: Maven未安装，请先安装Maven"
    exit 1
fi

# 构建Java项目
echo "编译Java项目..."

# 构建 Eureka Server
echo "编译 Eureka Server..."
cd eureka-server
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "错误: Eureka Server编译失败"
    exit 1
fi
echo "构建 Eureka Server 镜像..."
docker build -t ${IMAGE_PREFIX}/eureka-server:${IMAGE_TAG} .
cd ..

# 构建 Config Server
echo "编译 Config Server..."
cd config-server
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "错误: Config Server编译失败"
    exit 1
fi
echo "构建 Config Server 镜像..."
docker build -t ${IMAGE_PREFIX}/config-server:${IMAGE_TAG} .
cd ..

# 构建 Gateway Service
echo "编译 Gateway Service..."
cd gateway-service
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "错误: Gateway Service编译失败"
    exit 1
fi
echo "构建 Gateway Service 镜像..."
docker build -t ${IMAGE_PREFIX}/gateway-service:${IMAGE_TAG} .
cd ..

# 构建 User Service
echo "编译 User Service..."
cd user-service
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "错误: User Service编译失败"
    exit 1
fi
echo "构建 User Service 镜像..."
docker build -t ${IMAGE_PREFIX}/user-service:${IMAGE_TAG} .
cd ..

# 构建 Order Service
echo "编译 Order Service..."
cd order-service
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "错误: Order Service编译失败"
    exit 1
fi
echo "构建 Order Service 镜像..."
docker build -t ${IMAGE_PREFIX}/order-service:${IMAGE_TAG} .
cd ..

echo "所有镜像构建完成！"

# 显示镜像列表
echo "镜像列表："
docker images | grep ${IMAGE_PREFIX}

echo "构建完成时间: $(date)"