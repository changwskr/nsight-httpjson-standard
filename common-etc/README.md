# common-etc — ET 거래 I/O 기록 공통 WAS

업무 WAS의 ETF(종료 처리) 단계에서 **요청·응답 Header/Result를 기록·조회·삭제**하기 위한 공통 모듈입니다. `nsight.etc.record-enabled: true`인 업무 모듈과 연동됩니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **ET** |
| Gradle 모듈 | `common-etc` |
| WAR | `et.war` |
| bootRun 포트 | **8098** |
| Tomcat context | `/et` |
| Java | 21 |
| DB (local) | H2 파일 `./data/etc/tx-io-meta` |

---

## 목차

1. [모듈 역할](#1-모듈-역할)
2. [실행 방법](#2-실행-방법)
3. [API·Handler](#3-apihandler)
4. [디렉터리 구조](#4-디렉터리-구조)
5. [설정](#5-설정)
6. [demo-ui 연동](#6-demo-ui-연동)
7. [빌드·배포](#7-빌드배포)
8. [관련 문서](#8-관련-문서)

---

## 1. 모듈 역할

```text
업무 WAS (SV, CC, …)
  → ETF.postProcess()
  → POST /et/transaction-io/record   (비동기/동기 기록)
  → common-etc H2 DB

demo-ui / 운영자
  → GET Handler (목록·상세)
  → DELETE Handler (전체 삭제)
```

| 기능 | 설명 |
|------|------|
| I/O 기록 | 업무 거래의 Header·Result 스냅샷 저장 |
| 목록·상세 조회 | `TransactionIoListHandler`, `TransactionIoDetailHandler` |
| 전체 삭제 | `TransactionIoDeleteAllHandler` |

---

## 2. 실행 방법

```bash
gradle :common-etc:bootRun
common-etc/scripts/run-local.bat
```

| URL | |
|-----|---|
| Health | `http://localhost:8098/actuator/health` |
| 온라인 거래 | `POST http://localhost:8098/online` |
| REST 기록 | `POST http://localhost:8098/et/transaction-io/record` |

Tomcat: `common-etc/scripts/deploy.bat` 또는 `ztomcat/deploy-wars.bat et`

---

## 3. API·Handler

### REST Controller

| Method | Path | 설명 |
|--------|------|------|
| POST | `/et/transaction-io/record` | ETF에서 I/O 기록 수신 |
| POST | `/et/transaction-io/logs/delete` | 로그 전체 삭제 |

### Online Handler (`/online`)

| serviceId | Handler |
|-----------|---------|
| `ET.TransactionIo.list` | `TransactionIoListHandler` |
| `ET.TransactionIo.detail` | `TransactionIoDetailHandler` |
| `ET.TransactionIo.deleteAll` | `TransactionIoDeleteAllHandler` |

---

## 4. 디렉터리 구조

```text
common-etc/
├── scripts/           build · deploy · run-local
├── src/main/java/.../etc/
│   ├── handler/
│   ├── facade/
│   ├── service/
│   ├── dao/ · mapper/
│   └── controller/    TransactionIoRecordController
└── src/main/resources/
    ├── application.yml
    ├── schema.sql
    └── mapper/etc/TransactionIoMapper.xml
```

---

## 5. 설정

```yaml
nsight:
  module:
    business-code: ET
  etc:
    record-enabled: true   # 이 모듈 자체는 항상 기록 수신
```

업무 모듈에서 기록을 켜려면:

```yaml
nsight:
  etc:
    record-enabled: true
    record-url: http://localhost:8098/et/transaction-io/record
```

---

## 6. demo-ui 연동

- 화면: `http://localhost:8099/et/transaction-log.html`
- Relay API: `DELETE /api/etc/transaction-logs`

ET 서비스(8098 또는 Tomcat `/et`)가 기동되어 있어야 합니다.

---

## 7. 빌드·배포

| 명령 | 결과 |
|------|------|
| `common-etc/scripts/build.bat` | `et.war` 빌드 |
| `common-etc/scripts/deploy.bat` | Tomcat `webapps/et.war` |
| `gradle :common-etc:bootWar` | `build/libs/et.war` |

---

## 8. 관련 문서

- [프로젝트 README](../README.md#10-공통-모듈-et--ud)
- [`common-web`](../common-web/README.md)
- [`demo-ui`](../demo-ui/README.md#7-특수-화면-et--ud)
- [`ztomcat`](../ztomcat/README.md)
