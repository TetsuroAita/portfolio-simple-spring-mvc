#!/bin/bash

cd "$(dirname "$0")/.."

PROJECT_NAME=$(basename "$PWD")

# ==============================
# コンテナの立ち上げ
# ==============================
docker compose -p "$PROJECT_NAME" up -d --build

echo "========================="
echo "コンテナを立ち上げました。"
echo "========================="

# ==============================
# db の初期化とマイグレーションの実行
# ==============================
bash ./flyway/flyway.sh

echo "========================="
echo "db の初期化とマイグレーションが完了しました。"
echo "========================="

# ==============================
# health-monitor の実行
# ==============================
docker compose exec -d health-monitor sh -c \
"cd /home/vscode/workspace/health-monitor && ./mvnw spring-boot:run"

echo "========================="
echo "health-monitor を実行しました"
echo "========================="

# ==============================
# app の実行
# ==============================
docker compose exec -d app sh -c \
"cd /home/vscode/workspace/app && ./mvnw spring-boot:run"

echo "========================="
echo "app を実行しました"
echo "========================="