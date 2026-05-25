#!/bin/bash

cd "$(dirname "$0")/.."

PROJECT_NAME=$(basename "$PWD")

# ==============================
# コンテナを全て停止
# ==============================
docker compose -p "$PROJECT_NAME" stop

echo "========================="
echo "コンテナを全て停止しました"
echo "========================="