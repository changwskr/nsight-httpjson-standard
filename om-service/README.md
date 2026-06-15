# om-service — Operations Management (OM) 운영관리 WAS

**운영관리(OM)** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작하며, 관리화면 백엔드 API를 제공합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **OM** |
| Gradle 모듈 | `om-service` |
| WAR | `om.war` |
| bootRun 포트 | **8096** |
| Tomcat context | `/om` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_om` |

---

## 목차

1. [업무 개요](#1-업무-개요)
2. [1차 개발 범위](#2-1차-개발-범위)
3. [실행 방법](#3-실행-방법)
4. [API·URL](#4-apiurl)
5. [디렉터리 구조](#5-디렉터리-구조)
6. [등록된 Handler](#6-등록된-handler)
7. [DB 스키마](#7-db-스키마)
8. [설정 (application.yml)](#8-설정-applicationyml)
9. [샘플 요청·demo-ui](#9-샘플-요청demo-ui)
10. [빌드·배포](#10-빌드배포)
11. [신규 거래 추가](#11-신규-거래-추가)
12. [관련 문서](#12-관련-문서)

---

## 1. 업무 개요

| 항목 | 내용 |
|------|------|
| 영문명 | Operations Management |
| 업무 그룹 | 마케팅 |
| 설명 | 운영 대시보드, 거래로그, ServiceId 카탈로그, 사용자·권한·메뉴, 감사로그 등 운영관리 기능 |
| 참고 문서 | `관리화면을 만든다면 OM.docx` (농협상호금융 NSIGHT 운영관리 화면 설계) |

공통 프레임워크 연동:

- [`common-web`](../common-web/README.md) — `POST /online`, STF/TCF/ETF
- [`common-core`](../common-core/README.md) — 표준 전문 DTO

---

## 2. 1차 개발 범위

`OM.docx` 기준 **우선 구현 화면 5개 영역** (조회 INQUIRY):

| 화면 | serviceId | transactionCode | Handler |
|------|-----------|-----------------|---------|
| 운영 대시보드 | `OM.Dashboard.inquiry` | `OM-DSH-0001` | `OmDashboardInquiryHandler` |
| 거래로그 조회 | `OM.TransactionLog.inquiry` | `OM-TXL-0001` | `OmTransactionLogInquiryHandler` |
| ServiceId/거래코드 관리 | `OM.ServiceCatalog.inquiry` | `OM-SVC-0001` | `OmServiceCatalogInquiryHandler` |
| 사용자 조회 | `OM.User.inquiry` | `OM-USR-0001` | `OmUserInquiryHandler` |
| 메뉴 조회 | `OM.Menu.inquiry` | `OM-MNU-0001` | `OmMenuInquiryHandler` |
| 권한그룹 조회 | `OM.AuthGroup.inquiry` | `OM-AUT-0001` | `OmAuthGroupInquiryHandler` |
| 감사로그 조회 | `OM.AuditLog.inquiry` | `OM-AUD-0001` | `OmAuditLogInquiryHandler` |

### 2차 개발 범위 (조회)

| 화면 | serviceId | transactionCode | Handler |
|------|-----------|-----------------|---------|
| 오류코드 / 메시지 | `OM.ErrorCode.inquiry` | `OM-ERR-0001` | `OmErrorCodeInquiryHandler` |
| 배치 / 스케줄 | `OM.Batch.inquiry` | `OM-BAT-0001` | `OmBatchInquiryHandler` |
| Health Check | `OM.HealthCheck.inquiry` | `OM-HLT-0001` | `OmHealthCheckInquiryHandler` |
| 환경설정 조회 | `OM.SystemConfig.inquiry` | `OM-CFG-0001` | `OmSystemConfigInquiryHandler` |
| 다운로드 이력 | `OM.FileDownload.inquiry` | `OM-FIL-0001` | `OmFileDownloadInquiryHandler` |

후순위(미구현): 기능권한·데이터권한 **등록/수정**, 별도 om-admin-ui Frontend 분리 등.

### 3차 개발 범위

| 화면/기능 | serviceId | processingType | Handler |
|-----------|-----------|----------------|---------|
| 공통코드 조회 | `OM.CommonCode.inquiry` | INQUIRY | `OmCommonCodeInquiryHandler` |
| 공통코드 저장 | `OM.CommonCode.save` | UPDATE | `OmCommonCodeSaveHandler` |
| 오류코드 저장 | `OM.ErrorCode.save` | UPDATE | `OmErrorCodeSaveHandler` |
| 배치 재실행 | `OM.Batch.execute` | EXECUTE | `OmBatchExecuteHandler` |
| 기능권한 조회 | `OM.FunctionAuth.inquiry` | INQUIRY | `OmFunctionAuthInquiryHandler` |
| 데이터권한 조회 | `OM.DataAuth.inquiry` | INQUIRY | `OmDataAuthInquiryHandler` |
| 권한이력 조회 | `OM.AuthHistory.inquiry` | INQUIRY | `OmAuthHistoryInquiryHandler` |
| Cache 조회 | `OM.Cache.inquiry` | INQUIRY | `OmCacheInquiryHandler` |
| Cache 삭제 | `OM.Cache.delete` | DELETE | `OmCacheDeleteHandler` |

저장·삭제·재실행 시 `changeReason` / `executeReason` / `deleteReason` 필수 (5자 이상). `OmChangeRecorder`가 `OM_AUTH_HISTORY`·`OM_AUDIT_LOG`에 기록합니다.

기존 샘플 거래 `OM.Sample.inquiry` (`OmSampleInquiryHandler`)는 템플릿 참조용으로 유지됩니다.

---

## 3. 실행 방법

### bootRun (embedded)

```bash
gradle :om-service:bootRun
# 또는
scripts/run-local-om.sh
scripts/run-local-om.bat   # Windows
```

| 항목 | URL |
|------|-----|
| Health | `http://localhost:8096/actuator/health` |
| 온라인 거래 | `POST http://localhost:8096/online` |

