# common-core — NSIGHT 공통 코어 라이브러리

Spring Web에 **의존하지 않는** 공통 모듈입니다. 표준 전문 DTO, 거래 컨텍스트, 예외, 유틸리티를 제공하며, 모든 업무 WAS와 `common-web`의 기반이 됩니다.

| 항목 | 값 |
|------|-----|
| Gradle 모듈 | `common-core` |
| 플러그인 | `java-library` |
| Java | 21 |
| 의존성 | `jakarta.validation-api` (api) |
| Spring Boot BOM | 3.3.5 (dependency-management only) |

> **WAR 배포용** `NsightBootApplication`은 `common-web`에 있습니다. `common-core`는 순수 Java 라이브러리입니다.

---

## 목차

1. [모듈 역할](#1-모듈-역할)
2. [패키지 구조](#2-패키지-구조)
3. [표준 전문 (message)](#3-표준-전문-message)
4. [거래 컨텍스트 (context)](#4-거래-컨텍스트-context)
5. [예외 (exception)](#5-예외-exception)
6. [유틸리티 (util)](#6-유틸리티-util)
7. [로컬 실행 (boot)](#7-로컬-실행-boot)
8. [의존 관계](#8-의존-관계)
9. [사용 예시](#9-사용-예시)

---

## 1. 모듈 역할

```text
Client JSON
  → StandardRequest / StandardResponse  (common-core)
  → TransactionContext                    (common-core)
  → STF / TCF / ETF                       (common-web)
  → TransactionHandler                    (업무 모듈)
```

| 책임 | common-core | common-web |
|------|:-----------:|:----------:|
| 표준 Header/Body DTO | ✓ | |
| Bean Validation 어노테이션 | ✓ | |
| Servlet / Spring MVC | | ✓ |
| Filter, Controller, Dispatcher | | ✓ |

업무 모듈은 보통 `common-web`만 의존하며, `common-web`이 `common-core`를 **api**로 re-export합니다.

---

## 2. 패키지 구조

```text
com.nh.nsight.marketing.common
├── message/          StandardHeader, StandardRequest/Response, Result
├── context/          TransactionContext
├── exception/        BusinessException, SystemException
├── util/             GuidUtil, DateTimeUtil
└── boot/             LocalBootRun
```

---

## 3. 표준 전문 (message)

### 전문 구조

**요청**

```json
{
  "header": { ... StandardHeader ... },
  "body":   { ... 업무별 필드 ... }
}
```

**응답**

```json
{
  "header": { ... StandardHeader (응답용) ... },
  "result": { ... Result ... },
  "body":   { ... 업무별 필드 ... }
}
```

업무 식별은 URL이 아니라 Header의 **`serviceId`**, **`transactionCode`**, **`processingType`** 으로 수행합니다.

---

### `StandardHeader`

공통 헤더. Jakarta Bean Validation `@NotBlank`가 붙은 필드는 `@Valid` 역직렬화 시 검증됩니다.

| 필드 | 필수 | 설명 |
|------|:----:|------|
| `systemId` | ✓ | 시스템 ID (예: `NSIGHT-MP`) |
| `businessCode` | ✓ | 업무코드 (예: `SV`, `CC`) |
| `serviceId` | ✓ | 서비스 ID — **Handler 라우팅 키** (예: `SV.Sample.inquiry`) |
| `transactionCode` | ✓ | 거래코드 (예: `SV-INQ-0001`) |
| `processingType` | ✓ | 처리 유형 (예: `INQUIRY`, `CREATE`) |
| `guid` | | 글로벌 요청 ID (STF에서 생성·보강) |
| `traceId` | | 추적 ID |
| `channelId` | | 채널 (예: `WEBTOP`) |
| `userId` | | 사용자 ID |
| `branchId` | | 점번호 |
| `centerId` | | 센터 ID |
| `clientIp` | | 클라이언트 IP |
| `requestTime` | | 요청 시각 |
| `responseTime` | | 응답 시각 |
| `transactionIntime` | | 거래 시작 시각 |
| `transactionOuttime` | | 거래 종료 시각 |
| `systemDate` | | 시스템 일자 (`yyyyMMdd`) |
| `bizDate` | | 영업 일자 |
| `apId` | | AP 서버 ID |

`copy()` — 응답 Header 생성 시 요청 Header 복제용.

---

### `StandardRequest<T>`

| 필드 | 검증 | 설명 |
|------|------|------|
| `header` | `@NotNull`, `@Valid` | 표준 헤더 |
| `body` | | 업무별 페이로드 (제네릭) |

Controller에서는 보통 `StandardRequest<Map<String, Object>>`로 수신합니다.

---

### `StandardResponse<T>`

| 필드 | 설명 |
|------|------|
| `header` | 응답 헤더 (요청 Header 기반 + 시각 보강) |
| `result` | 처리 결과 |
| `body` | 업무 응답 데이터 |

팩토리: `StandardResponse.of(header, result, body)`

---

### `Result` / `ResultStatus`

| `ResultStatus` | 의미 |
|----------------|------|
| `SUCCESS` | 정상 |
| `FAIL` | 업무 실패 (BusinessException 등) |
| `ERROR` | 시스템 오류 |
| `PROCESSING` | 처리 중 (예약) |
| `UNKNOWN` | 미정 |

**팩토리 메서드**

| 메서드 | status | resultCode |
|--------|--------|------------|
| `Result.success(elapsedMs)` | SUCCESS | `S0000` |
| `Result.fail(code, msg, elapsedMs)` | FAIL | `E0001` |
| `Result.error(code, msg, elapsedMs)` | ERROR | `E0001` |

공통 필드: `messageCode`, `message`, `errorCode`, `errorMessage`, `elapsedTimeMs`

---

### `ProcessingType` (enum)

`INQUIRY`, `CREATE`, `UPDATE`, `DELETE`, `EXECUTE`, `DOWNLOAD`, `UPLOAD`, `APPROVAL`, `CANCEL`

Header의 `processingType` 문자열과 대응합니다 (JSON 역직렬화 시 문자열 사용).

---

## 4. 거래 컨텍스트 (context)

### `TransactionContext`

한 번의 온라인 거래 처리 동안 STF → Handler → ETF에 전달되는 **불변 스냅샷**입니다.

| 필드 | 설명 |
|------|------|
| `header` | STF가 보강한 **응답용** Header (guid, traceId, 시각 등 반영) |
| `requestHeader` | 클라이언트가 보낸 **원본** Header (ET I/O 기록용) |
| `startTime` | 거래 시작 `Instant` |
| `pathBusinessCode` | URL path의 `{businessCode}` (선택) |

**생성자**

```java
new TransactionContext(header, startTime);
new TransactionContext(header, requestHeader, startTime);  // I/O 기록용 원본 보존
```

**편의 getter:** `getGuid()`, `getTraceId()`, `getBusinessCode()`, `getServiceId()`, `getTransactionCode()`

`common-web`의 `STF`가 생성하고, `TCF`/`ETF`/`TransactionIoRecordPublisher`가 사용합니다.

---

## 5. 예외 (exception)

| 클래스 | 용도 | 처리 |
|--------|------|------|
| `BusinessException` | 업무 규칙 위반 | `TCF` → `Result.fail` / HTTP 200 + FAIL |
| `SystemException` | 시스템·예기치 않은 오류 | `TCF` → `Result.error` |

공통: `errorCode` 필드 보유.

```java
throw new BusinessException("SV-001", "고객 정보를 찾을 수 없습니다.");
throw new SystemException("SYS-001", "DB 연결 실패", cause);
```

---

## 6. 유틸리티 (util)

### `GuidUtil`

| 메서드 | 반환 예 |
|--------|---------|
| `newGuid()` | `550e8400-e29b-41d4-a716-446655440000` |
| `newTraceId()` | `trc-550e8400-e29b-41d4-a716-446655440000` |

`STFFilter` / `STF`에서 Header·MDC·응답 헤더(`X-GUID`, `X-Trace-Id`)에 사용합니다.

### `DateTimeUtil`

| 메서드 | 반환 예 |
|--------|---------|
| `nowKst()` | `2026-06-14T10:30:00+09:00` (ISO-8601, KST) |
| `todayKst()` | `20260614` |

`STF`에서 Header 시각·일자 필드 보강에 사용합니다.

---

## 7. 로컬 실행 (boot)

### `LocalBootRun`

IDE 또는 `bootRun`에서 **멀티모듈 classpath 충돌**을 줄이기 위한 JVM 프로퍼티 설정 유틸입니다.

```java
// 업무 서비스 (SvApplication 등)
LocalBootRun.apply(8085);

// common-etc, common-updownload
LocalBootRun.apply(8098, "ET", "nsight-etc-service");
```

설정 내용:

| System Property | 값 |
|-----------------|-----|
| `server.port` | 인자로 전달한 포트 |
| `spring.profiles.active` | `local` |
| `nsight.module.business-code` | (선택) 업무코드 |
| `spring.application.name` | (선택) 애플리케이션명 |

각 `*Application.main()` **맨 앞**에서 호출합니다.

---

## 8. 의존 관계

```text
common-core          (java-library, Spring-free)
    ↑ api
common-web           (Spring Web, STF/ETF/TCF)
    ↑ implementation
*-service            (업무 WAR)
common-etc
common-updownload
```

- `demo-ui`는 `common-core`를 **직접 의존하지 않음**
- 테스트·신규 라이브러리 모듈에서 표준 DTO만 필요하면 `common-core`만 의존 가능

---

## 9. 사용 예시

### 샘플 요청 JSON

[`docs/sample-requests/sv-sample-inquiry.json`](../docs/sample-requests/sv-sample-inquiry.json)

```json
{
  "header": {
    "systemId": "NSIGHT-MP",
    "businessCode": "SV",
    "serviceId": "SV.Sample.inquiry",
    "transactionCode": "SV-INQ-0001",
    "processingType": "INQUIRY",
    "userId": "U123456",
    "branchId": "001234"
  },
  "body": {
    "sampleKey": "SV-SAMPLE",
    "baseDate": "20260614"
  }
}
```

### Handler에서 body 사용

```java
@Override
public Object doHandle(TransactionContext context, Map<String, Object> body) {
    String serviceId = context.getServiceId();
    // Facade/Service 호출
    return facade.inquiry(context, body);
}
```

### 성공 응답 조립 (ETF 내부 패턴)

```java
StandardResponse.of(
    responseHeader,
    Result.success(elapsedMs),
    businessBody
);
```

---

## 관련 문서

- [프로젝트 README](../README.md)
- [common-web/README.md](../common-web/README.md) — STF/ETF/TCF, Controller, Handler 디스패치
- [docs/architecture.md](../docs/architecture.md) — 처리 흐름 상세
