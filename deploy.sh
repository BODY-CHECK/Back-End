#!/bin/bash
set -e

# 1. docker compose pull
echo "==========================================="
echo "========== Pull latest images...==========="
echo "==========================================="
docker compose pull

# 2. docker compose down
echo "==========================================="
echo "=== Stop and remove exist containers...===="
echo "==========================================="
docker compose down

# 3. docker compose up
echo "==========================================="
echo "============ Start containers...==========="
echo "==========================================="
docker compose up -d --build

# 4. docker image rm
echo "==========================================="
echo "============= Remove images...============="
echo "==========================================="
docker image rm $(docker images -q) || true

# 5. complete
echo "==========================================="
echo "========= Deployment successful!!!========="
echo "==========================================="
