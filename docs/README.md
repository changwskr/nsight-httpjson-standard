# docs — 프로젝트 문서

NSIGHT HTTP/JSON 표준 전문 플랫폼의 **아키텍처·Gradle·샘플 전문** 문서 모음입니다.

---

## 목차

| 문서 | 설명 |
|------|------|
| [`architecture.md`](architecture.md) | URL 원칙, STF/TCF/ETF 처리 흐름, 레거시 다이어그램 |
| [`gradle/README.md`](gradle/README.md) | Gradle 멀티모듈 구조, 태스크, 빌드 산출물 |
| [`sample-requests/`](sample-requests/) | 업무별 curl/demo-ui용 샘플 JSON |

---

## sample-requests

업무별 단일 조회 샘플:

```text
cc-sample-inquiry.json … om-sample-inquiry.json
sv-sample-inquiry.json
ud-sample-inquiry.json
```

`demo-ui`는 `src/main/resources/sample-requests/`에 동일·확장 샘플을 복사해 사용합니다.  
다중 거래 화면용 manifest: `{code}-transactions.json` (예: `sv-transactions.json`)

---

## 관련 문서

- [프로젝트 README](../README.md)
- [README-TXFLOW.md](../README-TXFLOW.md) — 클래스·함수 단위 거래 흐름
- [모듈별 README](../README.md#2-프로젝트-구조)
