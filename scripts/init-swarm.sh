#!/bin/bash

echo "初始化Docker Swarm集群..."

# 检查是否已经是Swarm节点
if docker info | grep -q "Swarm: active"; then
    echo "Docker Swarm已经初始化"
    docker node ls
else
    echo "初始化Swarm管理节点..."
    docker swarm init
    echo "Swarm初始化完成！"
fi

# 显示集群信息
echo "集群状态："
docker node ls

echo "要添加工作节点，请在其他机器上运行："
docker swarm join-token worker