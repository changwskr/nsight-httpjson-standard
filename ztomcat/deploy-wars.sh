#!/usr/bin/env bash
set -euo pipefail

ZTOMCAT_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_HOME="$(cd "${ZTOMCAT_HOME}/.." && pwd)"
CATALINA_HOME="${ZTOMCAT_HOME}/apache-tomcat-10.1.34"
WEBAPPS="${CATALINA_HOME}/webapps"

ALL_MODULES=(
  cc-service:cc.war:cc
  ic-service:ic.war:ic
  pc-service:pc.war:pc
  bc-service:bc.war:bc
  ms-service:ms.war:ms
  sv-service:sv.war:sv
  pd-service:pd.war:pd
  cm-service:cm.war:cm
  eb-service:eb.war:eb
  ep-service:ep.war:ep
  bp-service:bp.war:bp
  bd-service:bd.war:bd
  ss-service:ss.war:ss
  cs-service:cs.war:cs
  ct-service:ct.war:ct
  mg-service:mg.war:mg
  om-service:om.war:om
  common-updownload:ud.war:ud
  common-etc:et.war:et
)

usage() {
  cat <<'EOF'
Usage:
  deploy-wars.sh              Build and deploy all 19 WARs
  deploy-wars.sh all          Same as above
  deploy-wars.sh sv           Build and deploy one code (e.g. sv.war -> /sv)
  deploy-wars.sh sv cc ud     Build and deploy multiple codes

Codes: cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et
EOF
}

resolve_entry() {
  local code
  code="$(echo "$1" | tr '[:upper:]' '[:lower:]')"
  local entry module war ctx
  for entry in "${ALL_MODULES[@]}"; do
    IFS=':' read -r module war ctx <<< "${entry}"
    if [[ "${ctx}" == "${code}" ]]; then
      echo "${module}:${war}:${ctx}"
      return 0
    fi
  done
  echo "[ztomcat] Unknown code: ${1}" >&2
  echo "[ztomcat] Codes: cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et" >&2
  return 1
}

if [[ "${1:-}" == "help" || "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
  usage
  exit 0
fi

if [[ ! -f "${CATALINA_HOME}/bin/catalina.sh" ]]; then
  echo "[ztomcat] Tomcat not found. Run install-tomcat.sh first."
  exit 1
fi

# shellcheck disable=SC1091
. "${ZTOMCAT_HOME}/setenv.local.sh"

resolve_gradle() {
  if [[ -n "${GRADLE:-}" && -x "${GRADLE}" ]]; then
    return 0
  fi
  if command -v gradle >/dev/null 2>&1; then
    GRADLE="gradle"
    return 0
  fi
  echo "[ztomcat] gradle not found."
  exit 1
}

resolve_gradle

selected=()
gradle_tasks=()
deploy_all=0

if [[ $# -eq 0 ]]; then
  deploy_all=1
else
  for arg in "$@"; do
    if [[ "$(echo "${arg}" | tr '[:upper:]' '[:lower:]')" == "all" ]]; then
      deploy_all=1
      break
    fi
  done
fi

if [[ "${deploy_all}" -eq 1 ]]; then
  for entry in "${ALL_MODULES[@]}"; do
    selected+=("${entry}")
  done
  gradle_tasks=(bootWar)
  echo "[ztomcat] Building all WAR files ..."
else
  for local_code in "$@"; do
    resolved="$(resolve_entry "${local_code}")"
    selected+=("${resolved}")
    module="${resolved%%:*}"
    gradle_tasks+=(":${module}:bootWar")
  done
  echo "[ztomcat] Building selected WAR(s): ${gradle_tasks[*]}"
fi

(
  cd "${PROJECT_HOME}"
  "${GRADLE}" "${gradle_tasks[@]}"
)

echo "[ztomcat] Removing stale exploded directories ..."
for entry in "${selected[@]}"; do
  IFS=':' read -r _module _war ctx <<< "${entry}"
  rm -rf "${WEBAPPS}/${ctx}"
done

echo "[ztomcat] Copying WAR files to webapps ..."
for entry in "${selected[@]}"; do
  IFS=':' read -r module war ctx <<< "${entry}"
  src="${PROJECT_HOME}/${module}/build/libs/${war}"
  if [[ -f "${src}" ]]; then
    cp -f "${src}" "${WEBAPPS}/${war}"
    echo "  deployed ${war}"
  else
    echo "  missing ${war}"
  fi
done

if [[ "${deploy_all}" -eq 1 ]]; then
  echo "[ztomcat] Done. Restart Tomcat if it is already running."
else
  echo "[ztomcat] Done. Tomcat running: /{code} context redeploys automatically (~15s)."
fi
