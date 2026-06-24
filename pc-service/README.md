# pc-service — Private Customer (PC) 업무 WAS

**Private Customer** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **PC** |
| Gradle 모듈 | `pc-service` |
| WAR | `pc.war` |
| bootRun 포트 | **8082** |
| Tomcat context | `/pc` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_pc` |

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
| 영문명 | Private Customer |
| 설명 | 개인고객 관련 샘플 거래 제공 |
| 현재 구현 | `PC.Sample.inquiry` (`PcSampleInquiryHandler`) |

참조 템플릿: [`sv-service`](../sv-service/README.md)

---

## 2. 실행 방법

```bash
gradle :pc-service:bootRun
pc-service/scripts/run-local.bat
```

| URL | |
|-----|---|
| Health | `http://localhost:8082/actuator/health` |
| Online | `POST http://localhost:8082/pc/online` |

---

## 3. API·URL

```bash
curl -X POST http://localhost:8082/pc/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/pc-sample-inquiry.json
```

---

## 4. 등록된 Handler

| serviceId | transactionCode | Handler |
|-----------|-----------------|---------|
| `PC.Sample.inquiry` | `PC-INQ-0001` | `PcSampleInquiryHandler` |

---

## 5. 빌드·배포

`pc-service/scripts/build.bat` · `deploy.bat` · `gradle :pc-service:bootWar` · `bin/build-wars.sh pc`

---

## 6. 관련 문서

[프로젝트 README](../README.md) · [`demo-ui`](../demo-ui/README.md) · [`ztomcat`](../ztomcat/README.md)
