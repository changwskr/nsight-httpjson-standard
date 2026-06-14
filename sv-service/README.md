# sv-service — Single View (SV) 업무 WAS

**Single View(통합고객 조회)** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작하며, 다른 16개 업무 WAR의 **참조 템플릿**으로 사용할 수 있습니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **SV** |
| Gradle 모듈 | `sv-service` |
| WAR | `sv.war` |
| bootRun 포트 | **8085** |
| Tomcat context | `/sv` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_sv` |

---

## 목차

1. [업무 개요](#1-업무-개요)
2. [실행 방법](#2-실행-방법)
3. [API·URL](#3-apiurl)
4. [디렉터리 구조](#4-디렉터리-구조)
5. [처리 흐름 (샘플 거래)](#5-처리-흐름-샘플-거래)
6. [등록된 Handler](#6-등록된-handler)
7. [레이어별 클래스](#7-레이어별-클래스)
8. [설정 (application.yml)](#8-설정-applicationyml)
9. [샘플 요청·응답](#9-샘플-요청응답)
10. [빌드·배포](#10-빌드배포)
11. [신규 거래 추가](#11-신규-거래-추가)
12. [트러블슈팅](#12-트러블슈팅)
13. [관련 문서](#13-관련-문서)

---

## 1. 업무 개요

| 항목 | 내용 |
|------|------|
| 영문명 | Single View |
| 업무 그룹 | 마케팅 |
| 설명 | 통합고객 기준의 고객 단위 조회 제공 |
| 현재 구현 | **샘플 조회** 1건 (`SV.Sample.inquiry`) |

공통 프레임워크 연동:

- [`common-web`](../common-web/README.md) — `POST /online`, STF/TCF/ETF
- [`common-core`](../common-core/README.md) — 표준 전문 DTO
- ETF → [`common-etc`](../README.md#10-공통-모듈-et--ud) I/O 기록 (옵션)

---

## 2. 실행 방법

### bootRun (embedded)

```bash
gradle :sv-service:bootRun
# 또는
scripts/run-local-sv.sh
```

| 항목 | URL |
|------|-----|
| Health | `http://localhost:8085/actuator/health` |
| 온라인 거래 | `POST http://localhost:8085/online` 또는 `/sv/online` |

`SvApplication.main()`에서 `LocalBootRun.apply(8085)` — 프로필 `local`, 포트 8085 고정.

### ET I/O 기록 연동 (선택)

`nsight.etc.record-enabled: true`이면 **common-etc**가 필요합니다.

```bash
gradle :common-etc:bootRun    # :8098
```

### Tomcat WAR

```bash
cd ztomcat
deploy-wars.bat sv    # Linux: ./deploy-wars.sh sv
start.bat             # Linux: ./start.sh
```

```bash
curl http://localhost:8080/sv/actuator/health
```

상세: [`ztomcat/README.md`](../ztomcat/README.md)

### demo-ui

```bash
gradle :demo-ui:bootRun
```

`http://localhost:8099/sv/index.html` — [`demo-ui/README.md`](../demo-ui/README.md)

---

## 3. API·URL

| Method | bootRun | Tomcat |
|--------|---------|--------|
| POST | `http://localhost:8085/online` | `http://localhost:8080/sv/online` |
| POST | `http://localhost:8085/sv/online` | `http://localhost:8080/sv/SV/online` |
| GET | `/actuator/health` | `/sv/actuator/health` |

- **Content-Type:** `application/json`
- **Body:** `StandardRequest` (header + body)
- GET `/online`은 지원하지 않습니다.

---

## 4. 디렉터리 구조

```text
sv-service/
├── build.gradle
├── README.md
└── src/main/
    ├── java/com/nh/nsight/marketing/sv/
    │   ├── SvApplication.java
    │   ├── handler/          TransactionHandler
    │   ├── facade/           @Transactional 경계
    │   ├── service/          업무 로직
    │   ├── rule/             BusinessException 검증
    │   ├── dao/              Mapper 래핑
    │   └── mapper/           MyBatis @Mapper
    └── resources/
        ├── application.yml
        ├── logback-spring.xml
        └── mapper/sv/
            └── SvSampleMapper.xml
```

---

## 5. 처리 흐름 (샘플 거래)

```text
POST /sv/online  (serviceId: SV.Sample.inquiry)
  → STFFilter / OnlineTransactionController
  → TCF
       → STF (Header 검증, businessCode=SV)
       → TransactionDispatcher
            → SvSampleInquiryHandler
                 → SvSampleFacade.inquiry (@Transactional readOnly)
                      → SvSampleService.inquiry
                           → SvSampleRule.validateInquiry
                           → SvSampleDao.selectSample
                                → SvSampleMapper → SvSampleMapper.xml
       → ETF (StandardResponse + ET I/O 기록)
```

---

## 6. 등록된 Handler

| serviceId | Handler | processingType | transactionCode (샘플) |
|-----------|---------|----------------|------------------------|
| `SV.Sample.inquiry` | `SvSampleInquiryHandler` | `INQUIRY` | `SV-INQ-0001` |

