#!/usr/bin/env bash
set -euo pipefail
if [ -x ./gradlew ]; then
  ./gradlew :om-service:bootRun
else
  gradle :om-service:bootRun
fi
