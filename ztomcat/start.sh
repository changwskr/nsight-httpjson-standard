#!/usr/bin/env bash
set -euo pipefail

ZTOMCAT_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CATALINA_HOME="${ZTOMCAT_HOME}/apache-tomcat-10.1.34"

if [[ ! -f "${CATALINA_HOME}/bin/catalina.sh" ]]; then
  echo "[ztomcat] Tomcat not found. Run install-tomcat first."
  exit 1
fi

# shellcheck disable=SC1091
. "${ZTOMCAT_HOME}/setenv.local.sh"

export CATALINA_HOME
export CATALINA_BASE="${CATALINA_HOME}"

"${ZTOMCAT_HOME}/apply-config.sh"

echo "[ztomcat] Starting Tomcat on http://localhost:8080"
"${CATALINA_HOME}/bin/catalina.sh" start
