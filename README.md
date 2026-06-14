# NSIGHT HTTP/JSON 표준 전문 기반 온라인 거래 처리 Spring Boot 프로젝트

본 프로젝트는 NSIGHT 마케팅플랫폼의 **비REST HTTP/JSON 표준 전문 처리방식**을 구현하기 위한 Spring Boot 멀티모듈 샘플입니다.

## 1. 핵심 구조

```text
Client/WebTopSuite
  -> HTTP POST + JSON Body
  -> Apache / L4 / GSLB
  -> Spring Boot OnlineTransactionController
  -> STF(Start Transaction Filter) / PreProcessor
  -> Dispatcher(serviceId 기준)
  -> Business Handler
  -> Facade -> Service -> Rule -> DAO/Mapper
  -> ETF(End Transaction Filter) / PostProcessor
  -> Standard JSON Response
```

## 2. 모듈 구성

| No | 구분 | 업무코드 | 영문명 | Context | Gradle Module | WAR |
|---:|---|---|---|---|---|---|
| 1 | 공통 | CC | Common | `/cc` | `cc-service` | `cc.war` |
| 2 | 고객 | IC | Integration Customer | `/ic` | `ic-service` | `ic.war` |
| 3 | 고객 | PC | Private Customer | `/pc` | `pc-service` | `pc.war` |
| 4 | 고객 | BC | Business Customer | `/bc` | `bc-service` | `bc.war` |
| 5 | 고객 | MS | Mini Single View | `/ms` | `ms-service` | `ms.war` |
| 6 | 마케팅 | SV | Single View | `/sv` | `sv-service` | `sv.war` |
| 7 | 마케팅 | PD | Product | `/pd` | `pd-service` | `pd.war` |
| 8 | 마케팅 | CM | Campaign | `/cm` | `cm-service` | `cm.war` |
| 9 | 마케팅 | EB | EBM | `/eb` | `eb-service` | `eb.war` |
| 10 | 실시간 | EP | Event Processing | `/ep` | `ep-service` | `ep.war` |
| 11 | 실시간 | BP | Behavior Processing | `/bp` | `bp-service` | `bp.war` |
| 12 | 데이터 | BD | Behavior Data | `/bd` | `bd-service` | `bd.war` |
| 13 | 지원 | SS | Sales Support | `/ss` | `ss-service` | `ss.war` |
| 14 | 지원 | CS | Common Service | `/cs` | `cs-service` | `cs.war` |
| 15 | 지원 | CT | Contents | `/ct` | `ct-service` | `ct.war` |
| 16 | 지원 | MG | Message | `/mg` | `mg-service` | `mg.war` |
| 17 | 운영 | OM | Operation Management | `/om` | `om-service` | `om.war` |

## 3. 빌드

```bash
./gradlew clean build
./gradlew bootWar
```

Gradle Wrapper가 없는 환경이면 로컬 Gradle 8.x와 JDK 21로 다음처럼 실행합니다.

```bash
gradle clean build
gradle bootWar
```

## 4. 로컬 실행 예시

```bash
./gradlew :sv-service:bootRun
```

요청 예시:

```bash
curl -X POST http://localhost:8080/sv/online \
  -H 'Content-Type: application/json' \
  -d @docs/sample-requests/sv-sample-inquiry.json
```

Tomcat WAR 배포 시에는 `sv.war`를 배포하면 Context가 `/sv`가 되므로, 내부 Spring mapping은 `/online`만 사용해도 됩니다.

## 5. 표준 전문 구조

요청은 `header + body`, 응답은 `header + result + body` 구조입니다. 업무 식별은 URL Resource가 아니라 `serviceId`, `transactionCode`, `processingType`으로 수행합니다.

## 6. 생성 범위

- 표준 전문 DTO
- StandardHeader / StandardRequest / StandardResponse / Result
- STF Filter
- ETF Filter
- PreProcessor / PostProcessor
- TransactionDispatcher / TransactionHandler
- Header Validation
- IdempotencyChecker Stub
- Session/Auth/Authorization Validator Stub
- TransactionLog / Audit / Metrics Stub
- 17개 업무 모듈
- 각 업무별 Sample Handler / Facade / Service / Rule / DAO / Mapper
- application.yml
- Logback 설정
- Apache 라우팅 예시
- GitLab CI 예시
