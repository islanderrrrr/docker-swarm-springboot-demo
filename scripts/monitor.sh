#!/bin/bash

echo "Docker Swarm 微服务监控"
echo "========================"

while true; do
    clear
    echo "监控时间: $(date)"
    echo "========================"
    
    echo "服务状态："
    docker service ls
    echo ""
    
    echo "节点状态："
    docker node ls
    echo ""
    
    echo "网络状态："
    docker network ls | grep microservices
    echo ""
    
    echo "卷状态："
    docker volume ls | grep microservices
    echo ""
    
    echo "系统资源使用情况："
    echo "CPU使用率: $(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | awk -F'%' '{print $1}')"
    echo "内存使用情况:"
    free -h | grep "Mem:"
    echo ""
    
    echo "服务详细状态："
    for service in $(docker service ls --format "{{.Name}}" | grep microservices); do
        echo "=== $service ==="
        docker service ps $service --format "table {{.ID}}\t{{.Name}}\t{{.Image}}\t{{.Node}}\t{{.DesiredState}}\t{{.CurrentState}}\t{{.Error}}"
        echo ""
    done
    
    echo "按 Ctrl+C 退出监控"
    sleep 10
done