#!/usr/bin/env bash
set -euo pipefail

ZTOMCAT_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CATALINA_HOME="${ZTOMCAT_HOME}/apache-tomcat-10.1.34"
SERVER_XML="${CATALINA_HOME}/conf/server.xml"
SETENV_SRC="${ZTOMCAT_HOME}/conf/setenv.sh"
SETENV_DST="${CATALINA_HOME}/bin/setenv.sh"

if [[ ! -f "${SERVER_XML}" ]]; then
  echo "[ztomcat] server.xml not found. Run install-tomcat first."
  exit 1
fi

cp -f "${SETENV_SRC}" "${SETENV_DST}"
chmod +x "${SETENV_DST}"

if ! grep -q 'URIEncoding="UTF-8"' "${SERVER_XML}"; then
  tmp="${SERVER_XML}.tmp"
  sed 's|<Connector port="8080" protocol="HTTP/1.1"|<Connector port="8080" protocol="HTTP/1.1" URIEncoding="UTF-8" useBodyEncodingForURI="true"|' \
    "${SERVER_XML}" > "${tmp}"
  mv "${tmp}" "${SERVER_XML}"
  echo "[ztomcat] Applied UTF-8 Connector settings to server.xml"
fi

echo "[ztomcat] Encoding config applied."
