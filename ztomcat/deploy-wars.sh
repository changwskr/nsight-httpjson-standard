#!/usr/bin/env bash
set -euo pipefail

ZTOMCAT_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_HOME="$(cd "${ZTOMCAT_HOME}/.." && pwd)"
CATALINA_HOME="${ZTOMCAT_HOME}/apache-tomcat-10.1.34"
WEBAPPS="${CATALINA_HOME}/webapps"

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

echo "[ztomcat] Building WAR files ..."
(
  cd "${PROJECT_HOME}"
  "${GRADLE}" bootWar
)

echo "[ztomcat] Removing stale exploded directories ..."
for ctx in cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et; do
  rm -rf "${WEBAPPS}/${ctx}"
done

echo "[ztomcat] Copying WAR files to webapps ..."
modules=(
  cc-service:cc.war
  ic-service:ic.war
  pc-service:pc.war
  bc-service:bc.war
  ms-service:ms.war
  sv-service:sv.war
  pd-service:pd.war
  cm-service:cm.war
  eb-service:eb.war
  ep-service:ep.war
  bp-service:bp.war
  bd-service:bd.war
  ss-service:ss.war
  cs-service:cs.war
  ct-service:ct.war
  mg-service:mg.war
  om-service:om.war
  common-updownload:ud.war
  common-etc:et.war
)

for entry in "${modules[@]}"; do
  module="${entry%%:*}"
  war="${entry##*:}"
  src="${PROJECT_HOME}/${module}/build/libs/${war}"
  if [[ -f "${src}" ]]; then
    cp -f "${src}" "${WEBAPPS}/${war}"
    echo "  deployed ${war}"
  else
    echo "  missing ${war}"
  fi
done

echo "[ztomcat] Done. Restart Tomcat if it is already running."
