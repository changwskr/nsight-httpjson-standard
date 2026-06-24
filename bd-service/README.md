# bd-service — Behavior Data (BD) 업무 WAS

**Behavior Data** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **BD** |
| Gradle 모듈 | `bd-service` |
| WAR | `bd.war` |
| bootRun 포트 | **8091** |
| Tomcat context | `/bd` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_bd` |

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
| 영문명 | Behavior Data |
| 설명 | 행동 데이터 샘플 거래 제공 |
| 현재 구현 | `BD.Sample.inquiry` (`BdSampleInquiryHandler`) |

참조 템플릿: [`sv-service`](../sv-service/README.md)

---

## 2. 실행 방법

```bash
gradle :bd-service:bootRun
bd-service/scripts/run-local.bat
```

Health: `http://localhost:8091/actuator/health`  
Online: `POST http://localhost:8091/bd/online`

---

## 3. API·URL

```bash
curl -X POST http://localhost:8091/bd/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/bd-sample-inquiry.json
```

---

## 4. 등록된 Handler

| serviceId | transactionCode | Handler |
|-----------|-----------------|---------|
| `BD.Sample.inquiry` | `BD-INQ-0001` | `BdSampleInquiryHandler` |

---

## 5. 빌드·배포

`bd-service/scripts/build.bat` · `deploy.bat` · `gradle :bd-service:bootWar`

---

## 6. 관련 문서

[프로젝트 README](../README.md) · [`demo-ui`](../demo-ui/README.md) · [`ztomcat`](../ztomcat/README.md)
