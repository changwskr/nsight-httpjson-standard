# pd-service — Product (PD) 업무 WAS

**Product** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **PD** |
| Gradle 모듈 | `pd-service` |
| WAR | `pd.war` |
| bootRun 포트 | **8086** |
| Tomcat context | `/pd` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_pd` |

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
| 영문명 | Product |
| 설명 | 상품 관련 샘플 거래 제공 |
| 현재 구현 | `PD.Sample.inquiry` (`PdSampleInquiryHandler`) |

참조 템플릿: [`sv-service`](../sv-service/README.md)

---

## 2. 실행 방법

```bash
gradle :pd-service:bootRun
```

> `pd-service/scripts/`는 아직 없습니다. Gradle `bootRun` 또는 `ztomcat/deploy-wars.bat pd`를 사용하세요.

Health: `http://localhost:8086/actuator/health`  
Online: `POST http://localhost:8086/pd/online`

---

## 3. API·URL

```bash
curl -X POST http://localhost:8086/pd/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/pd-sample-inquiry.json
```

---

## 4. 등록된 Handler

| serviceId | transactionCode | Handler |
|-----------|-----------------|---------|
| `PD.Sample.inquiry` | `PD-INQ-0001` | `PdSampleInquiryHandler` |

---

## 5. 빌드·배포

`gradle :pd-service:bootWar` · `ztomcat/deploy-wars.bat pd`

---

## 6. 관련 문서

[프로젝트 README](../README.md) · [`demo-ui`](../demo-ui/README.md) · [`ztomcat`](../ztomcat/README.md)
