#!/bin/bash

echo "测试微服务API接口..."

# 设置API基础URL
GATEWAY_URL="http://localhost:8080"

# 等待服务启动
echo "等待服务启动..."
sleep 30

# 测试用户服务
echo "=== 测试用户服务 ==="

# 创建用户
echo "创建用户..."
USER_RESPONSE=$(curl -s -X POST "${GATEWAY_URL}/api/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试用户",
    "email": "test@example.com",
    "age": 25
  }')

echo "用户创建响应: $USER_RESPONSE"

# 提取用户ID
USER_ID=$(echo $USER_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
echo "用户ID: $USER_ID"

# 获取用户列表
echo "获取用户列表..."
curl -s "${GATEWAY_URL}/api/users" | jq '.'

# 获取用户详情
if [ ! -z "$USER_ID" ]; then
    echo "获取用户详情..."
    curl -s "${GATEWAY_URL}/api/users/${USER_ID}" | jq '.'
fi

echo ""
echo "=== 测试订单服务 ==="

# 创建订单
if [ ! -z "$USER_ID" ]; then
    echo "创建订单..."
    ORDER_RESPONSE=$(curl -s -X POST "${GATEWAY_URL}/api/orders" \
      -H "Content-Type: application/json" \
      -d "{
        \"userId\": ${USER_ID},
        \"productName\": \"测试商品\",
        \"quantity\": 2,
        \"price\": 99.99
      }")
    
    echo "订单创建响应: $ORDER_RESPONSE"
    
    # 提取订单ID
    ORDER_ID=$(echo $ORDER_RESPONSE | grep -o '"id":[0-9]*' | cut -d':' -f2)
    echo "订单ID: $ORDER_ID"
fi

# 获取订单列表
echo "获取订单列表..."
curl -s "${GATEWAY_URL}/api/orders" | jq '.'

# 获取用户订单
if [ ! -z "$USER_ID" ]; then
    echo "获取用户订单..."
    curl -s "${GATEWAY_URL}/api/orders/user/${USER_ID}" | jq '.'
fi

# 获取订单详情（包含用户信息）
if [ ! -z "$ORDER_ID" ]; then
    echo "获取订单详情（含用户信息）..."
    curl -s "${GATEWAY_URL}/api/orders/${ORDER_ID}/details" | jq '.'
fi

echo ""
echo "=== 测试统计接口 ==="

# 用户统计
echo "用户统计信息..."
curl -s "${GATEWAY_URL}/api/users/statistics" | jq '.'

# 订单统计
echo "订单统计信息..."
curl -s "${GATEWAY_URL}/api/orders/statistics" | jq '.'

# 热门商品
echo "热门商品..."
curl -s "${GATEWAY_URL}/api/orders/popular-products" | jq '.'

echo ""
echo "=== 测试健康检查 ==="

# 服务健康检查
echo "Eureka健康检查..."
curl -s "http://localhost:8761/actuator/health" | jq '.'

echo "Gateway健康检查..."
curl -s "http://localhost:8080/actuator/health" | jq '.'

echo "User Service健康检查..."
curl -s "http://localhost:8081/actuator/health" | jq '.'

echo "Order Service健康检查..."
curl -s "http://localhost:8082/actuator/health" | jq '.'

echo ""
echo "API测试完成！"