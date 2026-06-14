# bin — 업무 WAR 빌드·수집

17개 **업무 WAR**를 Gradle로 빌드한 뒤 **`bin/{code}.war`** 한곳에 모아 두는 스크립트와 산출물 디렉터리입니다. Tomcat 배포는 [`ztomcat/`](../ztomcat/README.md), 전체 모듈(ET·UD 포함) 빌드는 [`scripts/build-all.sh`](../scripts/build-all.sh)를 사용합니다.

| 항목 | 값 |
|------|-----|
| 위치 | `bin/` |
| 대상 | 업무 17개 (`cc` … `om`) — **ET·UD 미포함** |
| 산출물 | `bin/{code}.war` (예: `bin/sv.war`) |
| 사전 요구 | **JDK 21**, **Gradle 8.x** |

> `bin/*.war`는 `.gitignore` 대상입니다. 스크립트(`build-wars.*`)와 이 README만 Git에 포함됩니다.

---

## 목차

1. [역할](#1-역할)
2. [bin vs scripts vs ztomcat](#2-bin-vs-scripts-vs-ztomcat)
3. [스크립트 목록](#3-스크립트-목록)
4. [build-wars 사용법](#4-build-wars-사용법)
5. [지원 업무 코드](#5-지원-업무-코드)
6. [산출물 경로](#6-산출물-경로)
7. [트러블슈팅](#7-트러블슈팅)
8. [관련 문서](#8-관련-문서)

---

## 1. 역할

```text
Gradle :{code}-service:bootWar
  → {code}-service/build/libs/{code}.war
  → copy
  → bin/{code}.war
```

| 작업 | bin | scripts | ztomcat |
|------|:---:|:-------:|:-------:|
| 업무 WAR 17개 빌드 | ✓ | ✓ (bootWar 일부) | ✓ |
| ET·UD WAR 빌드 | | ✓ | ✓ |
| `bin/`에 WAR 수집 | ✓ | | |
| Tomcat `webapps/` 배포 | | | ✓ |
| `clean build` + 테스트 | | ✓ | |

**용도 예:** CI artifact 업로드, 수동 WAR 전달, Tomcat 없이 빌드 결과만 확인.

---

## 2. bin vs scripts vs ztomcat

| 도구 | 명령 예 | 결과 |
|------|---------|------|
| **`bin/build-wars`** | `bin/build-wars.sh sv` | `bin/sv.war` |
| [`scripts/build-all.sh`](../scripts/build-all.sh) | `./scripts/build-all.sh` | 각 모듈 `build/libs/*.war` (19개 + test) |
| [`ztomcat/deploy-wars.sh`](../ztomcat/deploy-wars.sh) | `./deploy-wars.sh sv` | `ztomcat/.../webapps/sv.war` |

Tomcat에 올리려면:

```bash
# 방법 1: ztomcat (빌드 + 배포 일괄)
cd ztomcat && ./deploy-wars.sh sv

# 방법 2: bin 빌드 후 수동 복사
bin/build-wars.sh sv
cp bin/sv.war ztomcat/apache-tomcat-10.1.34/webapps/
```

---

## 3. 스크립트 목록

| 파일 | OS | 설명 |
|------|-----|------|
| [`build-wars.sh`](build-wars.sh) | Linux, macOS, Git Bash | 전체 또는 **복수** 코드 빌드·복사 |
| [`build-wars.bat`](build-wars.bat) | Windows CMD | 전체 또는 **단일** 코드 빌드·복사 |

공통:

- Gradle `:{code}-service:bootWar` 실행
- `{code}-service/build/libs/{code}.war` → `bin/{code}.war` 복사
- `./gradlew` 있으면 Wrapper 우선, 없으면 `gradle`

---

## 4. build-wars 사용법

### Linux / macOS / Git Bash

```bash
# 프로젝트 루트에서
chmod +x bin/build-wars.sh          # 최초 1회

bin/build-wars.sh                   # 업무 17개 전체
bin/build-wars.sh sv                # SV만
bin/build-wars.sh sv bc om          # 복수 선택
bin/build-wars.sh --help
```

### Windows (CMD)

```bat
bin\build-wars.bat                  rem 17개 전체
bin\build-wars.bat sv               rem SV만 (단일 코드만 지원)
bin\build-wars.bat help
```

> Windows `.bat`은 **한 번에 코드 1개**만 지정할 수 있습니다. 복수 빌드는 Git Bash에서 `build-wars.sh`를 사용하세요.

### 출력 예

```text
==> Gradle bootWar: sv bc
  copied -> bin/sv.war
  copied -> bin/bc.war

==> Done. WAR files in bin/
bin/bc.war
bin/sv.war
```

---

## 5. 지원 업무 코드

17개 업무 모듈만 대상입니다 (**`ud`, `et` 없음**).

```text
cc ic pc bc ms sv pd cm eb ep bp bd ss cs ct mg om
```

| 코드 | Gradle 모듈 | 산출 WAR |
|------|-------------|----------|
| cc | `cc-service` | `bin/cc.war` |
| ic | `ic-service` | `bin/ic.war` |
| pc | `pc-service` | `bin/pc.war` |
| bc | `bc-service` | `bin/bc.war` |
| ms | `ms-service` | `bin/ms.war` |
| sv | `sv-service` | `bin/sv.war` |
| pd | `pd-service` | `bin/pd.war` |
| cm | `cm-service` | `bin/cm.war` |
| eb | `eb-service` | `bin/eb.war` |
| ep | `ep-service` | `bin/ep.war` |
| bp | `bp-service` | `bin/bp.war` |
| bd | `bd-service` | `bin/bd.war` |
| ss | `ss-service` | `bin/ss.war` |
| cs | `cs-service` | `bin/cs.war` |
| ct | `ct-service` | `bin/ct.war` |
| mg | `mg-service` | `bin/mg.war` |
| om | `om-service` | `bin/om.war` |

ET·UD WAR:

```bash
gradle :common-etc:bootWar :common-updownload:bootWar
# common-etc/build/libs/et.war
# common-updownload/build/libs/ud.war
```

---

## 6. 산출물 경로

| 경로 | 설명 |
|------|------|
| `bin/{code}.war` | 스크립트가 복사한 **수집본** (배포·전달용) |
| `{code}-service/build/libs/{code}.war` | Gradle **원본** 산출물 |

원본과 수집본은 동일 빌드에서 복사되므로 내용은 같습니다. `bin/`은 편의용 staging 디렉터리입니다.

---

## 7. 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| `gradle not found` | Gradle 미설치 | Gradle 8.x PATH 설정 |
| Java 버전 오류 | JDK 21 아님 | `JAVA_HOME` JDK 21 |
| `ERROR: WAR not found` | bootWar 실패 | Gradle 오류 로그 확인 |
| `unknown business code: ud` | ET·UD 미지원 | `gradle :common-etc:bootWar` 또는 `ztomcat/deploy-wars.sh ud` |
| `bin/`이 Git에 안 보임 | WAR만 ignore | `bin/*.war` ignore — 스크립트·README는 추적 가능 |

---

## 8. 관련 문서

- [프로젝트 README](../README.md)
- [scripts/README.md](../scripts/README.md) — `clean build bootWar`, SV bootRun
- [ztomcat/README.md](../ztomcat/README.md) — Tomcat 배포·검증