`OmApplication.main()`에서 `LocalBootRun.apply(8096)` — 프로필 `local`, 포트 8096 고정.

### Tomcat WAR

```bash
cd ztomcat
./deploy-wars.sh om
# context: http://localhost:8080/om/online
```

---

## 4. API·URL

모든 거래는 표준 전문 형식으로 `POST /online` (또는 Tomcat 배포 시 `/om/online`)에 요청합니다.

**요청 body 예 (운영 대시보드)**

```json
{
  "header": {
    "systemId": "NSIGHT-MP",
    "businessCode": "OM",
    "serviceId": "OM.Dashboard.inquiry",
    "transactionCode": "OM-DSH-0001",
    "processingType": "INQUIRY",
    "userId": "admin01",
    "channelId": "WEBTOP"
  },
  "body": {
    "baseDate": "2026-06-14"
  }
}
```

**응답 body 주요 필드 (대시보드)**

| 필드 | 설명 |
|------|------|
| `overallStatus` | `NORMAL` / `WARN` |
| `errorRatePct` | 당일 오류율(%) |
| `transactionSummary` | 총건수·오류건수·평균응답시간 등 |
| `apStatus` / `dbStatus` / `deployStatus` | AP·DB·배포 상태 목록 |
| `errorTop` | 당일 상위 오류 코드 |

**거래로그 조회 body 검색 조건**

`serviceId`, `resultStatus`, `errorCode`, `guid`, `traceId`, `fromDate`, `toDate`, `pageNo`, `pageSize`

---

## 5. 디렉터리 구조

```
om-service/
├── src/main/java/.../om/
│   ├── OmApplication.java
│   ├── handler/          *InquiryHandler (serviceId 라우팅)
│   ├── facade/
│   ├── service/
│   ├── rule/             OmOperationRule, OmSampleRule
│   ├── dao/              OmOperationDao, OmSampleDao
│   ├── mapper/           OmOperationMapper, OmSampleMapper
│   └── support/          OmBodySupport
└── src/main/resources/
    ├── application.yml
    ├── schema.sql        OM 운영 테이블 + SPRING_SESSION
    ├── data.sql          시드 데이터
    └── mapper/om/
        ├── OmOperationMapper.xml
        └── OmSampleMapper.xml
```

---

## 6. 등록된 Handler

Spring `@Component`로 등록되며 `TransactionHandler.getServiceId()`로 `common-web` 디스패처에 자동 매핑됩니다.

| Handler | serviceId |
|---------|-----------|
| `OmDashboardInquiryHandler` | `OM.Dashboard.inquiry` |
| `OmTransactionLogInquiryHandler` | `OM.TransactionLog.inquiry` |
| `OmServiceCatalogInquiryHandler` | `OM.ServiceCatalog.inquiry` |
| `OmUserInquiryHandler` | `OM.User.inquiry` |
| `OmMenuInquiryHandler` | `OM.Menu.inquiry` |
| `OmAuthGroupInquiryHandler` | `OM.AuthGroup.inquiry` |
| `OmAuditLogInquiryHandler` | `OM.AuditLog.inquiry` |
| `OmErrorCodeInquiryHandler` | `OM.ErrorCode.inquiry` |
| `OmBatchInquiryHandler` | `OM.Batch.inquiry` |
| `OmHealthCheckInquiryHandler` | `OM.HealthCheck.inquiry` |
| `OmSystemConfigInquiryHandler` | `OM.SystemConfig.inquiry` |
| `OmFileDownloadInquiryHandler` | `OM.FileDownload.inquiry` |
| `OmSampleInquiryHandler` | `OM.Sample.inquiry` |

