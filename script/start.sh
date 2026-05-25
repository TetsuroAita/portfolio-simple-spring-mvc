#!/bin/bash

cd "$(dirname "$0")/.."

PROJECT_NAME=$(basename "$PWD")

# ==============================
# コンテナを再稼働
# ==============================
docker compose -p "$PROJECT_NAME" start

echo "========================="
echo "全てのコンテナが再稼働しました。"
echo "========================="