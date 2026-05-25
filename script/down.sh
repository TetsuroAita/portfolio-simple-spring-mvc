#!/bin/bash

cd "$(dirname "$0")/.."

PROJECT_NAME=$(basename "$PWD")

# ==============================
# 全てのコンテナを削除
# ==============================
docker compose -p "$PROJECT_NAME" down -v

echo "========================="
echo "コンテナを全て削除しました"
echo "========================="