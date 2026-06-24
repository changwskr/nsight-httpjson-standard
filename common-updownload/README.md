# common-updownload — UD 파일 업·다운로드 공통 WAS

파일 **업로드·목록·상세·수정·삭제·다운로드**를 제공하는 공통 모듈입니다. 표준 `/online` Handler와 REST `/ud/files/*` API를 함께 제공합니다.

| 항목 | 값 |
|------|-----|
| 업무코드 | **UD** |
| Gradle 모듈 | `common-updownload` |
| WAR | `ud.war` |
| bootRun 포트 | **8097** |
| Tomcat context | `/ud` |
| Java | 21 |
| DB (local) | H2 파일 `./data/updownload/ud-meta` |
| 파일 저장 | `./data/updownload/` |

---

## 목차

1. [모듈 역할](#1-모듈-역할)
2. [실행 방법](#2-실행-방법)
3. [REST API](#3-rest-api)
4. [Online Handler](#4-online-handler)
5. [디렉터리 구조](#5-디렉터리-구조)
6. [설정](#6-설정)
7. [demo-ui 연동](#7-demo-ui-연동)
8. [빌드·배포](#8-빌드배포)
9. [관련 문서](#9-관련-문서)

---

## 1. 모듈 역할

| 기능 | 설명 |
|------|------|
| 파일 업로드 | multipart 업로드, 메타 DB 저장 |
| 목록·검색 | 페이징 목록, 조건 검색 |
| 상세·수정·삭제 | 파일 메타 관리 |
| 다운로드 | `fileId` 기준 스트리밍 다운로드 |

---

## 2. 실행 방법

```bash
gradle :common-updownload:bootRun
common-updownload/scripts/run-local.bat
```

| URL | |
|-----|---|
| Health | `http://localhost:8097/actuator/health` |
| REST | `http://localhost:8097/ud/files` |
| Online | `POST http://localhost:8097/online` |

Tomcat: `common-updownload/scripts/deploy.bat` 또는 `ztomcat/deploy-wars.bat ud`

---

## 3. REST API

| Method | Path | 설명 |
|--------|------|------|
| POST | `/ud/files/upload` | multipart 파일 업로드 |
| GET | `/ud/files` | 목록·검색 (query params) |
| GET | `/ud/files/{fileId}` | 상세 |
| PUT | `/ud/files/{fileId}` | 메타 수정 |
| DELETE | `/ud/files/{fileId}` | 삭제 |
| GET | `/ud/files/{fileId}/download` | 다운로드 |

---

## 4. Online Handler

| serviceId | Handler |
|-----------|---------|
| `UD.File.upload` | `UpdownloadUploadHandler` |
| `UD.File.list` | `UpdownloadListHandler` |
| `UD.File.detail` | `UpdownloadDetailHandler` |
| `UD.File.update` | `UpdownloadUpdateHandler` |
| `UD.File.delete` | `UpdownloadDeleteHandler` |
| `UD.File.download` | `UpdownloadDownloadHandler` |

샘플 JSON: `docs/sample-requests/ud-sample-inquiry.json`

---

## 5. 디렉터리 구조

```text
common-updownload/
├── scripts/
├── src/main/java/.../updownload/
│   ├── handler/
│   ├── facade/ · service/ · rule/ · dao/
│   └── controller/    UpdownloadFileController
└── src/main/resources/
    ├── application.yml
    ├── schema.sql
    └── mapper/ud/UpdownloadMapper.xml
```

---

## 6. 설정

```yaml
nsight:
  module:
    business-code: UD
  updownload:
    storage-path: ./data/updownload
    max-file-size-mb: 50
```

---

## 7. demo-ui 연동

- 화면: `http://localhost:8099/ud/updownload.html`
- Relay: `/api/updownload/*` → UD 서비스

---

## 8. 빌드·배포

| 명령 | 결과 |
|------|------|
| `common-updownload/scripts/build.bat` | `ud.war` 빌드 |
| `common-updownload/scripts/deploy.bat` | Tomcat 배포 |
| `gradle :common-updownload:bootWar` | `build/libs/ud.war` |

---

## 9. 관련 문서

- [프로젝트 README](../README.md#10-공통-모듈-et--ud)
- [`demo-ui`](../demo-ui/README.md#7-특수-화면-et--ud)
- [`om-service`](../om-service/README.md) — 파일 다운로드 이력(OM)
- [`ztomcat`](../ztomcat/README.md)
