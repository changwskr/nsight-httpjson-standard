# ztomcat — NSIGHT 로컬 Tomcat WAR 배포

Spring Boot 3 **WAR** 19개를 **Apache Tomcat 10.1.34**에 올려 로컬에서 운영 환경과 동일한 context path로 테스트하기 위한 도구 모음입니다.

| 항목 | 값 |
|------|-----|
| Tomcat | 10.1.34 (Jakarta EE 10 / Servlet 6) |
| 포트 | **8080** |
| JDK | **21 필수** (WAR가 Java 21로 빌드됨) |
| WAR 개수 | 19 (업무 17 + ET + UD) |
| Gradle | 8.x (`bootWar` 빌드) |

> WAR는 **JDK 21**로 컴파일됩니다. Tomcat을 JDK 18 등으로 기동하면 Spring Boot가 뜨지 않아 **`/sv/online` 404**가 납니다. `start.ps1` / `start.sh`가 JDK 21을 고정합니다.

---

## 목차

1. [빠른 시작](#1-빠른-시작)
2. [디렉터리 구조](#2-디렉터리-구조)
3. [스크립트 목록](#3-스크립트-목록)
4. [Tomcat 설치](#4-tomcat-설치)
5. [기동·중지](#5-기동중지)
6. [WAR 배포 (deploy-wars)](#6-war-배포-deploy-wars)
7. [배포 검증 (verify-deploy)](#7-배포-검증-verify-deploy)
8. [원클릭 재배포 (deploy-restart)](#8-원클릭-재배포-deploy-restart)
9. [설정 (UTF-8·JVM)](#9-설정-utf-8jvm)
10. [배포 URL·Context](#10-배포-urlcontext)
11. [ztomcat vs scripts vs demo-ui](#11-ztomcat-vs-scripts-vs-demo-ui)
12. [트러블슈팅](#12-트러블슈팅)
13. [관련 문서](#13-관련-문서)

---

## 1. 빠른 시작

### Windows

```bat
cd ztomcat
install-tomcat.bat
deploy-wars.bat all
start.bat
verify-deploy.ps1
```

### Linux / macOS / Git Bash

```bash
cd ztomcat
chmod +x *.sh
./install-tomcat.sh
./deploy-wars.sh all
./start.sh
./verify-deploy.sh
```

### API 호출 확인

```bash
curl http://localhost:8080/sv/actuator/health
curl -X POST http://localhost:8080/sv/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/sv-sample-inquiry.json
```

---

## 2. 디렉터리 구조

```text
ztomcat/
├── apache-tomcat-10.1.34/   # install 후 생성 (git 제외 권장)
│   ├── bin/setenv.bat|sh    # apply-config가 conf/에서 복사
│   ├── conf/server.xml      # UTF-8 Connector 패치 대상
│   └── webapps/             # *.war 배포 위치
├── conf/
│   ├── setenv.bat           # Windows JVM·JDK 21 템플릿
│   └── setenv.sh            # Linux JVM·JDK 21 템플릿
├── setenv.local.bat|sh      # 로컬 JAVA_HOME·CATALINA_OPTS 오버라이드
├── install-tomcat.*         # Tomcat 다운로드·압축 해제
├── deploy-wars.*            # Gradle bootWar + webapps 복사
├── start.* / stop.*         # Tomcat 기동·중지
├── apply-config.*           # setenv 복사 + server.xml UTF-8
├── verify-deploy.*          # 19 context health check
├── deploy-restart.*         # stop → deploy all → start → verify
└── README.md
```

| 경로 | 설명 |
|------|------|
| `apache-tomcat-10.1.34/` | Tomcat 설치본 (약 150MB, `install-tomcat` 실행 후 생성) |
| `webapps/{code}.war` | WAR 파일 (예: `sv.war`) |
| `webapps/{code}/` | Tomcat autoDeploy로 풀린 exploded 디렉터리 |

---

## 3. 스크립트 목록

### Windows

| 스크립트 | 설명 |
|----------|------|
| `install-tomcat.bat` | Tomcat 10.1.34 Windows zip 다운로드·압축 해제 |
| `deploy-wars.bat [코드…]` | WAR 빌드·배포 |
| `deploy-wars.bat all` | 19개 전체 (인자 없음과 동일) |
| `deploy-wars.bat sv` | SV만 빌드·배포 |
| `deploy-wars.bat sv cc ud` | 복수 선택 배포 |
| `start.bat` | Tomcat 기동 → 내부 `start.ps1` |
| `stop.bat` | Tomcat 중지 → 내부 `stop.ps1` |
| `apply-config.ps1` | UTF-8·setenv 적용 (`start` 시 자동 호출) |
| `verify-deploy.ps1` | 19 context `/actuator/health` 검증 |
| `deploy-restart.ps1` | stop → deploy all → start → health 대기 → verify |

> 프로젝트 경로에 괄호 `(23-08-15)`가 있어 `start.bat`/`stop.bat`은 **PowerShell 래퍼**(`start.ps1`/`stop.ps1`)를 사용합니다.

### Linux / macOS

| 스크립트 | 설명 |
|----------|------|
| `install-tomcat.sh` | tar.gz 다운로드·압축 해제 (`curl` 또는 `wget`) |
| `deploy-wars.sh [코드…]` | Windows `.bat`과 동일 시맨틱 |
| `start.sh` | `setenv.local.sh` + `apply-config.sh` + `catalina.sh start` |
| `stop.sh` | `catalina.sh stop` |
| `apply-config.sh` | setenv 복사 + `server.xml` UTF-8 |
| `verify-deploy.sh` | health check (curl) |
| `deploy-restart.sh` | 원클릭 전체 재배포 |

---

## 4. Tomcat 설치

`install-tomcat`은 **최초 1회**만 실행하면 됩니다. 이미 설치되어 있으면 스킵합니다.

| OS | 다운로드 | 설치 경로 |
|----|----------|-----------|
| Windows | `apache-tomcat-10.1.34-windows-x64.zip` | `ztomcat/apache-tomcat-10.1.34/` |
| Linux/macOS | `apache-tomcat-10.1.34.tar.gz` | 동일 |

소스: [Apache Tomcat Archive](https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.34/bin/)

---

## 5. 기동·중지

### 기동

```bat
start.bat          rem Windows
./start.sh         rem Linux
```

기동 시 자동 수행:

1. **JDK 21** 설정 (`%USERPROFILE%\.jdks\temurin-21.0.4` 또는 `JAVA_HOME`)
2. `apply-config` — `conf/setenv.*` → `bin/setenv.*` 복사, `server.xml` UTF-8
3. `catalina start`

접속: `http://localhost:8080`

### 중지

```bat
stop.bat
./stop.sh
```

---

## 6. WAR 배포 (deploy-wars)

Gradle `bootWar`로 WAR를 빌드한 뒤 `webapps/`에 복사합니다.

### 사용법

```bash
deploy-wars.sh              # 19개 전체
deploy-wars.sh all          # 동일
deploy-wars.sh sv           # SV만
deploy-wars.sh sv cc et     # 복수
deploy-wars.sh help         # 도움말
```

### 지원 코드 (19개)

```text
cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om ud et
```

| 코드 | Gradle 모듈 | WAR | Context |
|------|-------------|-----|---------|
| cc | `cc-service` | `cc.war` | `/cc` |
| ic | `ic-service` | `ic.war` | `/ic` |
| pc | `pc-service` | `pc.war` | `/pc` |
| bc | `bc-service` | `bc.war` | `/bc` |
| ms | `ms-service` | `ms.war` | `/ms` |
| sv | `sv-service` | `sv.war` | `/sv` |
| pd | `pd-service` | `pd.war` | `/pd` |
| cm | `cm-service` | `cm.war` | `/cm` |
| eb | `eb-service` | `eb.war` | `/eb` |
| ep | `ep-service` | `ep.war` | `/ep` |
| bp | `bp-service` | `bp.war` | `/bp` |
| bd | `bd-service` | `bd.war` | `/bd` |
| ss | `ss-service` | `ss.war` | `/ss` |
| cs | `cs-service` | `cs.war` | `/cs` |
| ct | `ct-service` | `ct.war` | `/ct` |
| mg | `mg-service` | `mg.war` | `/mg` |
| om | `om-service` | `om.war` | `/om` |
| ud | `common-updownload` | `ud.war` | `/ud` |
| et | `common-etc` | `et.war` | `/et` |

### 전체 vs 단건 배포

| 모드 | Tomcat 재기동 | 소요 시간 | 비고 |
|------|:-------------:|-----------|------|
| `all` (19개) | **권장** (실행 중이면 restart) | 빌드 2~3분 + 기동·배포 4~5분 | `deploy-restart` 사용 |
| 단건 (`sv` 등) | **불필요** | 빌드 ~30초 + autoDeploy ~15초 | WAR 교체 후 context 자동 재배포 |

단건 배포 시 스크립트가 해당 `webapps/{code}/` exploded 디렉터리를 삭제한 뒤 WAR를 복사합니다.

---

## 7. 배포 검증 (verify-deploy)

19개 context에 대해 `GET /{code}/actuator/health`를 호출합니다.

```powershell
verify-deploy.ps1
```

```bash
./verify-deploy.sh
```

출력 예:

```text
  OK   sv -> 200
  OK   et -> 200
[ztomcat] Result: 19 OK, 0 FAIL (total 19)
```

- 타임아웃: 요청당 30초
- 1개라도 FAIL이면 exit code 1 (shell)

---

## 8. 원클릭 재배포 (deploy-restart)

전체 WAR를 깨끗이 다시 올릴 때 사용합니다.

```powershell
deploy-restart.ps1
```

```bash
./deploy-restart.sh
```

순서:

1. `stop`
2. 3초 대기
3. `deploy-wars` (전체)
4. `start`
5. health 19/19 될 때까지 최대 ~6분 폴링 (15초 간격)
6. `verify-deploy`

---

## 9. 설정 (UTF-8·JVM)

### apply-config

`start`마다 실행됩니다.

| 작업 | 내용 |
|------|------|
| setenv 복사 | `ztomcat/conf/setenv.*` → `apache-tomcat-.../bin/setenv.*` |
| server.xml | 8080 Connector에 `URIEncoding="UTF-8" useBodyEncodingForURI="true"` |

### JVM 옵션 (기본)

| 옵션 | 값 |
|------|-----|
| Heap | `-Xms512m -Xmx1536m` |
| Encoding | `-Dfile.encoding=UTF-8` |
| Timezone | `-Duser.timezone=Asia/Seoul` |

Windows 추가: `-Dsun.stdout.encoding=UTF-8`, `-Dsun.stderr.encoding=UTF-8`

### 로컬 오버라이드

| 파일 | 용도 |
|------|------|
| `setenv.local.bat` | Windows — `JAVA_HOME`, `CATALINA_OPTS` |
| `setenv.local.sh` | Linux — `start.sh`/`deploy-wars.sh`에서 source |

Linux JDK 21 후보 경로 (`setenv.local.sh`):

- `~/.jdks/temurin-21.0.4`
- `/usr/lib/jvm/java-21-openjdk*`
- `/opt/homebrew/opt/openjdk@21`

---

## 10. 배포 URL·Context

### Health

```text
http://localhost:8080/{code}/actuator/health
```

### 온라인 거래 (POST JSON)

```text
POST http://localhost:8080/sv/online
POST http://localhost:8080/sv/SV/online
POST http://localhost:8080/cc/online
POST http://localhost:8080/et/online
POST http://localhost:8080/ud/online
```

- **Method:** POST only (`GET /online` → 404/405)
- **Content-Type:** `application/json`
- 샘플: [`docs/sample-requests/`](../docs/sample-requests/)

### demo-ui 연동

Tomcat 모드:

```yaml
# demo-ui/src/main/resources/application.yml
nsight:
  demo:
    deployment-mode: tomcat
    tomcat-gateway-url: http://localhost:8080
```

---

## 11. ztomcat vs scripts vs demo-ui

| 도구 | 역할 |
|------|------|
| [`scripts/`](../scripts/README.md) | Gradle `build-all.sh`, `run-local-sv.sh` (embedded bootRun) |
| **`ztomcat/`** | Tomcat 설치·WAR 배포·기동·검증 |
| [`demo-ui/`](../demo-ui/README.md) | 브라우저 Relay UI (`:8099`) |

```text
WAR만 빌드     →  scripts/build-all.sh  또는  ztomcat/deploy-wars
Tomcat 배포    →  ztomcat/deploy-wars
Tomcat 기동    →  ztomcat/start
검증           →  ztomcat/verify-deploy
```

---

## 12. 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| `/sv/online` 404, Spring 로그 없음 | Tomcat이 **JDK 18** 등으로 기동 | `start.ps1`/`start.sh`로 JDK 21 확인 |
| health 타임아웃 (19개 중 일부) | 19 WAR 순차 autoDeploy 중 | `deploy-restart` 후 4~5분 대기 |
| `GET /online` 404 | POST만 지원 | curl `-X POST` 또는 demo-ui |
| 8080 포트 충돌 | `cc-service` bootRun(8080) 등 | bootRun 중지 또는 Tomcat 포트 변경 |
| 한글 깨짐 | Connector encoding | `start` 재실행 (`apply-config`), 로그 UTF-8로 열기 |
| `gradle not found` | Gradle 미설치 | Gradle 8.x PATH 또는 `deploy-wars.bat`의 `GRADLE` 경로 수정 |
| 단건 배포 후에도 구버전 | exploded 캐시 | deploy-wars가 `{code}/` 삭제 후 WAR 복사 — 15초 대기 |

### 로그 확인

```text
ztomcat/apache-tomcat-10.1.34/logs/catalina.*.log
ztomcat/apache-tomcat-10.1.34/logs/localhost.*.log
```

Spring Boot 기동 성공 시 `Started *Application` 로그가 context별로 출력됩니다.

---

## 13. 관련 문서

- [프로젝트 README](../README.md) — 전체 가이드·모듈·포트
- [scripts/README.md](../scripts/README.md) — Gradle 빌드 단축
- [demo-ui/README.md](../demo-ui/README.md) — 브라우저 테스트
- [common-web/README.md](../common-web/README.md) — `/online` Controller·STF/ETF
- [deploy/apache/nsight-marketing-routing.conf](../deploy/apache/nsight-marketing-routing.conf) — Apache ProxyPass 예시
