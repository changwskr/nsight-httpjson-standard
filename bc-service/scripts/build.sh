#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_HOME="$(cd "${SCRIPT_DIR}/../.." && pwd)"
MODULE="bc-service"

resolve_gradle() {
  local home="${GRADLE_HOME_OVERRIDE:-${GRADLE_HOME:-}}"
  if [[ -n "${home}" && -x "${home}/bin/gradle" ]]; then
    GRADLE="${home}/bin/gradle"
    return 0
  fi
  if command -v gradle >/dev/null 2>&1; then
    GRADLE="$(command -v gradle)"
    return 0
  fi
  echo "[bc-build] gradle not found." >&2
  exit 1
}

resolve_gradle

tasks=":common-core:build :common-web:build :${MODULE}:bootWar"
case "${1:-}" in
  help|-h|--help)
    cat <<EOF
Usage: build.sh [clean|run]
  build.sh        Build common-core/web + bc.war
  build.sh clean  clean + build
  build.sh run    bootRun (port 8083)
EOF
    exit 0
    ;;
  clean) tasks="clean ${tasks}" ;;
  run) tasks=":${MODULE}:bootRun" ;;
esac

gradle --stop >/dev/null 2>&1 || true
(
  cd "${PROJECT_HOME}"
  "${GRADLE}" ${tasks}
)

if [[ "${1:-}" == "run" ]]; then
  exit 0
fi

war_file="${PROJECT_HOME}/${MODULE}/build/libs/bc.war"
echo
echo "[bc-build] Build output:"
if [[ -f "${war_file}" ]]; then
  echo "  [OK] bc.war"
  ls -lh "${war_file}"
else
  echo "  [MISSING] bc.war - not found in ${MODULE}/build/libs" >&2
  exit 1
fi
