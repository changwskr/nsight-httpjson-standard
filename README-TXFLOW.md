# NSIGHT 온라인 거래 흐름 — 클래스·함수 단위

`POST /online` (또는 `POST /{businessCode}/online`) 한 건이 **HTTP Filter → Controller → Processor(STF/TCF/ETF) → Handler → 업무 레이어 → 응답**까지 지나는 호출 순서를 **클래스.메서드** 기준으로 정리한 문서입니다.

**예시 거래:** `sv-service`, `serviceId = SV.Sample.inquiry`  
샘플 JSON: [`docs/sample-requests/sv-sample-inquiry.json`](docs/sample-requests/sv-sample-inquiry.json)

| 관련 문서 |
|-----------|
| [`common-web/README.md`](common-web/README.md) — processor 상세 |
| [`common-core/README.md`](common-core/README.md) — DTO·Context |
| [`sv-service/README.md`](sv-service/README.md) — SV 업무 레이어 |

---

## 목차

1. [전체 호출 순서 (성공)](#1-전체-호출-순서-성공)
2. [레이어별 상세](#2-레이어별-상세)
3. [예외 분기 흐름](#3-예외-분기-흐름)
4. [Bean Validation (TCF 진입 전)](#4-bean-validation-tcf-진입-전)
5. [ET I/O 기록 (비동기 후처리)](#5-et-io-기록-비동기-후처리)
6. [호출 순서표 (번호)](#6-호출-순서표-번호)
7. [Filter vs Processor 이름 구분](#7-filter-vs-processor-이름-구분)

---

## 1. 전체 호출 순서 (성공)

```text
[Client] POST /sv/online + StandardRequest JSON
    │
    ▼ ── HTTP Servlet Filter ─────────────────────────────────────
 1  STFFilter.doFilterInternal()
       ├─ headerOrNew("X-GUID")     → GuidUtil.newGuid() (없을 때)
       ├─ headerOrNew("X-Trace-Id") → GuidUtil.newTraceId()
       ├─ request.setAttribute(REQUEST_*)
       ├─ MDC.put("guid", "traceId")
       └─ filterChain.doFilter()
    │
    ▼ ── Spring MVC ─────────────────────────────────────────────
 2  (DispatcherServlet → JSON 역직렬화 → @Valid 검증)
 3  OnlineTransactionController.processWithBusinessCode(businessCode, request)
       └─ TCF.process(businessCode, request)
    │
    ▼ ── Processor: TCF ─────────────────────────────────────────
 4  STF.preProcess(pathBusinessCode, request)
       ├─ StandardHeaderValidator.validateAndNormalize()
       ├─ STF.applyStartHeader()
       ├─ new TransactionContext(...)
       ├─ SessionValidator.validate()
       ├─ AuthorizationValidator.validate()
       ├─ IdempotencyChecker.check()
       └─ TransactionLogService.start()
 5  TransactionDispatcher.dispatch(context, body)
       └─ TransactionHandler.handle() → doHandle()
            └─ (업무) SvSampleInquiryHandler.doHandle()
                 └─ SvSampleFacade.inquiry()
                      └─ SvSampleService.inquiry()
                           ├─ SvSampleRule.validateInquiry()
                           └─ SvSampleDao.selectSample()
                                └─ SvSampleMapper.selectSampleWithLogging()
                                     └─ SvSampleMapper.selectSample() [MyBatis XML]
 6  ETF.success(context, body)
       └─ ETF.build(context, result, body)
            ├─ ETF.applyEndHeader()
            ├─ TransactionLogService.end()
            ├─ TransactionIoRecordPublisher.publish()  [옵션]
            ├─ AuditLogService.auditIfRequired()
            ├─ MetricsPublisher.publish()
            ├─ MDC.clear()
            └─ StandardResponse.of()
 7  ResponseEntity.ok(response)   → HTTP 200 + JSON
    │
    ▼ ── HTTP Servlet Filter ─────────────────────────────────────
 8  ETFFilter.doFilterInternal() finally
       └─ TRANSACTION_LOG "ETF_END" (uri, status, elapsedMs)
    │
    ▼
[Client] StandardResponse JSON
```

---

## 2. 레이어별 상세

### 2.1 HTTP Filter (`common-web` … `filter`)

#### `STFFilter.doFilterInternal(request, response, filterChain)`

| 순서 | 호출 | 설명 |
|------|------|------|
| 1 | `System.currentTimeMillis()` | HTTP 요청 시작 시각 |
| 2 | `headerOrNew(request, "X-GUID", GuidUtil.newGuid())` | HTTP 헤더 GUID |
| 3 | `headerOrNew(request, "X-Trace-Id", GuidUtil.newTraceId())` | HTTP TraceId |
| 4 | `request.setAttribute(REQUEST_START_TIME\|GUID\|TRACE_ID)` | ETFFilter용 |
| 5 | `MDC.put("guid", "traceId")` | SLF4J MDC (HTTP 레벨) |
| 6 | `response.setHeader("X-GUID", "X-Trace-Id")` | 응답 echo |
| 7 | `filterChain.doFilter()` | Controller chain 진입 |
| finally | `MDC.remove("guid", "traceId")` | Processor ETF와 이중 정리 |

#### `STFFilter.headerOrNew(request, name, generated)` (private)

HTTP 헤더 값이 blank이면 `generated` 반환.

> **참고:** HTTP Filter GUID와 JSON Header `guid`는 **별도**. Header `guid` blank 시 `StandardHeaderValidator`가 다시 생성합니다.

#### `ETFFilter.doFilterInternal(request, response, filterChain)`

| 순서 | 호출 | 설명 |
|------|------|------|
| try | `filterChain.doFilter()` | 전체 MVC·Processor 실행 |
| finally | `request.getAttribute(STFFilter.REQUEST_START_TIME)` | HTTP 경과 ms |
| finally | `log.info("ETF_END guid=... uri=... status=... elapsedMs=...")` | `TRANSACTION_LOG` |
| finally | `MDC.clear()` | 최종 MDC 정리 |

---

### 2.2 Controller (`common-web` … `controller`)

#### `OnlineTransactionController.process(request)`

| 항목 | 값 |
|------|-----|
| Mapping | `POST /online` |
| `pathBusinessCode` | `null` → TCF에 전달 |
| 다음 | `TCF.process(null, request)` |

#### `OnlineTransactionController.processWithBusinessCode(businessCode, request)`

| 항목 | 값 |
|------|-----|
| Mapping | `POST /{businessCode}/online` |
| `pathBusinessCode` | path 변수 (예: `"SV"`) |
| 다음 | `TCF.process(businessCode, request)` |

공통: `ResponseEntity.ok(response)` — **항상 HTTP 200** (업무 실패 포함).

---

### 2.3 Processor — STF (`common-web` … `processor` … `STF`)

#### `STF.preProcess(pathBusinessCode, request)` → `TransactionContext`

| # | 클래스.메서드 | 설명 |
|---|---------------|------|
| 1 | `StandardHeaderValidator.validateAndNormalize(header, pathBusinessCode)` | Header 검증·기본값 보강 ([§2.4](#24-standardheadervalidator)) |
| 2 | `STF.applyStartHeader(header)` (private) | 시각·일자 보강 ([§2.3.1](#231-stfapplystartheader-private)) |
| 3 | `new TransactionContext(header, header.copy(), Instant.now())` | Context 생성 |
| 4 | `context.setPathBusinessCode(pathBusinessCode)` | URL path 저장 |
| 5 | `MDC.put(guid, traceId, userId, branchId, serviceId)` | 거래 MDC |
| 6 | `SessionValidator.validate(context)` | 세션 ([§2.5](#25-보안세션멱등)) |
| 7 | `AuthorizationValidator.validate(context)` | 권한 (stub) |
| 8 | `IdempotencyChecker.check(context)` | 멱등 (stub) |
| 9 | `TransactionLogService.start(context)` | TX_START 로그 |
| return | `TransactionContext` | TCF·Dispatcher·Handler에 전달 |

#### 2.3.1 `STF.applyStartHeader(header)` (private)

| Header 필드 | 처리 |
|-------------|------|
| `businessCode` | `toUpperCase()` |
| `transactionIntime` | blank → `DateTimeUtil.nowKst()` |
| `requestTime` | blank → `transactionIntime` |
| `systemDate` | blank → `DateTimeUtil.todayKst()` |
| `bizDate` | blank → `systemDate` |

---

### 2.4 StandardHeaderValidator

#### `StandardHeaderValidator.validateAndNormalize(header, pathBusinessCode)`

| # | 검증·보강 | 실패 시 |
|---|-----------|---------|
| 1 | `header != null` | `BusinessException E-COM-HDR-0001` |
| 2 | `systemId` blank → `properties.systemId` | |
| 3 | `businessCode` blank → path 또는 `properties.businessCode` | |
| 4 | `businessCode` 필수 | `E-COM-HDR-0002` |
| 5 | path vs header businessCode 일치 | `E-COM-HDR-0003` |
| 6 | module vs header businessCode 일치 | `E-COM-HDR-0004` |
| 7 | `required(serviceId)` | `E-COM-HDR-0005` |
| 8 | `required(transactionCode)` | `E-COM-HDR-0006` |
| 9 | `required(processingType)` | `E-COM-HDR-0007` |
| 10 | `ProcessingType.valueOf(processingType)` | `E-COM-HDR-0008` |
| 11 | `guid` blank → `GuidUtil.newGuid()` | |
| 12 | `traceId` blank → `GuidUtil.newTraceId()` | |
| 13 | `apId` blank → `properties.apId` | |

`required()` → `BusinessException(code, name + "는 필수입니다.")`

---

### 2.5 보안·세션·멱등

#### `SessionValidator.validate(context)`

- `sessionValidationEnabled == false` → skip
- enabled && `userId` blank → `BusinessException E-COM-AUTH-0001`

#### `AuthorizationValidator.validate(context)`

- `authorizationValidationEnabled == false` → skip
- enabled → TODO (현재 통과)

#### `IdempotencyChecker.check(context)`

- `idempotencyEnabled == false` → skip
- enabled && 변경성 `ProcessingType` → TODO (현재 로그만)

---

### 2.6 Processor — TCF (`TCF`)

#### `TCF.process(pathBusinessCode, request)` → `StandardResponse`

```text
context = STF.preProcess(...)
try {
    body = TransactionDispatcher.dispatch(context, request.getBody())
    return ETF.success(context, body)
} catch (BusinessException e) {
    return ETF.businessFail(context, e)
} catch (Exception e) {
    return ETF.systemError(context, e)
}
```

---

### 2.7 Dispatcher · Handler (`common-web` … `dispatch`)

#### `TransactionDispatcher.<init>(handlers)` (기동 시 1회)

- 모든 `TransactionHandler` Bean → `handlerMap.put(handler.getServiceId(), handler)`

#### `TransactionDispatcher.dispatch(context, body)` → `Object`

| # | 처리 |
|---|------|
| 1 | `handler = handlerMap.get(context.getServiceId())` |
| 2 | handler == null → `BusinessException E-COM-HDL-0001` |
| 3 | `handler.handle(context, body)` |

#### `TransactionHandler.handle(context, body)` (default)

| # | 처리 |
|---|------|
| 1 | 로그 |
| 2 | `doHandle(context, body)` — **업무 구현** |
| 3 | return result |

---

### 2.8 업무 레이어 예시 (`sv-service`)

`serviceId = SV.Sample.inquiry` 일 때:

| # | 클래스.메서드 | 패키지 |
|---|---------------|--------|
| 1 | `SvSampleInquiryHandler.getServiceId()` | `…sv.handler` |
| 2 | `SvSampleInquiryHandler.doHandle(context, body)` | |
| 3 | `SvSampleFacade.inquiry(context, body)` | `…sv.facade` — `@Transactional(readOnly=true)` |
| 4 | `SvSampleService.inquiry(context, body)` | `…sv.service` |
| 5 | `SvSampleRule.validateInquiry(context, body)` | `…sv.rule` — `BusinessException E-SV-BIZ-0001` |
| 6 | `SvSampleDao.selectSample(context, body)` | `…sv.dao` |
| 7 | `SvSampleMapper.selectSampleWithLogging(parameter)` | `…sv.mapper` (default) |
| 8 | `SvSampleMapper.selectSample(parameter)` | MyBatis → `SvSampleMapper.xml` |

Handler 반환 `Map` → TCF → `ETF.success(context, body)`.

---

### 2.9 Processor — ETF (`ETF`)

#### `ETF.success(context, body)`

1. `Result.success(ETF.elapsed(context))` — `S0000`, status `SUCCESS`
2. `ETF.build(context, result, body)`

#### `ETF.businessFail(context, BusinessException e)`

1. `Result.fail(e.getErrorCode(), e.getMessage(), elapsed)` — status `FAIL`
2. `ETF.build(context, result, null)`

#### `ETF.systemError(context, Exception e)`

1. `Result.error("E-COM-SYS-9999", "시스템 처리 중 오류가 발생했습니다.", elapsed)` — status `ERROR`
2. `ETF.build(context, result, null)`

#### `ETF.build(context, result, body)` (private) — 공통 종료

| # | 클래스.메서드 | 설명 |
|---|---------------|------|
| 1 | `ETF.applyEndHeader(context.getHeader())` | outtime, responseTime (KST) |
| 2 | `responseHeader = context.getHeader().copy()` | 응답 Header 스냅샷 |
| 3 | `TransactionLogService.end(context, result)` | TX_END |
| 4 | `TransactionIoRecordPublisher.publish(context, responseHeader, result)` | ET REST ([§5](#5-et-io-기록-비동기-후처리)) |
| 5 | `AuditLogService.auditIfRequired(context)` | Customer/download 감사 |
| 6 | `MetricsPublisher.publish(context, result)` | debug 메트릭 |
| 7 | `MDC.clear()` | Processor MDC 정리 |
| 8 | `StandardResponse.of(responseHeader, result, body)` | [`common-core`](common-core/README.md) |

#### `ETF.elapsed(context)` (private)

`Duration.between(context.getStartTime(), Instant.now()).toMillis()`

#### `ETF.applyEndHeader(header)` (private)

| 필드 | 처리 |
|------|------|
| `transactionOuttime`, `responseTime` | `DateTimeUtil.nowKst()` |
| `systemDate`, `bizDate` | blank 시 KST today |

---

### 2.10 로그·감사·메트릭

#### `TransactionLogService.start(context)`

- Logger `TRANSACTION_LOG` → `TX_START guid=... serviceId=...`

#### `TransactionLogService.end(context, result)`

- Logger `TRANSACTION_LOG` → `TX_END ... status=... elapsedMs=...`

#### `AuditLogService.auditIfRequired(context)`

- `serviceId`에 `Customer` 또는 `download` 포함 시 `AUDIT_LOG`

#### `MetricsPublisher.publish(context, result)`

- debug: `METRIC serviceId=... status=... elapsedMs=...`

---

## 3. 예외 분기 흐름

### 3.1 BusinessException (업무·검증 실패)

발생 가능 지점 → 모두 `TCF.process` catch → `ETF.businessFail`:

```text
StandardHeaderValidator.validateAndNormalize()     E-COM-HDR-xxxx
SessionValidator.validate()                        E-COM-AUTH-0001
TransactionDispatcher.dispatch()                   E-COM-HDL-0001
SvSampleRule.validateInquiry()                       E-SV-BIZ-0001
(기타 Handler / Service / Rule)
        │
        ▼
TCF.process() catch (BusinessException)
        │
        ▼
ETF.businessFail(context, e)
        └─ ETF.build() → StandardResponse (body=null, status=FAIL)
        │
        ▼
OnlineTransactionController → HTTP 200
```

**Handler·DB는 실행되지 않을 수 있음** (STF 단계에서 throw 시 Dispatcher 미호출).

### 3.2 Exception (시스템 오류)

```text
(Handler / Service / DAO / RuntimeException 등)
        │
        ▼
TCF.process() catch (Exception)
        │
        ▼
ETF.systemError(context, e)
        └─ Result.error("E-COM-SYS-9999", ...)
        └─ ETF.build() → StandardResponse (body=null, status=ERROR)
```

### 3.3 Bean Validation 실패 (TCF 미진입)

```text
@RequestBody @Valid StandardRequest
        │
        ▼ (header @NotNull, @Valid 실패 등)
Spring MethodArgumentNotValidException
        │
        ▼
HTTP 400 Bad Request  (표준 StandardResponse 아님)
```

---

## 4. Bean Validation (TCF 진입 전)

Spring MVC가 JSON → `StandardRequest` 역직렬화 후 `@Valid` 실행:

| 클래스 | 어노테이션 |
|--------|-----------|
| `StandardRequest.header` | `@NotNull`, `@Valid` |
| `StandardHeader.systemId` 등 | `@NotBlank` ([`common-core`](common-core/README.md)) |

통과 후에만 `OnlineTransactionController` → `TCF.process` 호출.

---

## 5. ET I/O 기록 (비동기 후처리)

`nsight.etc.record-enabled: true` 일 때 `ETF.build` →:

#### `TransactionIoRecordPublisher.publish(context, responseHeader, result)`

| # | 처리 |
|---|------|
| 1 | `inputHeader = context.getRequestHeader()` (STF 시점 copy) |
| 2 | payload = `{ inputHeader, outputHeader, result }` |
| 3 | `RestClient.post().uri(recordUrl).body(payload)` |
| 4 | 실패 시 warn 로그 — **본 거래 응답에는 영향 없음** |

#### ET WAS (`common-etc`) 수신

| # | 클래스.메서드 |
|---|---------------|
| 1 | `TransactionIoRecordController.record(request)` |
| 2 | `TransactionIoService.record(inputHeader, outputHeader, result)` |
| 3 | H2 파일 DB 저장 |

URL 예 (bootRun): `POST http://localhost:8098/et/transaction-io/record`  
Tomcat: `POST http://localhost:8080/et/transaction-io/record`

---

## 6. 호출 순서표 (번호)

성공 경로 **번호 순** (Filter 포함):

| No | 클래스 | 메서드 | 비고 |
|----|--------|--------|------|
| 1 | `STFFilter` | `doFilterInternal` | HTTP GUID/TraceId |
| 2 | `STFFilter` | `headerOrNew` | private ×2 |
| 3 | `GuidUtil` | `newGuid` / `newTraceId` | HTTP 헤더용 |
| 4 | — | Spring `@Valid` + JSON bind | 실패 시 400 |
| 5 | `OnlineTransactionController` | `process` / `processWithBusinessCode` | |
| 6 | `TCF` | `process` | 진입점 |
| 7 | `STF` | `preProcess` | |
| 8 | `StandardHeaderValidator` | `validateAndNormalize` | |
| 9 | `GuidUtil` | `newGuid` / `newTraceId` | Header blank 시 |
| 10 | `STF` | `applyStartHeader` | private |
| 11 | `DateTimeUtil` | `nowKst` / `todayKst` | |
| 12 | `TransactionContext` | constructor | common-core |
| 13 | `SessionValidator` | `validate` | |
| 14 | `AuthorizationValidator` | `validate` | |
| 15 | `IdempotencyChecker` | `check` | |
| 16 | `TransactionLogService` | `start` | TX_START |
| 17 | `TransactionDispatcher` | `dispatch` | |
| 18 | `TransactionHandler` | `handle` → `doHandle` | |
| 19 | `SvSampleInquiryHandler` | `doHandle` | 업무 예 |
| 20 | `SvSampleFacade` | `inquiry` | |
| 21 | `SvSampleService` | `inquiry` | |
| 22 | `SvSampleRule` | `validateInquiry` | |
| 23 | `SvSampleDao` | `selectSample` | |
| 24 | `SvSampleMapper` | `selectSampleWithLogging` → `selectSample` | MyBatis |
| 25 | `ETF` | `success` | |
| 26 | `Result` | `success` | common-core |
| 27 | `ETF` | `build` | private |
| 28 | `ETF` | `applyEndHeader` | private |
| 29 | `TransactionLogService` | `end` | TX_END |
| 30 | `TransactionIoRecordPublisher` | `publish` | 옵션 |
| 31 | `AuditLogService` | `auditIfRequired` | |
| 32 | `MetricsPublisher` | `publish` | |
| 33 | `StandardResponse` | `of` | common-core |
| 34 | `OnlineTransactionController` | `ResponseEntity.ok` | HTTP 200 |
| 35 | `ETFFilter` | `doFilterInternal` finally | ETF_END |

---

## 7. Filter vs Processor 이름 구분

| 이름 | 패키지 | 클래스 | 단위 |
|------|--------|--------|------|
| STF (Filter) | `web.filter` | `STFFilter` | **HTTP 요청** |
| STF (Processor) | `web.processor` | `STF` | **표준 전문 거래** |
| ETF (Filter) | `web.filter` | `ETFFilter` | **HTTP 응답** |
| ETF (Processor) | `web.processor` | `ETF` | **표준 전문 거래** |
| TCF | `web.processor` | `TCF` | Processor만 존재 |

---

## 관련 문서

- [README.md](README.md) — 프로젝트 개요
- [common-web/README.md](common-web/README.md#5-stf--tcf--etf-processor-패키지) — processor 상세
- [docs/architecture.md](docs/architecture.md) — 아키텍처 다이어그램
- [sv-service/README.md](sv-service/README.md) — SV 샘플 업무
