#!/usr/bin/env bash
set -euo pipefail

ZTOMCAT_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "[ztomcat] Stop -> deploy WARs -> start -> verify"
"${ZTOMCAT_HOME}/stop.sh"
sleep 3
"${ZTOMCAT_HOME}/deploy-wars.sh"
"${ZTOMCAT_HOME}/start.sh"

echo "[ztomcat] Waiting for WAR deployments (~5 min for 19 modules) ..."
contexts=(cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et)
deadline=$((SECONDS + 360))

while [[ "${SECONDS}" -lt "${deadline}" ]]; do
  up=0
  for ctx in "${contexts[@]}"; do
    code="$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "http://localhost:8080/${ctx}/actuator/health" 2>/dev/null || echo "000")"
    if [[ "${code}" == "200" ]]; then
      up=$((up + 1))
    fi
  done
  echo "  health OK: ${up} / ${#contexts[@]}"
  if [[ "${up}" -eq "${#contexts[@]}" ]]; then
    break
  fi
  sleep 15
done

"${ZTOMCAT_HOME}/verify-deploy.sh"