라우팅: Header **`serviceId`** = `TransactionHandler.getServiceId()` ([`common-web`](../common-web/README.md#6-handler-디스패치))

---

## 7. 레이어별 클래스

### `SvSampleInquiryHandler`

```java
@Component
public class SvSampleInquiryHandler implements TransactionHandler {
    @Override
    public String getServiceId() { return "SV.Sample.inquiry"; }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
```

### `SvSampleFacade`

- `@Transactional(readOnly = true, timeout = 5)` — DB 조회 트랜잭션 경계

### `SvSampleService`

- Rule 검증 → DAO 조회 → 응답 body Map 조립
- 응답 필드: `businessCode`, `businessName`, `businessGroup`, `description`, `serviceId`, `transactionCode`, `data`

### `SvSampleRule`

- `businessCode`가 `SV`가 아니면 `BusinessException("E-SV-BIZ-0001", ...)`

### `SvSampleDao` / `SvSampleMapper`

- MyBatis `selectSample` — 현재 H2 더미 SELECT (`BUSINESS_CODE`, `BUSINESS_NAME`)
- DAO에서 `sampleId`, `sampleName`, `input`, `note` 추가

---

## 8. 설정 (application.yml)

### 핵심 설정

```yaml
server:
  port: 8085

spring:
  application:
    name: nsight-sv-service
  profiles:
    active: local
  datasource:
    url: jdbc:h2:mem:nsight_sv;MODE=Oracle;DB_CLOSE_DELAY=-1

nsight:
  module:
    business-code: SV          # Header businessCode와 일치 필수
    ap-id: local-sv-ap01
  etc:
    record-enabled: true
    record-url: http://localhost:8098/et/transaction-io/record   # bootRun
    # record-url: http://localhost:8080/et/transaction-io/record  # Tomcat
```

| 설정 | 설명 |
|------|------|
| `nsight.module.business-code` | 모듈 고정 업무코드 — Header와 불일치 시 STF 오류 |
| `nsight.etc.record-enabled` | ETF → ET I/O 기록 REST 호출 |
| `spring.session.store-type: jdbc` | Spring Session (stub) |
| `mybatis.mapper-locations` | `classpath:/mapper/**/*.xml` |

Tomcat 배포 시 `record-url`을 `http://localhost:8080/et/...`로 변경하세요.

전체 Properties: [`common-web/README.md`](../common-web/README.md#8-설정-properties)

---

## 9. 샘플 요청·응답

### 요청 JSON

[`docs/sample-requests/sv-sample-inquiry.json`](../docs/sample-requests/sv-sample-inquiry.json)

```bash
curl -X POST http://localhost:8085/sv/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/sv-sample-inquiry.json
```

### 응답 예 (성공)

```json
{
  "header": { "...": "..." },
  "result": {
    "status": "SUCCESS",
    "resultCode": "S0000",
    "message": "정상 처리되었습니다.",
    "elapsedTimeMs": 42
  },
  "body": {
    "businessCode": "SV",
    "businessName": "Single View",
    "businessGroup": "마케팅",
    "description": "통합고객 기준의 고객 단위 조회 제공 업무",
    "serviceId": "SV.Sample.inquiry",
    "transactionCode": "SV-INQ-0001",
    "data": {
      "BUSINESS_CODE": "SV",
      "BUSINESS_NAME": "Single View",
      "sampleId": "SV-SAMPLE-0001",
      "sampleName": "Single View sample transaction",
      "input": { "sampleKey": "SV-SAMPLE", "baseDate": "20260614" },
      "note": "실제 업무에서는 이 위치에서 MyBatis Mapper를 호출한다."
    }
  }
}
```

---

## 10. 빌드·배포

| 명령 | 결과 |
|------|------|
| `gradle :sv-service:bootWar` | `sv-service/build/libs/sv.war` |
| `bin/build-wars.sh sv` | `bin/sv.war` |
| `ztomcat/deploy-wars.sh sv` | Tomcat `webapps/sv.war` |

Gradle 상세: [`docs/gradle/README.md`](../docs/gradle/README.md)

---

## 11. 신규 거래 추가

1. **Handler** — `TransactionHandler` 구현, 고유 `serviceId` (예: `SV.Customer.inquiry`)
2. **Facade / Service / Rule / DAO** — 레이어 추가
3. **Mapper XML** — `src/main/resources/mapper/sv/`
4. **샘플 JSON** — `docs/sample-requests/sv-*.json`
5. **demo-ui** — `BusinessTransactionCatalog`에 거래 등록 (선택)

다른 업무 모듈 추가 시 `sv-service`를 복사·개명하는 패턴을 권장합니다.  
프로젝트 공통 가이드: [루트 README §11](../README.md#11-업무-handler-추가-방법)

---

## 12. 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| `E-COM-HDR-0004` | Header `businessCode` ≠ `SV` | Header 또는 `application.yml` 확인 |
| `E-COM-HDL-0001` | `serviceId` 미등록 | Handler `getServiceId()`와 Header 일치 확인 |
| `E-SV-BIZ-0001` | Rule — businessCode 불일치 | Header `businessCode: SV` |
| ET 기록 warn | ET 미기동 | `common-etc` bootRun(:8098) 또는 Tomcat `/et` |
| Tomcat 404 | JDK 21 아님 / 배포 중 | [`ztomcat/README.md`](../ztomcat/README.md) |
| 8085 포트 충돌 | 다른 프로세스 | `netstat` 확인 후 종료 |

---

## 13. 관련 문서

- [프로젝트 README](../README.md)
- [common-web/README.md](../common-web/README.md)
- [common-core/README.md](../common-core/README.md)
- [demo-ui/README.md](../demo-ui/README.md)
- [ztomcat/README.md](../ztomcat/README.md)
- [scripts/run-local-sv.sh](../scripts/README.md#4-run-local-svsh)
