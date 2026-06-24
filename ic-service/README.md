# ic-service — Integration Customer (IC) 업무 WAS

**Integration Customer** 업무 모듈입니다. NSIGHT 표준 HTTP/JSON 온라인 거래 파이프라인(`common-web`) 위에서 Handler → Facade → Service → Rule → DAO/Mapper 레이어로 동작합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **IC** |
| Gradle 모듈 | `ic-service` |
| WAR | `ic.war` |
| bootRun 포트 | **8081** |
| Tomcat context | `/ic` |
| Java | 21 |
| DB (local) | H2 인메모리 `nsight_ic` |

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
| 영문명 | Integration Customer |
| 업무코드 | IC |
| 설명 | 통합고객 관련 샘플 거래 제공 |
| 현재 구현 | **샘플 조회** 1건 (`IC.Sample.inquiry`) |

공통 프레임워크: [`common-web`](../common-web/README.md), [`common-core`](../common-core/README.md)  
참조 템플릿: [`sv-service`](../sv-service/README.md)

---

## 2. 실행 방법

```bash
gradle :ic-service:bootRun
ic-service/scripts/run-local.bat   # Windows
```

| 항목 | URL |
|------|-----|
| Health | `http://localhost:8081/actuator/health` |
| 온라인 거래 | `POST http://localhost:8081/ic/online` |

Tomcat: `ic-service/scripts/deploy.bat` 또는 `ztomcat/deploy-wars.bat ic`

---

## 3. API·URL

```bash
curl -X POST http://localhost:8081/ic/online \
  -H "Content-Type: application/json" \
  -d @docs/sample-requests/ic-sample-inquiry.json
```

---

## 4. 등록된 Handler

| serviceId | transactionCode | Handler |
|-----------|-----------------|---------|
| `IC.Sample.inquiry` | `IC-INQ-0001` | `IcSampleInquiryHandler` |

---

## 5. 빌드·배포

| 명령 | 결과 |
|------|------|
| `ic-service/scripts/build.bat` | `ic.war` 빌드 |
| `ic-service/scripts/deploy.bat` | Tomcat 배포 |
| `gradle :ic-service:bootWar` | `build/libs/ic.war` |

---

## 6. 신규 거래 추가

Handler → Facade → Service → Rule → DAO 패턴으로 확장. [`common-web`](../common-web/README.md#6-handler-디스패치) 참고.

---

## 7. 관련 문서

- [프로젝트 README](../README.md) · [`sv-service`](../sv-service/README.md) · [`demo-ui`](../demo-ui/README.md) · [`ztomcat`](../ztomcat/README.md)
