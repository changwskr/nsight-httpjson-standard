# demo-ui — NSIGHT 온라인 거래 테스트 UI

WebTopSuite/Client 없이 **브라우저에서 표준 HTTP/JSON 전문**을 작성·전송·응답 확인하기 위한 Spring Boot 애플리케이션입니다.

| 항목 | 값 |
|------|-----|
| Gradle 모듈 | `demo-ui` |
| 산출물 | `demo-ui.jar` (WAR 아님) |
| 포트 | **8099** |
| 의존성 | `spring-boot-starter-web` only |

> demo-ui는 **Relay 서버** 역할을 합니다. 브라우저 → demo-ui API → 실제 업무 WAS(`/{code}/online`)로 JSON을 전달합니다.

---

## 목차

1. [실행 방법](#1-실행-방법)
2. [화면 구성](#2-화면-구성)
3. [배포 모드 (bootrun / tomcat)](#3-배포-모드-bootrun--tomcat)
4. [REST API](#4-rest-api)
5. [샘플 전문·거래 카탈로그](#5-샘플-전문거래-카탈로그)
6. [디렉터리 구조](#6-디렉터리-구조)
7. [특수 화면 (ET / UD)](#7-특수-화면-et--ud)
8. [트러블슈팅](#8-트러블슈팅)

---

## 1. 실행 방법

### bootRun

```bash
# 프로젝트 루트에서
gradle :demo-ui:bootRun
```

또는

```bat
cd demo-ui
run-demo-ui.bat      :: Windows
./run-demo-ui.sh     :: Linux/macOS
```

브라우저: **http://localhost:8099**

### 사전 조건

테스트하려는 **대상 WAS가 기동**되어 있어야 합니다.

| 배포 모드 | 예: SV 테스트 전 |
|-----------|------------------|
| `bootrun` | `gradle :sv-service:bootRun` (포트 8085) |
| `tomcat` | `ztomcat/start.bat` + `deploy-wars.bat sv` |

ET 거래 I/O 기록·로그 화면 사용 시 **common-etc**(8098)도 기동하세요.

---

## 2. 화면 구성

### 허브 (업무 선택)

| URL | 설명 |
|-----|------|
| `/index.html` | 19개 업무 모듈 카드 — 각 업무 테스트 화면 링크 |

### 업무별 화면 (`/static/{code}/`)

| URL | 설명 |
|-----|------|
| `/{code}/index.html` | **단일 서비스** 테스트 — 샘플 1건, JSON 편집 후 전송 |
| `/{code}/index-multi.html` | **다중 거래** 테스트 — 조회/등록/수정 등 여러 `serviceId` 선택 |

예:

```text
http://localhost:8099/sv/index.html
http://localhost:8099/sv/index-multi.html
http://localhost:8099/cc/index.html
```

### ET / UD 전용

| URL | 설명 |
|-----|------|
| `/et/transaction-log.html` | common-etc에 기록된 거래 I/O 조회·삭제 |
| `/ud/updownload.html` | 파일 업로드·목록·다운로드 |

공통 UI 리소스: `/static/_shared/online.css`, `online-single.js`, `online-multi.js`

---

## 3. 배포 모드 (bootrun / tomcat)

`application.yml` 또는 화면에서 선택합니다.

```yaml
# demo-ui/src/main/resources/application.yml
nsight:
  demo:
    deployment-mode: bootrun          # bootrun | tomcat
    bootrun-host: http://localhost
    tomcat-gateway-url: http://localhost:8080
```

### bootrun (기본)

업무별 **embedded 포트**로 직접 Relay합니다.

```text
SV → http://localhost:8085/sv/online
CC → http://localhost:8080/cc/online
ET → http://localhost:8098/et/online
```

### tomcat

Tomcat Gateway **한 포트(8080)** 로 Relay합니다.

```text
SV → http://localhost:8080/sv/online
ET → http://localhost:8080/et/online
```

Tomcat 사용 시 설정 예:

```yaml
nsight:
  demo:
    deployment-mode: tomcat
    tomcat-gateway-url: http://localhost:8080
```

화면의 **배포 모드** 드롭다운은 API 쿼리 파라미터(`deploymentMode`, `bootrunHost`, `tomcatGatewayUrl`)로 런타임에도 변경 가능합니다.

---

## 4. REST API

demo-ui가 제공하는 백엔드 API입니다. 정적 HTML이 이 API를 호출한 뒤 실제 WAS로 Relay합니다.

### 설정·카탈로그

| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/config` | `deploymentMode`, `tomcatGatewayUrl`, `bootrunHost` |
| GET | `/api/business-modules` | 업무 모듈 목록 + 샘플 inquiry JSON |
| GET | `/api/business-modules/{code}` | 단일 모듈 정보 |
| GET | `/api/business-modules/{code}/target-url` | Relay 대상 URL 계산 |

### 온라인 거래 Relay (단일)

| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/relay/{code}/online` | JSON Body → `{base}/{code}/online` POST |

**응답 (`RelayResult`):**

```json
{
  "businessCode": "SV",
  "targetUrl": "http://localhost:8085/sv/online",
  "httpStatus": 200,
  "elapsedMs": 142,
  "responseBody": "{ ... StandardResponse JSON ... }"
}
```

대상 WAS 미기동 시 `httpStatus: 502`와 hint JSON이 반환됩니다.

### 다중 거래

| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/multi/business-modules` | 모듈별 거래 목록 |
| GET | `/api/multi/business-modules/{code}` | 단일 모듈 거래 목록 |
| GET | `/api/multi/business-modules/{code}/transactions/{id}` | 거래 1건 샘플 |
| GET | `/api/multi/business-modules/{code}/target-url` | Relay URL |
| POST | `/api/multi/relay/{code}/online` | JSON Relay |

### ET (거래 I/O 로그)

| Method | Path | 설명 |
|--------|------|------|
| DELETE | `/api/etc/transaction-logs` | ET 로그 전체 삭제 Relay |
| POST | `/api/etc/transaction-logs/delete` | 위와 동일 (브라우저 호환) |

→ 대상: `{base}/et/et/transaction-io/logs/delete` (bootrun) 또는 `{gateway}/et/et/transaction-io/logs/delete` (tomcat)

### UD (파일 업·다운로드)

| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/updownload/base-url` | UD 서비스 base URL |
| POST | `/api/updownload/upload` | multipart 파일 업로드 Relay |
| GET | `/api/updownload/files` | 파일 목록 Relay |
| GET | `/api/updownload/files/{fileId}/download` | 파일 다운로드 Relay |

---

## 5. 샘플 전문·거래 카탈로그

### 단일 화면 (`BusinessModuleCatalog`)

classpath `sample-requests/{code}-sample-inquiry.json`을 읽어 초기 JSON을 채웁니다.

예: `sample-requests/sv-sample-inquiry.json`

### 다중 화면 (`BusinessTransactionCatalog`)

1. **`{code}-transactions.json`이 있으면** — manifest에 정의된 거래 목록 사용  
2. **없으면** — inquiry JSON 기반으로 조회/등록/수정 3건 자동 생성

**manifest 예** (`sample-requests/sv-transactions.json`):

```json
[
  { "label": "Sample Inquiry", "sampleFile": "sv-sample-inquiry.json" },
  {
    "label": "Customer Detail Inquiry",
    "sampleRequest": { "header": { ... }, "body": { ... } }
  }
]
```

### 지원 업무코드 (19)

| 코드 | bootRun 포트 | Context |
|------|-------------:|---------|
| CC | 8080 | `/cc` |
| IC | 8081 | `/ic` |
| PC | 8082 | `/pc` |
| BC | 8083 | `/bc` |
| MS | 8084 | `/ms` |
| SV | 8085 | `/sv` |
| PD | 8086 | `/pd` |
| CM | 8087 | `/cm` |
| EB | 8088 | `/eb` |
| EP | 8089 | `/ep` |
| BP | 8090 | `/bp` |
| BD | 8091 | `/bd` |
| SS | 8092 | `/ss` |
| CS | 8093 | `/cs` |
| CT | 8094 | `/ct` |
| MG | 8095 | `/mg` |
| OM | 8096 | `/om` |
| UD | 8097 | `/ud` |
| ET | 8098 | `/et` |

샘플 JSON 추가·수정: `src/main/resources/sample-requests/`

---

## 6. 디렉터리 구조

```text
demo-ui/
├── README.md
├── run-demo-ui.bat / .sh
├── build.gradle
└── src/main/
    ├── java/.../demo/
    │   ├── DemoUiApplication.java
    │   ├── config/          DemoUiProperties, DemoUiConfiguration
    │   ├── controller/      DemoApiController, EtcApiController, UpdownloadApiController
    │   ├── service/         Catalog, TransactionRelayService, Etc/Updownload Relay
    │   └── model/           BusinessModuleInfo, RelayResult, ...
    └── resources/
        ├── application.yml
        ├── sample-requests/     업무별 JSON, *-transactions.json
        └── static/
            ├── index.html           허브
            ├── _shared/             CSS, JS
            ├── {code}/index.html    단일 테스트
            ├── {code}/index-multi.html
            ├── et/transaction-log.html
            └── ud/updownload.html
```

### 처리 흐름 (단일 거래)

```text
브라우저 (index.html)
  → GET /api/business-modules, /api/config
  → POST /api/relay/{code}/online?deploymentMode=...
       → TransactionRelayService (RestClient)
            → POST http://localhost:{port}/{code}/online
  → RelayResult → 화면에 HTTP 상태·응답 JSON 표시
```

---

## 7. 특수 화면 (ET / UD)

### ET — 트랜잭션 로그 (`/et/transaction-log.html`)

- 업무 WAS가 거래 시 **common-etc**에 I/O Header/Result를 기록 (`nsight.etc.record-enabled: true`)
- 이 화면에서 ET API를 조회·삭제
- ET 서비스(8098 또는 Tomcat `/et`) 기동 필요

### UD — 파일 업·다운로드 (`/ud/updownload.html`)

- **common-updownload** (8097 또는 Tomcat `/ud`)로 Relay
- 업로드·목록·다운로드 테스트

---

## 8. 트러블슈팅

| 증상 | 확인 |
|------|------|
| HTTP 502 / connection error | 대상 WAS 기동 여부, 배포 모드·포트 일치 |
| Tomcat 404 | JDK 21 Tomcat, WAR 배포 완료 여부 (`verify-deploy`) |
| ET 로그 비어 있음 | 업무 `record-enabled: true`, ET(8098) 기동 |
| CC + Tomcat 동시 사용 | 둘 다 8080 — 하나만 사용 |
| IDE에서 demo-ui 포트 이상 | `DemoUiApplication`이 8099 고정 (다른 모듈 yml 혼입 방지) |

### curl로 Relay API 직접 호출

```bash
curl -X POST "http://localhost:8099/api/relay/sv/online?deploymentMode=bootrun" \
  -H "Content-Type: application/json" \
  -d @../docs/sample-requests/sv-sample-inquiry.json
```

Tomcat 모드:

```bash
curl -X POST "http://localhost:8099/api/relay/sv/online?deploymentMode=tomcat&tomcatGatewayUrl=http://localhost:8080" \
  -H "Content-Type: application/json" \
  -d @../docs/sample-requests/sv-sample-inquiry.json
```

---

## 관련 문서

- [프로젝트 README](../README.md) — 전체 가이드
- [docs/architecture.md](../docs/architecture.md) — STF/ETF/TCF 처리 흐름
- [docs/sample-requests/](../docs/sample-requests/) — curl용 샘플 JSON
- [ztomcat/README.md](../ztomcat/README.md) — Tomcat WAR 배포
