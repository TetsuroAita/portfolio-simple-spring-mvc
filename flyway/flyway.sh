#!/bin/bash

cd "$(dirname "$0")"

docker run --rm \
--network portfolio-network \
-v ./conf:/flyway/conf \
-v ./migration:/flyway/migration \
--env-file ./.env.flyway \
flyway/flyway:latest \
-configFiles=/flyway/conf/flyway-dev.conf \
clean \
migrate