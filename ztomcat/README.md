# ztomcat — NSIGHT 로컬 Tomcat

Spring Boot 3 WAR 배포용 **Apache Tomcat 10.1** 로컬 환경입니다.

## 1. 설치

```bat
cd ztomcat
install-tomcat.bat
```

## 2. 기동 / 중지

```bat
start.bat    rem http://localhost:8080
stop.bat
```

경로에 괄호 `(23-08-15)` 가 있어 `start.bat`/`stop.bat` 은 내부적으로 PowerShell(`start.ps1`/`stop.ps1`)을 사용합니다.

## UTF-8 (한글 깨짐 방지)

`start.bat` 실행 시 `apply-config.ps1`이 다음을 적용합니다.

- `server.xml` Connector: `URIEncoding=UTF-8`
- JVM: `-Dfile.encoding=UTF-8`, `-Dsun.stdout.encoding=UTF-8`

Tomcat 재기동 후 반영됩니다. 로그 파일(`logs/catalina.*.log`)은 UTF-8로 열어야 합니다.

## 3. WAR 배포 (선택)

```bat
deploy-wars.bat
stop.bat
start.bat
```

배포 후 URL 예:

```text
POST http://localhost:8080/sv/online
POST http://localhost:8080/cc/online
POST http://localhost:8080/ud/online
POST http://localhost:8080/et/online
```

## 디렉터리

| 경로 | 설명 |
|------|------|
| `apache-tomcat-10.1.34/` | Tomcat 설치본 (git 제외) |
| `install-tomcat.bat` | Tomcat 다운로드·압축 해제 |
| `deploy-wars.bat` | `gradle bootWar` 후 webapps 복사 |
| `start.bat` / `stop.bat` | Tomcat 기동·중지 |

## 요구사항

- JDK 21 (`JAVA_HOME` 또는 `%USERPROFILE%\.jdks\temurin-21.0.4`)
- Gradle 8.x (WAR 빌드 시)
