#!/bin/sh
# Applied by ztomcat/start.sh on each start (WAR modules require JDK 21)

if [ -z "${JAVA_HOME:-}" ]; then
  for candidate in \
    "${HOME}/.jdks/temurin-21.0.4" \
    "/usr/lib/jvm/java-21-openjdk-amd64" \
    "/usr/lib/jvm/java-21-openjdk" \
    "/usr/lib/jvm/java-21" \
    "/usr/lib/jvm/temurin-21-jdk-amd64" \
    "/usr/lib/jvm/temurin-21-jdk"; do
    if [ -x "${candidate}/bin/java" ]; then
      JAVA_HOME="${candidate}"
      export JAVA_HOME
      break
    fi
  done
fi

JAVA_OPTS="${JAVA_OPTS} -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"
export JAVA_OPTS

CATALINA_OPTS="${CATALINA_OPTS} -Xms512m -Xmx1536m -Duser.timezone=Asia/Seoul"
export CATALINA_OPTS
