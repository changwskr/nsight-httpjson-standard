#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_HOME="$(cd "${SCRIPT_DIR}/../.." && pwd)"
MODULE="demo-ui"

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
  echo "[demo-ui-build] gradle not found." >&2
  exit 1
}

resolve_gradle

tasks=":demo-ui:bootJar"
case "${1:-}" in
  help|-h|--help)
    cat <<EOF
Usage: build.sh [clean|run]
  build.sh        Build demo-ui demo-ui.jar
  build.sh clean  clean + build
  build.sh run    bootRun (port 8099)
EOF
    exit 0
    ;;
  clean) tasks="clean ${tasks}" ;;
  run) tasks=":${MODULE}:bootJar" ;;
esac

gradle --stop >/dev/null 2>&1 || true
(
  cd "${PROJECT_HOME}"
  "${GRADLE}" ${tasks}
)

if [[ "${1:-}" == "run" ]]; then
  exit 0
fi

out_file="${PROJECT_HOME}/${MODULE}/build/libs/demo-ui.jar"
echo
echo "[demo-ui-build] Build output:"
if [[ -f "${out_file}" ]]; then
  echo "  [OK] demo-ui.jar"
  ls -lh "${out_file}"
else
  echo "  [MISSING] demo-ui.jar - not found in ${MODULE}/build/libs" >&2
  exit 1
fi
