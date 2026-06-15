# NSIGHT HTTP/JSON 표준 전문 온라인 거래 플랫폼

NSIGHT 마케팅플랫폼의 **비 REST HTTP/JSON 표준 전문** 처리 방식을 구현한 Spring Boot 멀티모듈 프로젝트입니다.  
업무 식별은 URL 리소스가 아니라 JSON Header의 `serviceId`, `transactionCode`, `processingType`으로 수행합니다.

| 항목 | 버전 |
|------|------|
| Java | 21 |
| Spring Boot | 3.3.5 |
| Gradle | 8.x (Wrapper 없음 — 로컬 `gradle` 사용) |
| Tomcat (WAR 배포) | 10.1.34 |
| Group | `com.nh.nsight.marketing` |

> **상세 문서:** 모듈·도구별 README가 [`common-core`](common-core/README.md), [`common-web`](common-web/README.md), [`demo-ui`](demo-ui/README.md), [`bin`](bin/README.md), [`scripts`](scripts/README.md), [`ztomcat`](ztomcat/README.md)에 있습니다. 이 문서는 **전체 개요·빠른 시작·참조표** 역할을 합니다.

---

## 목차

1. [처리 흐름](#1-처리-흐름)
2. [프로젝트 구조](#2-프로젝트-구조)
3. [업무 모듈·포트](#3-업무-모듈포트)
4. [빠른 시작](#4-빠른-시작)
5. [로컬 개발 (bootRun)](#5-로컬-개발-bootrun)
6. [Tomcat WAR 배포 (ztomcat)](#6-tomcat-war-배포-ztomcat)
7. [demo-ui 테스트 화면](#7-demo-ui-테스트-화면)
8. [표준 전문 구조](#8-표준-전문-구조)
9. [API 엔드포인트](#9-api-엔드포인트)
10. [공통 모듈 (ET / UD)](#10-공통-모듈-et--ud)
11. [업무 Handler 추가 방법](#11-업무-handler-추가-방법)
12. [설정 참고](#12-설정-참고)
13. [빌드·배포 도구](#13-빌드배포-도구)
14. [빌드·CI·Git](#14-빌드cigit)
15. [관련 문서](#15-관련-문서)
16. [트러블슈팅](#16-트러블슈팅)

---

## 1. 처리 흐름

```text
Client / WebTopSuite / demo-ui
  │  HTTP POST + application/json
  ▼
Apache / Tomcat (선택)
  ▼
STFFilter          … GUID·TraceId 생성, MDC, 요청 시작 로그
  ▼
OnlineTransactionController   … POST /online
  ▼
TCF.process()
  ├─ STF.preProcess()           … Header 검증, 세션/권한/중복(stub), 거래 시작
  ├─ TransactionDispatcher      … serviceId → TransactionHandler
  │    └─ Handler → Facade → Service → Rule → DAO/Mapper
  └─ ETF.success|fail|error     … 응답 조립, I/O 기록, Audit, Metrics
  ▼
ETFFilter          … 응답 완료 로그, MDC clear
  ▼
StandardResponse JSON
```

| 계층 | 모듈 | 상세 |
|------|------|------|
| DTO·Context | [`common-core`](common-core/README.md) | `StandardRequest/Response`, `TransactionContext` |
| Web 파이프라인 | [`common-web`](common-web/README.md) | STF/TCF/ETF, Filter, Dispatcher, Controller |
| 업무 로직 | `*-service` | `TransactionHandler` → Facade → Service → DAO |
| 아키텍처 | [`docs/architecture.md`](docs/architecture.md) | URL 원칙·레거시 다이어그램 |
| **거래 호출 흐름** | [`README-TXFLOW.md`](README-TXFLOW.md) | 클래스·함수 단위 순서 |

---

## 2. 프로젝트 구조

```text
nsight-httpjson-standard/
├── common-core/          → common-core/README.md
├── common-web/           → common-web/README.md
├── cc-service … om-service/   # 17개 업무 WAR (SV → sv-service/README.md)
├── common-etc/           # ET — 거래 I/O 기록
├── common-updownload/    # UD — 파일 업·다운로드
├── demo-ui/              → demo-ui/README.md
├── bin/                  → bin/README.md
├── scripts/              → scripts/README.md
├── ztomcat/              → ztomcat/README.md
├── docs/
│   ├── architecture.md
│   └── sample-requests/
└── deploy/apache/        # Apache ProxyPass 예시
```

### Gradle 모듈 (`settings.gradle`)

| 구분 | 모듈 | README |
|------|------|--------|
| 공통 라이브러리 | `common-core`, `common-web` | ✓ |
| 업무 서비스 (17) | `cc-service` … `om-service` | — |
| 공통 WAR | `common-etc`, `common-updownload` | §10 |
| 데모 | `demo-ui` | ✓ |

### 레이어 패턴 (업무 모듈)

```text
OnlineTransactionController (/online)     ← common-web
  → TransactionHandler   (@Component, serviceId)
  → Facade → Service → Rule → DAO / MyBatis Mapper
```

Handler·Dispatcher 상세: [`common-web/README.md`](common-web/README.md#6-handler-디스패치)

---

## 3. 업무 모듈·포트

### 17개 업무 WAR

| No | 코드 | 영문명 | Gradle | WAR | bootRun | Tomcat URL |
|---:|------|--------|--------|-----|--------:|------------|
| 1 | CC | Common | `cc-service` | `cc.war` | 8080 | `http://localhost:8080/cc/online` |
| 2 | IC | Integration Customer | `ic-service` | `ic.war` | 8081 | `/ic/online` |
| 3 | PC | Private Customer | `pc-service` | `pc.war` | 8082 | `/pc/online` |
| 4 | BC | Business Customer | `bc-service` | `bc.war` | 8083 | `/bc/online` |
| 5 | MS | Mini Single View | `ms-service` | `ms.war` | 8084 | `/ms/online` |
| 6 | SV | Single View | `sv-service` | `sv.war` | 8085 | `/sv/online` |
| 7 | PD | Product | `pd-service` | `pd.war` | 8086 | `/pd/online` |
| 8 | CM | Campaign | `cm-service` | `cm.war` | 8087 | `/cm/online` |
| 9 | EB | EBM | `eb-service` | `eb.war` | 8088 | `/eb/online` |
| 10 | EP | Event Processing | `ep-service` | `ep.war` | 8089 | `/ep/online` |
| 11 | BP | Behavior Processing | `bp-service` | `bp.war` | 8090 | `/bp/online` |
| 12 | BD | Behavior Data | `bd-service` | `bd.war` | 8091 | `/bd/online` |
| 13 | SS | Sales Support | `ss-service` | `ss.war` | 8092 | `/ss/online` |
| 14 | CS | Common Service | `cs-service` | `cs.war` | 8093 | `/cs/online` |
| 15 | CT | Contents | `ct-service` | `ct.war` | 8094 | `/ct/online` |
| 16 | MG | Message | `mg-service` | `mg.war` | 8095 | `/mg/online` |
| 17 | OM | Operation Management | `om-service` | `om.war` | 8096 | `/om/online` |

### 공통·데모

| 코드 | 모듈 | 산출물 | bootRun | Tomcat |
|------|------|--------|--------:|--------|
| UD | `common-updownload` | `ud.war` | 8097 | `/ud/...` |
| ET | `common-etc` | `et.war` | 8098 | `/et/...` |
| — | `demo-ui` | `demo-ui.jar` | **8099** | (별도 JAR) |

> **주의:** `cc-service` bootRun(8080)과 Tomcat(8080) 포트가 겹칩니다. 동시에 쓰지 마세요.

---

## 4. 빠른 시작

### 사전 요구

- **JDK 21** (`JAVA_HOME` 설정) — Tomcat·WAR 실행에 필수
- **Gradle 8.x** (PATH에 `gradle`)
- (Tomcat 배포 시) Windows PowerShell 또는 Linux shell

### ① 단일 업무 embedded 실행 (가장 간단)

```bash
gradle :sv-service:bootRun
# 또는
scripts/run-local-sv.sh      # Linux/Git Bash
scripts\run-local-sv.bat     # Windows CMD
```

```bash
curl -X POST http://localhost:8085/sv/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/sv-sample-inquiry.json
```

bootRun URL: `http://localhost:{port}/online` 또는 `/{code}/online` — [`common-core`](common-core/README.md#7-로컬-실행-boot) `LocalBootRun`

### ② Tomcat에 19개 WAR 배포

```bat
cd ztomcat
install-tomcat.bat
deploy-wars.bat all
start.bat
verify-deploy.ps1
```

Linux: `install-tomcat.sh` → `deploy-wars.sh all` → `start.sh` → `verify-deploy.sh`

상세: [`ztomcat/README.md`](ztomcat/README.md#1-빠른-시작)

```bash
curl http://localhost:8080/sv/actuator/health
curl -X POST http://localhost:8080/sv/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/sv-sample-inquiry.json
```

### ③ demo-ui로 브라우저 테스트

```bash
gradle :demo-ui:bootRun
# 또는 demo-ui/run-demo-ui.sh
```

브라우저: `http://localhost:8099` — 상세: [`demo-ui/README.md`](demo-ui/README.md)

Tomcat 연동 시:

```yaml
nsight:
  demo:
    deployment-mode: tomcat
    tomcat-gateway-url: http://localhost:8080
```

---

## 5. 로컬 개발 (bootRun)

각 `*Application.java`는 `LocalBootRun.apply(포트)`로 포트·프로필(`local`)을 고정합니다.  
WAR 배포용 `NsightBootApplication`은 [`common-web`](common-web/README.md#10-war-배포-nsightbootapplication)에 있습니다.

```bash
gradle :sv-service:bootRun
gradle :common-etc:bootRun
gradle :demo-ui:bootRun
```

| 실행 방식 | URL 패턴 |
|-----------|----------|
| bootRun | `http://localhost:{port}/online` |
| bootRun | `http://localhost:{port}/{code}/online` |

편의 스크립트: [`scripts/run-local-sv.sh`](scripts/README.md#4-run-local-svsh)

---

## 6. Tomcat WAR 배포 (ztomcat)

**전체 가이드:** [`ztomcat/README.md`](ztomcat/README.md)

### 핵심 요약

| 항목 | 내용 |
|------|------|
| Tomcat | 10.1.34, 포트 **8080** |
| JDK | **21 필수** — JDK 18이면 Spring 미기동 → `/online` 404 |
| WAR | 19개 (업무 17 + ET + UD) |
| 단건 배포 | `deploy-wars sv` — Tomcat 재기동 **불필요** (~15초 autoDeploy) |
| 전체 배포 | `deploy-restart` — stop → deploy all → start → verify (~5분) |

### 스크립트 (Windows / Linux)

| Windows | Linux | 설명 |
|---------|-------|------|
| `install-tomcat.bat` | `install-tomcat.sh` | Tomcat 설치 |
| `deploy-wars.bat [코드…]` | `deploy-wars.sh [코드…]` | 빌드 + webapps 복사 |
| `start.bat` / `stop.bat` | `start.sh` / `stop.sh` | 기동·중지 |
| `verify-deploy.ps1` | `verify-deploy.sh` | 19 context health |
| `deploy-restart.ps1` | `deploy-restart.sh` | 원클릭 전체 재배포 |

---

## 7. demo-ui 테스트 화면

**전체 가이드:** [`demo-ui/README.md`](demo-ui/README.md)

demo-ui는 **Relay 서버**입니다. 브라우저 → demo-ui API → 업무 WAS `/{code}/online`.

| 항목 | 값 |
|------|-----|
| 포트 | **8099** |
| 허브 | `http://localhost:8099/index.html` |
| 업무 단건 | `http://localhost:8099/{code}/index.html` |
| 업무 다건 | `http://localhost:8099/{code}/index-multi.html` |

주요 Relay API:

| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/business-modules` | 업무 모듈 목록 |
| POST | `/api/relay/{code}/online` | JSON Relay |
| GET | `/api/etc/transaction-logs` | ET 거래 I/O 로그 |

---

## 8. 표준 전문 구조

**필드·검증·Result 상세:** [`common-core/README.md`](common-core/README.md#3-표준-전문-message)

### 요청 (`StandardRequest<T>`)

```json
{
  "header": { ... StandardHeader ... },
  "body": { ... 업무별 필드 ... }
}
```

### 응답 (`StandardResponse<T>`)

```json
{
  "header": { ... },
  "result": {
    "status": "SUCCESS",
    "resultCode": "S0000",
    "message": "정상 처리되었습니다.",
    "elapsedTimeMs": 42
  },
  "body": { ... }
}
```

업무 실패·시스템 오류도 HTTP **200** + `result.status` (`FAIL` / `ERROR`) — [`common-web`](common-web/README.md#2-처리-흐름)

### Header 필수 필드

| 필드 | 설명 |
|------|------|
| `systemId` | 시스템 ID (예: `NSIGHT-MP`) |
| `businessCode` | 업무코드 (예: `SV`) |
| `serviceId` | **Handler 라우팅 키** (예: `SV.Sample.inquiry`) |
| `transactionCode` | 거래코드 (예: `SV-INQ-0001`) |
| `processingType` | `INQUIRY`, `CREATE`, `UPDATE`, `DELETE`, `EXECUTE` 등 |

샘플: [`docs/sample-requests/sv-sample-inquiry.json`](docs/sample-requests/sv-sample-inquiry.json)

---

## 9. API 엔드포인트

**Controller·Filter 상세:** [`common-web/README.md`](common-web/README.md#4-http-엔드포인트)

### 온라인 거래 (17 업무 공통)

| Method | Path | Content-Type |
|--------|------|--------------|
| POST | `/online` | `application/json` |
| POST | `/{businessCode}/online` | `application/json` |

> GET `/online` → 404/405. **POST + JSON**만 지원합니다.

| 배포 | 예 (SV) |
|------|---------|
| bootRun (:8085) | `POST http://localhost:8085/sv/online` |
| Tomcat (/sv) | `POST http://localhost:8080/sv/online` |

### Actuator

| Path | 설명 |
|------|------|
| `/actuator/health` | 헬스 체크 (`verify-deploy`에서 사용) |
| `/actuator/info` | 정보 |
| `/actuator/metrics` | 메트릭 |

### common-etc (ET)

| Method | Path |
|--------|------|
| POST | `/et/transaction-io/record` |
| DELETE | `/et/transaction-io/logs` |
| POST | `/et/transaction-io/logs/delete` |

### common-updownload (UD)

| Method | Path |
|--------|------|
| POST | `/ud/files/upload` |
| GET | `/ud/files/{fileId}/download` |
| GET | `/ud/files` |

---

## 10. 공통 모듈 (ET / UD)

### common-etc (ET) — 거래 I/O 기록

- 업무 ETF가 `TransactionIoRecordPublisher`로 input/output Header·Result 기록 ([`common-web`](common-web/README.md#9-부가-서비스))
- H2 파일 DB: `./data/etc/tx-io-meta`
- bootRun `:8098`, Tomcat `/et`

```yaml
nsight:
  etc:
    record-enabled: true
    record-url: http://localhost:8098/et/transaction-io/record   # bootRun
    # record-url: http://localhost:8080/et/transaction-io/record  # Tomcat
```

### common-updownload (UD) — 파일 업·다운로드

- H2 + 로컬 파일 (`./data/updownload`)
- 최대 50MB (`nsight.updownload.max-file-size-mb`)
- bootRun `:8097`, Tomcat `/ud`

---

## 11. 업무 Handler 추가 방법

**Dispatcher·인터페이스:** [`common-web/README.md`](common-web/README.md#6-handler-디스패치)

1. `{code}-service`에 `TransactionHandler` 구현 — `@Component`
2. `getServiceId()` — Header `serviceId`와 **동일 문자열**
3. `doHandle(context, body)` — Facade → Service → Rule → DAO
4. MyBatis XML: `src/main/resources/mapper/{code}/`
5. 샘플 JSON: `docs/sample-requests/{code}-sample-inquiry.json`

예: `sv-service/.../handler/SvSampleInquiryHandler.java` — `serviceId`: `SV.Sample.inquiry`  
**SV 모듈 상세:** [`sv-service/README.md`](sv-service/README.md)

---

## 12. 설정 참고

**Properties 전체:** [`common-web/README.md`](common-web/README.md#8-설정-properties)

### 업무 서비스 (`application.yml` 요약)

```yaml
spring:
  profiles.active: local
  datasource:
    url: jdbc:h2:mem:nsight_sv;MODE=Oracle;DB_CLOSE_DELAY=-1

nsight:
  module:
    business-code: SV
    session-validation-enabled: false
    authorization-validation-enabled: false
    idempotency-enabled: false
  etc:
    record-enabled: true
    record-url: http://localhost:8098/et/transaction-io/record
```

### H2 사용 방식

| 모듈 | DB |
|------|-----|
| 17 업무 | 인메모리 `jdbc:h2:mem:nsight_{code}` |
| ET, UD | 파일 `./data/...` (영속) |

---

## 13. 빌드·배포 도구

| 목적 | 도구 | 명령 예 | 상세 |
|------|------|---------|------|
| 업무 WAR 17개 → `bin/` 수집 | [`bin/`](bin/README.md) | `bin/build-wars.sh sv` | ET·UD 미포함 |
| 전체 clean build + 19 WAR | [`scripts/`](scripts/README.md) | `scripts/build-all.sh` | test 포함 |
| SV bootRun | [`scripts/`](scripts/README.md) | `scripts/run-local-sv.sh` | :8085 |
| WAR 빌드 + Tomcat 배포 | [`ztomcat/`](ztomcat/README.md) | `deploy-wars.sh sv` | 19개 지원 |
| Tomcat 기동·검증 | [`ztomcat/`](ztomcat/README.md) | `start.sh`, `verify-deploy.sh` | JDK 21 |

| Gradle (직접 호출) | [`docs/gradle/README.md`](docs/gradle/README.md) | `gradle bootWar`, `:sv-service:bootRun` |

---

## 14. 빌드·CI·Git

**Gradle 환경 상세:** [`docs/gradle/README.md`](docs/gradle/README.md) — JDK 21 Toolchain, 모듈 구조, 태스크, CI, Wrapper

### GitLab CI (`.gitlab-ci.yml`)

`compileJava` → `test` → `bootWar` (WAR artifact 7일 보관)

### Git 브랜치

| 브랜치 | 용도 |
|--------|------|
| `main` | 운영/릴리스 |
| `develop` | 개발 통합 |

---

## 15. 관련 문서

| 문서 | 내용 |
|------|------|
| [`common-core/README.md`](common-core/README.md) | `StandardHeader/Request/Response`, `TransactionContext`, `LocalBootRun` |
| [`common-web/README.md`](common-web/README.md) | STF/TCF/ETF, Filter, Controller, Handler, `NsightBootApplication` |
| [`demo-ui/README.md`](demo-ui/README.md) | Relay UI, 화면, API, bootrun/tomcat 모드 |
| [`bin/README.md`](bin/README.md) | 업무 WAR 17개 빌드 → `bin/{code}.war` |
| [`scripts/README.md`](scripts/README.md) | `build-all.sh`, `run-local-sv.sh` |
| [`ztomcat/README.md`](ztomcat/README.md) | Tomcat 설치·배포·JDK 21·verify |
| [`sv-service/README.md`](sv-service/README.md) | SV 업무 WAS — 샘플 Handler·레이어·실행 (업무 모듈 템플릿) |
| [`README-TXFLOW.md`](README-TXFLOW.md) | 온라인 거래 **클래스·함수 단위** 호출 흐름 |
| [`docs/gradle/README.md`](docs/gradle/README.md) | Gradle 멀티모듈·JDK 21·태스크·CI |
| [`docs/architecture.md`](docs/architecture.md) | 아키텍처·URL 원칙 |
| [`deploy/apache/nsight-marketing-routing.conf`](deploy/apache/nsight-marketing-routing.conf) | Apache ProxyPass 예시 |
| [`docs/sample-requests/`](docs/sample-requests/) | 업무별 curl 샘플 JSON |

---

## 16. 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| Tomcat `/sv/online` 404, Spring 로그 없음 | JDK 18 등으로 Tomcat 기동 | [`ztomcat/start`](ztomcat/README.md#5-기동중지) — JDK 21 |
| health 일부 FAIL | 19 WAR 순차 autoDeploy 중 | `deploy-restart` 후 4~5분 대기 |
| GET `/online` 404/405 | POST만 지원 | curl `-X POST` 또는 demo-ui |
| H2 file lock (UD/ET) | 중복 프로세스 | 기존 프로세스 종료 후 재실행 |
| CC bootRun + Tomcat 충돌 | 둘 다 8080 | 하나만 사용 |
| demo-ui Relay 실패 | 대상 WAS 미기동 | bootRun 또는 Tomcat + deploy 확인 |
| ET I/O 기록 실패 | ET 미기동 | `common-etc` bootRun(:8098) 또는 Tomcat `/et` |

모듈별 트러블슈팅: [`ztomcat/README.md#12`](ztomcat/README.md#12-트러블슈팅), [`demo-ui/README.md#8`](demo-ui/README.md#8-트러블슈팅), [`scripts/README.md#6`](scripts/README.md#6-트러블슈팅)
