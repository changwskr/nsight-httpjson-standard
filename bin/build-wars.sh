#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT_DIR="$ROOT/bin"
SERVICES=(cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om)

usage() {
  cat <<'EOF'
Usage:
  bin/build-wars.sh              # build all business WAR files
  bin/build-wars.sh sv bc        # build selected modules only

Output:
  bin/{code}.war
EOF
}

resolve_gradle() {
  if [[ -x "$ROOT/gradlew" ]]; then
    echo "$ROOT/gradlew"
  else
    echo "gradle"
  fi
}

build_wars() {
  local -a targets=("$@")
  local gradle_cmd tasks src dest code

  gradle_cmd="$(resolve_gradle)"
  tasks=()
  for code in "${targets[@]}"; do
    tasks+=(":${code}-service:bootWar")
  done

  echo "==> Gradle bootWar: ${targets[*]}"
  (cd "$ROOT" && "$gradle_cmd" "${tasks[@]}")

  mkdir -p "$OUT_DIR"
  for code in "${targets[@]}"; do
    src="$ROOT/${code}-service/build/libs/${code}.war"
    dest="$OUT_DIR/${code}.war"
    if [[ ! -f "$src" ]]; then
      echo "ERROR: WAR not found: $src" >&2
      exit 1
    fi
    cp "$src" "$dest"
    echo "  copied -> bin/${code}.war"
  done
}

main() {
  if [[ "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
    usage
    exit 0
  fi

  local -a targets
  if [[ $# -eq 0 ]]; then
    targets=("${SERVICES[@]}")
  else
    targets=("$@")
    for code in "${targets[@]}"; do
      local found=false
      for known in "${SERVICES[@]}"; do
        if [[ "$code" == "$known" ]]; then
          found=true
          break
        fi
      done
      if [[ "$found" != true ]]; then
        echo "ERROR: unknown business code: $code" >&2
        usage
        exit 1
      fi
    done
  fi

  build_wars "${targets[@]}"

  echo
  echo "==> Done. WAR files in bin/"
  ls -1 "$OUT_DIR"/*.war 2>/dev/null || true
}

main "$@"