---

## 7. DB 스키마

로컬 H2에 `schema.sql` / `data.sql`로 초기화됩니다.

| 테이블 | 용도 |
|--------|------|
| `OM_TX_LOG` | 거래로그 (대시보드 집계·로그 조회) |
| `OM_SERVICE_CATALOG` | ServiceId·거래코드 카탈로그 |
| `OM_USER` | 운영 사용자 |
| `OM_AUTH_GROUP` | 권한 그룹 |
| `OM_MENU` | 메뉴 트리 |
| `OM_AUDIT_LOG` | 감사로그 |
| `OM_AP_STATUS` / `OM_DB_STATUS` / `OM_DEPLOY_STATUS` | 대시보드·Health Check 인프라 상태 |
| `OM_ERROR_CODE` | 오류코드·메시지 기준 |
| `OM_BATCH_JOB` / `OM_BATCH_HISTORY` | 배치·실행 이력 |
| `OM_SYSTEM_CONFIG` | 환경설정 스냅샷 (조회 전용) |
| `OM_FILE_DOWNLOAD_LOG` | 파일 다운로드 이력 |
| `SPRING_SESSION` | JDBC 세션 (로컬 검증용) |

---

## 8. 설정 (application.yml)

| 항목 | 값 |
|------|-----|
| `server.port` | 8096 |
| `spring.datasource.url` | `jdbc:h2:mem:nsight_om;MODE=Oracle` |
| `nsight.module.business-code` | OM |
| `nsight.module.authorization-validation-enabled` | false (로컬) |

운영 환경에서는 Oracle 등 외부 DB URL·MyBatis 매퍼를 그대로 사용하고, `spring.sql.init.mode`를 `never`로 전환합니다.

---

## 9. 샘플 요청·demo-ui

### 운영관리 포털 (관리화면)

`OM.docx` 1차 범위 5개 화면이 **demo-ui**에 구현되어 있습니다.

| 화면 | URL (demo-ui 8099) |
|------|---------------------|
| 운영 대시보드 | `http://localhost:8099/om/admin/dashboard.html` |
| 거래로그 조회 | `/om/admin/transaction-log.html` |
| ServiceId 관리 | `/om/admin/service-catalog.html` |
| 사용자/권한/메뉴 | `/om/admin/user-auth.html` |
| 감사로그 조회 | `/om/admin/audit-log.html` |
| 오류코드 관리 | `/om/admin/error-code.html` |
| 배치 관리 | `/om/admin/batch.html` |
| Health Check | `/om/admin/health-check.html` |
| 환경설정 조회 | `/om/admin/system-config.html` |
| 다운로드 이력 | `/om/admin/file-download.html` |
| 공통코드 관리 | `/om/admin/common-code.html` |
| 기능권한 | `/om/admin/function-auth.html` |
| 데이터권한 | `/om/admin/data-auth.html` |
| 권한이력 | `/om/admin/auth-history.html` |
| Cache 관리 | `/om/admin/cache.html` |

```bash
gradle :om-service:bootRun    # 8096
gradle :demo-ui:bootRun      # 8099
```

포털은 `demo-ui`의 `/api/relay/OM/online`으로 `om-service` API를 호출합니다.

### API 거래 테스트

- manifest: [`demo-ui/src/main/resources/sample-requests/om-transactions.json`](../demo-ui/src/main/resources/sample-requests/om-transactions.json)
- 레거시 샘플: [`docs/sample-requests/om-sample-inquiry.json`](../docs/sample-requests/om-sample-inquiry.json)

---

## 10. 빌드·배포

```bash
gradle :om-service:compileJava
gradle :om-service:bootWar
# 산출물: om-service/build/libs/om.war
```

---

## 11. 신규 거래 추가

1. `OmOperationMapper.java` / `OmOperationMapper.xml`에 SQL 추가
2. `OmOperationDao` → `OmXxxService` → `OmXxxFacade` → `OmXxxInquiryHandler` 생성
3. `data.sql`의 `OM_SERVICE_CATALOG`에 serviceId 등록
4. `demo-ui/.../om-transactions.json`에 샘플 요청 추가

처리 흐름은 [`README-TXFLOW.md`](../README-TXFLOW.md), 레이어 패턴은 [`sv-service/README.md`](../sv-service/README.md)를 참고하세요.

---

## 12. 관련 문서

- [루트 README](../README.md)
- [README-TXFLOW.md](../README-TXFLOW.md)
- [common-web README](../common-web/README.md)
- [demo-ui README](../demo-ui/README.md)
