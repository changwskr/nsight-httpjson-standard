# ct-service — Contents (CT) 업무 WAS

**Contents** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **CT** |
| Gradle 모듈 | `ct-service` |
| WAR | `ct.war` |
| bootRun 포트 | **8094** |
| Tomcat context | `/ct` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_ct` |

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
| 영문명 | Contents |
| 설명 | 콘텐츠 샘플 거래 제공 |
| 현재 구현 | `CT.Sample.inquiry` (`CtSampleInquiryHandler`) |

참조 템플릿: [`sv-service`](../sv-service/README.md)

---

## 2. 실행 방법

```bash
gradle :ct-service:bootRun
```

> `ct-service/scripts/`는 아직 없습니다. Gradle `bootRun` 또는 `ztomcat/deploy-wars.bat ct`를 사용하세요.

Health: `http://localhost:8094/actuator/health`  
Online: `POST http://localhost:8094/ct/online`

---

## 3. API·URL

```bash
curl -X POST http://localhost:8094/ct/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/ct-sample-inquiry.json
```

---

## 4. 등록된 Handler

| serviceId | transactionCode | Handler |
|-----------|-----------------|---------|
| `CT.Sample.inquiry` | `CT-INQ-0001` | `CtSampleInquiryHandler` |

---

## 5. 빌드·배포

`gradle :ct-service:bootWar` · `ztomcat/deploy-wars.bat ct`

---

## 6. 관련 문서

[프로젝트 README](../README.md) · [`demo-ui`](../demo-ui/README.md) · [`ztomcat`](../ztomcat/README.md)
