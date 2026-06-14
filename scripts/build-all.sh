#!/usr/bin/env bash
set -euo pipefail
if [ -x ./gradlew ]; then
  ./gradlew clean build bootWar
else
  gradle clean build bootWar
fi
