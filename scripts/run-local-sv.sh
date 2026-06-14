#!/usr/bin/env bash
set -euo pipefail
if [ -x ./gradlew ]; then
  ./gradlew :sv-service:bootRun
else
  gradle :sv-service:bootRun
fi
