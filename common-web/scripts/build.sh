#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_HOME="$(cd "${SCRIPT_DIR}/../.." && pwd)"
MODULE="common-web"

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
  echo "[common-web-build] gradle not found." >&2
  exit 1
}

resolve_gradle

tasks=":common-core:build :common-web:build"
case "${1:-}" in
  help|-h|--help)
    cat <<EOF
Usage: build.sh [clean]
  build.sh        Build library JAR
  build.sh clean  clean + build
EOF
    exit 0
    ;;
  clean) tasks="clean ${tasks}" ;;

esac

gradle --stop >/dev/null 2>&1 || true
(
  cd "${PROJECT_HOME}"
  "${GRADLE}" ${tasks}
)

if [[ "${1:-}" == "run" ]]; then
  exit 0
fi

out_dir="${PROJECT_HOME}/${MODULE}/build/libs"
echo
echo "[common-web-build] Build output:"
if compgen -G "${out_dir}/*.jar" > /dev/null; then
  echo "  [OK] JAR in build/libs"
  ls -lh "${out_dir}"/*.jar
else
  echo "  [MISSING] JAR - not found in ${MODULE}/build/libs" >&2
  exit 1
fi
