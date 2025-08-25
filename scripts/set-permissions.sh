#!/bin/bash

echo "设置脚本执行权限..."

chmod +x scripts/build-images.sh
chmod +x scripts/deploy-stack.sh
chmod +x scripts/init-swarm.sh
chmod +x scripts/push-images.sh
chmod +x scripts/cleanup.sh
chmod +x scripts/monitor.sh
chmod +x scripts/test-apis.sh

echo "权限设置完成！"

ls -la scripts/