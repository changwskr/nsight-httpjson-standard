# ms-service — Mini Single View (MS) 업무 WAS

**Mini Single View** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **MS** |
| Gradle 모듈 | `ms-service` |
| WAR | `ms.war` |
| bootRun 포트 | **8084** |
| Tomcat context | `/ms` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_ms` |

---

## 목차

1. [업무 개요](#1-업무-개요)
2. [실행 방법](#2-실행-방법)
3. [API·URL](#3-apiurl)
4. [등록된 Handler](#4-등록된-handler)
5. [빌드·배포](#5-빌드배포)
6. [관련 문서](#6-관련-문서)

---

## 1. 업무 개요

| 항목 | 내용 |
|------|------|
| 영문명 | Mini Single View |
| 설명 | 미니 통합고객 조회 샘플 거래 제공 |
| 현재 구현 | `MS.Sample.inquiry` (`MsSampleInquiryHandler`) |

참조 템플릿: [`sv-service`](../sv-service/README.md)

---

## 2. 실행 방법

```bash
gradle :ms-service:bootRun
ms-service/scripts/run-local.bat
```

Health: `http://localhost:8084/actuator/health`  
Online: `POST http://localhost:8084/ms/online`

---

## 3. API·URL

```bash
curl -X POST http://localhost:8084/ms/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/ms-sample-inquiry.json
```

---

## 4. 등록된 Handler

| serviceId | transactionCode | Handler |
|-----------|-----------------|---------|
| `MS.Sample.inquiry` | `MS-INQ-0001` | `MsSampleInquiryHandler` |

---

## 5. 빌드·배포

`ms-service/scripts/build.bat` · `deploy.bat` · `gradle :ms-service:bootWar`

---

## 6. 관련 문서

[프로젝트 README](../README.md) · [`demo-ui`](../demo-ui/README.md) · [`ztomcat`](../ztomcat/README.md)
