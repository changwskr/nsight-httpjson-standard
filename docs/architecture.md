# NSIGHT HTTP/JSON 온라인 거래 처리 아키텍처

## 처리 흐름

```text
[Client/WebTopSuite]
      |
      | HTTP POST + JSON Body
      v
[Apache]
      |
      v
[Spring Boot Controller]
      |
      v
[STF Filter]
 - GUID/TraceId 생성
 - MDC 설정
 - 요청 시작시간 기록
      |
      v
[PreProcessor]
 - 표준 전문 검증
 - Header 검증
 - 세션/인증/권한 검증
 - 중복요청 확인
 - 거래 시작 로그
      |
      v
[Dispatcher]
 - serviceId 기준 Handler 선택
      |
      v
[Business Handler]
      |
      v
[Facade -> Service -> Rule -> DAO/Mapper]
      |
      v
[PostProcessor]
 - 표준 응답 조립
 - 결과/오류코드 매핑
 - 마스킹
 - 거래 종료 로그
      |
      v
[ETF Filter]
 - 응답 완료 로그
 - MDC Clear
```

## URL 원칙

REST Resource 방식이 아니라 업무 Context + 온라인 진입점 방식입니다.

```text
POST /cc/online
POST /sv/online
POST /cm/online
POST /mg/online
POST /om/online
```

실제 업무는 JSON Header의 `serviceId`, `transactionCode`, `processingType`으로 구분합니다.
