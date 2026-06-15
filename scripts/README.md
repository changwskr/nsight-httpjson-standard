# scripts — 프로젝트 루트 Gradle 편의 스크립트

Linux/macOS/Git Bash 및 **Windows CMD**에서 **반복 Gradle 명령**을 짧게 실행하기 위한 스크립트 모음입니다. Tomcat 설치·배포는 [`ztomcat/`](../ztomcat/README.md), demo-ui 전용 실행은 [`demo-ui/run-demo-ui.sh`](../demo-ui/run-demo-ui.sh)를 사용합니다.

| 항목 | 값 |
|------|-----|
| 위치 | `scripts/` |
| 실행 OS | Linux, macOS, Git Bash, **Windows CMD** |
| 사전 요구 | **JDK 21**, **Gradle 8.x** (`PATH`에 `gradle`) |
| 실행 위치 | **프로젝트 루트** (`nsight-httpjson-standard/`) |

> 이 저장소에는 Gradle Wrapper(`gradlew`)가 **없습니다**. 스크립트는 `./gradlew`가 있으면 우선 사용하고, 없으면 시스템 `gradle`을 호출합니다.

---

## 목차

1. [scripts vs ztomcat vs demo-ui](#1-scripts-vs-ztomcat-vs-demo-ui)
2. [스크립트 목록](#2-스크립트-목록)
3. [build-all.sh](#3-build-allsh)
4. [run-local-sv.sh](#4-run-local-svsh)
5. [Windows에서 동일 작업](#5-windows에서-동일-작업)
6. [트러블슈팅](#6-트러블슈팅)
7. [관련 문서](#7-관련-문서)

---

## 1. scripts vs ztomcat vs demo-ui

| 디렉터리 | 역할 | 예시 |
|----------|------|------|
| **`scripts/`** | Gradle **빌드·bootRun** 단축 | 전체 WAR 빌드, SV 로컬 기동 |
| **`ztomcat/`** | Tomcat **설치·WAR 배포·기동** | `deploy-wars.sh sv`, `start.sh` |
| **`demo-ui/`** | demo-ui **JAR bootRun** | `run-demo-ui.sh` |

```text
로컬 개발 (embedded)
  scripts/run-local-sv.sh  →  :8085/sv/online

Tomcat 배포
  scripts/build-all.sh     →  WAR 산출물 생성
  ztomcat/deploy-wars.sh   →  webapps/ 복사
  ztomcat/start.sh         →  :8080/{ctx}/online

브라우저 테스트
  demo-ui/run-demo-ui.sh   →  :8099
```

---

## 2. 스크립트 목록

| 파일 | 설명 |
|------|------|
| [`build-all.sh`](build-all.sh) / [`build-all.bat`](build-all.bat) | `clean build bootWar` — 19개 WAR + 테스트 포함 전체 빌드 |
| [`run-local-sv.sh`](run-local-sv.sh) / [`run-local-sv.bat`](run-local-sv.bat) | `:sv-service:bootRun` — SV 업무 embedded 서버 기동 |

공통 동작:

- `set -euo pipefail` — 오류 시 즉시 종료
- `./gradlew` 실행 가능 시 Wrapper 우선, 아니면 `gradle` 사용

---

## 3. build-all.sh

### 하는 일

프로젝트 **전 모듈**에 대해 다음 Gradle 태스크를 순서대로 실행합니다.

```bash
clean → build → bootWar
```

- **compile + test** (`build`)
- **WAR 산출** (`bootWar`) — 17개 업무 + `common-etc` + `common-updownload` (총 19개)
- `demo-ui`는 JAR 모듈이라 `bootWar` 대상이 아닙니다

### 사용법

```bash
# 프로젝트 루트에서
chmod +x scripts/build-all.sh   # 최초 1회
./scripts/build-all.sh
```

또는

```bash
bash scripts/build-all.sh
```

### WAR 출력 위치

각 `*-service`, `common-etc`, `common-updownload` 모듈의 `build/libs/`:

| 예 | 경로 |
|----|------|
| SV | `sv-service/build/libs/sv.war` |
| ET | `common-etc/build/libs/et.war` |

빌드만 하고 Tomcat에 올리려면 이어서 [`ztomcat/deploy-wars.sh`](../ztomcat/deploy-wars.sh)를 실행합니다.

```bash
./scripts/build-all.sh
cd ztomcat && ./deploy-wars.sh all
```

---

## 4. run-local-sv.sh

### 하는 일

**Single View(SV)** 업무 모듈을 Spring Boot embedded Tomcat으로 기동합니다.

```bash
gradle :sv-service:bootRun
```

내부적으로 `LocalBootRun.apply(8085)`가 적용되어 포트 **8085**, 프로필 **`local`** 로 실행됩니다.

### 사용법

```bash
./scripts/run-local-sv.sh
```

기동 후:

| 항목 | 값 |
|------|-----|
| Health | `http://localhost:8085/actuator/health` |
| 온라인 거래 | `POST http://localhost:8085/online` 또는 `/sv/online` |
| 샘플 JSON | [`docs/sample-requests/sv-sample-inquiry.json`](../docs/sample-requests/sv-sample-inquiry.json) |

```bash
curl -X POST http://localhost:8085/sv/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/sv-sample-inquiry.json
```

### ET I/O 기록 연동

`sv-service`의 `application.yml`에서 `nsight.etc.record-enabled: true`이면 **common-etc**가 떠 있어야 합니다.

```bash
# 다른 터미널
gradle :common-etc:bootRun    # :8098
```

---

## 5. Windows (CMD)

| scripts | 명령 |
|---------|------|
| `scripts\build-all.bat` | `gradle clean build bootWar` |
| `scripts\run-local-sv.bat` | `gradle :sv-service:bootRun` |

프로젝트 루트 또는 `scripts\`에서 실행 가능합니다 (`%~dp0..`로 루트 이동).

Tomcat 배포는 [`ztomcat/*.bat`](../ztomcat/README.md)을 사용합니다.

Git Bash/WSL에서는 `*.sh`를 사용하세요.

---

## 6. 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| `gradle: command not found` | Gradle 미설치 | Gradle 8.x 설치 후 `PATH` 설정 |
| Java 버전 오류 | JDK 21 아님 | `JAVA_HOME`을 JDK 21로 설정 |
| `Permission denied` | 실행 권한 없음 | `chmod +x scripts/*.sh` |
| SV 기동 후 ET 기록 실패 | ET 미기동 | `gradle :common-etc:bootRun` (:8098) |
| `./gradlew` 관련 메시지 없음 | Wrapper 없음 | 정상 — 시스템 `gradle` 사용 |
| Tomcat 8080과 SV 충돌 | CC bootRun(8080) 등 | SV는 8085 — CC bootRun과 Tomcat 동시 사용 주의 |

---

## 7. 관련 문서

- [프로젝트 README](../README.md) — 빠른 시작·모듈·포트
- [ztomcat/README.md](../ztomcat/README.md) — Tomcat 설치·배포·검증
- [demo-ui/README.md](../demo-ui/README.md) — 브라우저 테스트 UI
- [docs/sample-requests/](../docs/sample-requests/) — curl용 샘플 JSON
