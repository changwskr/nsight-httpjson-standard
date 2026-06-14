#!/usr/bin/env bash
# ztomcat local JVM settings (sourced by start.sh; mirrored in conf/setenv.sh for Tomcat)

ztomcat_resolve_java_home() {
  if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
    return 0
  fi
  local candidates=(
    "${HOME}/.jdks/temurin-21.0.4"
    "${HOME}/.sdkman/candidates/java/current"
    "/usr/lib/jvm/java-21-openjdk-amd64"
    "/usr/lib/jvm/java-21-openjdk"
    "/usr/lib/jvm/java-21"
    "/usr/lib/jvm/temurin-21-jdk-amd64"
    "/usr/lib/jvm/temurin-21-jdk"
    "/opt/homebrew/opt/openjdk@21"
  )
  local candidate
  for candidate in "${candidates[@]}"; do
    if [[ -x "${candidate}/bin/java" ]]; then
      export JAVA_HOME="${candidate}"
      return 0
    fi
  done
  return 1
}

_sourced=0
if [[ "${BASH_SOURCE[0]}" != "${0}" ]]; then
  _sourced=1
fi

_require_java="${ZTOMCAT_REQUIRE_JAVA:-1}"
if ! ztomcat_resolve_java_home; then
  if [[ "${_require_java}" == "1" ]]; then
    echo "[ztomcat] JAVA_HOME is not set. Install JDK 21 or set JAVA_HOME." >&2
    if [[ "${_sourced}" -eq 1 ]]; then
      return 1
    fi
    exit 1
  fi
fi

export CATALINA_OPTS="-Xms512m -Xmx1536m -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Seoul"
