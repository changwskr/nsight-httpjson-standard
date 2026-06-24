# cc-service — Common (CC) 업무 WAS

**Common** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **CC** |
| Gradle 모듈 | `cc-service` |
| WAR | `cc.war` |
| bootRun 포트 | **8080** |
| Tomcat context | `/cc` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_cc` |

> **주의:** `cc-service` bootRun(8080)과 Tomcat(8080) 포트가 겹칩니다. 동시에 사용하지 마세요.

---

## 목차

1. [업무 개요](#1-업무-개요)
2. [실행 방법](#2-실행-방법)
3. [API·URL](#3-apiurl)
4. [등록된 Handler](#4-등록된-handler)
5. [빌드·배포](#5-빌드배포)
6. [신규 거래 추가](#6-신규-거래-추가)
7. [관련 문서](#7-관련-문서)

---

## 1. 업무 개요

| 항목 | 내용 |
|------|------|
| 영문명 | Common |
| 업무코드 | CC |
| 설명 | 공통 업무 기준 샘플 거래 제공 |
| 현재 구현 | **샘플 조회** 1건 (`CC.Sample.inquiry`) |

공통 프레임워크 연동:

- [`common-web`](../common-web/README.md) — `POST /online`, STF/TCF/ETF
- [`common-core`](../common-core/README.md) — 표준 전문 DTO
- ETF → [`common-etc`](../common-etc/README.md) I/O 기록 (옵션)

상세 구현 참고: [`sv-service`](../sv-service/README.md)

---

## 2. 실행 방법

### bootRun (embedded)

```bash
gradle :cc-service:bootRun
```

```bat
cc-service\scripts\run-local.bat
```

| 항목 | URL |
|------|-----|
| Health | `http://localhost:8080/actuator/health` |
| 온라인 거래 | `POST http://localhost:8080/online` 또는 `/cc/online` |

### Tomcat WAR

```bat
cc-service\scripts\deploy.bat
cd ztomcat && deploy-wars.bat cc
```

---

## 3. API·URL

| Method | Path | 설명 |
|--------|------|------|
| POST | `/online`, `/cc/online` | 표준 JSON 온라인 거래 |
| GET | `/actuator/health` | 헬스 체크 |

```bash
curl -X POST http://localhost:8080/cc/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/cc-sample-inquiry.json
```

---

## 4. 등록된 Handler

| serviceId | transactionCode | Handler |
|-----------|-----------------|---------|
| `CC.Sample.inquiry` | `CC-INQ-0001` | `CcSampleInquiryHandler` |

---

## 5. 빌드·배포

| 명령 | 결과 |
|------|------|
| `cc-service/scripts/build.bat` | `cc.war` 빌드 |
| `cc-service/scripts/deploy.bat` | Tomcat `webapps/cc.war` |
| `gradle :cc-service:bootWar` | `cc-service/build/libs/cc.war` |
| `bin/build-wars.sh cc` | `bin/cc.war` |

---

## 6. 신규 거래 추가

1. `handler/` — `TransactionHandler` 구현
2. `facade/` → `service/` → `rule/` → `dao/` / Mapper
3. `docs/sample-requests/` — 샘플 JSON 추가

Handler 상세: [`common-web/README.md`](../common-web/README.md#6-handler-디스패치)

---

## 7. 관련 문서

- [프로젝트 README](../README.md)
- [`sv-service`](../sv-service/README.md)
- [`demo-ui`](../demo-ui/README.md)
- [`ztomcat`](../ztomcat/README.md)
