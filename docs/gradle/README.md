# Gradle 빌드 환경 가이드

NSIGHT HTTP/JSON 표준 프로젝트의 **Gradle 멀티모듈** 빌드 설정·명령·모듈 구조를 정리한 문서입니다.

| 항목 | 값 |
|------|-----|
| Gradle | **8.x** 권장 (로컬 `gradle` 명령) |
| Gradle Wrapper | **미포함** (`gradlew` 없음) |
| Java Toolchain | **21** (모든 서브프로젝트) |
| Spring Boot | **3.3.5** |
| Group / Version | `com.nh.nsight.marketing` / `0.1.0-SNAPSHOT` |
| 저장소 | Maven Central only |

> Tomcat 배포·스크립트 단축: [`bin/`](../bin/README.md), [`scripts/`](../scripts/README.md), [`ztomcat/`](../ztomcat/README.md)

---

## 목차

1. [사전 준비](#1-사전-준비)
2. [프로젝트 Gradle 구조](#2-프로젝트-gradle-구조)
3. [모듈 유형·의존 관계](#3-모듈-유형의존-관계)
4. [주요 Gradle 태스크](#4-주요-gradle-태스크)
5. [WAR·JAR 산출물](#5-warjar-산출물)
6. [gradle.properties](#6-gradleproperties)
7. [로컬 개발 명령](#7-로컬-개발-명령)
8. [IDE 연동](#8-ide-연동)
9. [GitLab CI](#9-gitlab-ci)
10. [신규 모듈 추가](#10-신규-모듈-추가)
11. [트러블슈팅](#11-트러블슈팅)
12. [관련 문서](#12-관련-문서)

---

## 1. 사전 준비

### JDK 21

모든 모듈이 **Java Toolchain 21**을 사용합니다.

```bash
java -version    # openjdk 21.x
echo $JAVA_HOME  # JDK 21 경로
```

Windows 예: `%USERPROFILE%\.jdks\temurin-21.0.4`

Gradle Toolchain은 `JAVA_HOME` 외에도 자동 JDK 탐색을 시도합니다. Toolchain JDK를 명시하려면:

```properties
# gradle.properties (로컬, git 제외 가능)
org.gradle.java.installations.auto-download=true
```

### Gradle 설치

```bash
gradle -version   # Gradle 8.x
```

| OS | 설치 |
|----|------|
| Windows | [Gradle 공식](https://gradle.org/install/) 또는 SDKMAN/Chocolatey |
| Linux/macOS | `sdk install gradle 8.10.1` 등 |

> **Wrapper 없음:** 이 저장소 루트에 `gradlew` / `gradlew.bat`이 **없습니다**. 로컬·스크립트는 시스템 `gradle`을 사용합니다.

### Gradle Wrapper 추가 (선택)

팀·CI 재현성을 위해 Wrapper 생성을 권장합니다.

```bash
# 프로젝트 루트에서 (Gradle 설치 후 1회)
gradle wrapper --gradle-version 8.10.1
```

생성 후 `./gradlew` / `gradlew.bat` 사용. GitLab CI(`.gitlab-ci.yml`)는 `./gradlew`를 가정합니다.

---

## 2. 프로젝트 Gradle 구조

```text
nsight-httpjson-standard/
├── settings.gradle          # 22개 모듈 include
├── build.gradle             # 루트: Boot 플러그인 버전, 공통 group/version
├── gradle.properties        # JVM·병렬·캐시
├── common-core/build.gradle
├── common-web/build.gradle
├── *-service/build.gradle   # 17개 업무 WAR
├── common-etc/build.gradle
├── common-updownload/build.gradle
└── demo-ui/build.gradle
```

### settings.gradle

- `dependencyResolutionManagement`: **FAIL_ON_PROJECT_REPOS** — 서브프로젝트 개별 repository 금지
- `mavenCentral()`만 사용
- `rootProject.name = 'nsight-httpjson-standard'`

### build.gradle (루트)

```gradle
plugins {
    id 'org.springframework.boot' version '3.3.5' apply false
    id 'io.spring.dependency-management' version '1.1.6' apply false
}

allprojects {
    group = 'com.nh.nsight.marketing'
    version = '0.1.0-SNAPSHOT'
}

subprojects {
    tasks.withType(JavaCompile).configureEach {
        options.encoding = 'UTF-8'
        options.compilerArgs.add('-parameters')   // @PathVariable 등
    }
}
```

---

## 3. 모듈 유형·의존 관계

### 모듈 분류

| 유형 | 모듈 | 플러그인 | 산출물 |
|------|------|----------|--------|
| 공통 라이브러리 | `common-core` | `java-library` | `.jar` |
| Web 프레임워크 | `common-web` | `java-library` | `.jar` |
| 업무 WAS | `cc-service` … `om-service` (17) | `spring.boot` + `war` | `{code}.war` |
| 공통 WAS | `common-etc`, `common-updownload` | `spring.boot` + `war` | `et.war`, `ud.war` |
| 데모 UI | `demo-ui` | `spring.boot` | `demo-ui.jar` |

### 의존 그래프

```text
common-core
    ↑ api
common-web
    ↑ implementation
cc-service … om-service
common-etc
common-updownload

demo-ui          (독립 — common-web 미의존)
```

| 모듈 | 주요 의존성 |
|------|-------------|
| `common-core` | `jakarta.validation-api` |
| `common-web` | `common-core`, `spring-boot-starter-web`, `validation`, `actuator` |
| `*-service` | `common-web`, JDBC, Session, MyBatis, H2, `providedRuntime` Tomcat |
| `demo-ui` | `spring-boot-starter-web` only |

상세: [`common-core/README.md`](../common-core/README.md), [`common-web/README.md`](../common-web/README.md)

---

## 4. 주요 Gradle 태스크

### 루트에서 전체 실행

| 명령 | 설명 |
|------|------|
| `gradle projects` | 포함 모듈 목록 |
| `gradle clean` | 모든 `build/` 삭제 |
| `gradle compileJava` | 전 모듈 컴파일 |
| `gradle test` | 전 모듈 테스트 (현재 테스트 클래스 없음) |
| `gradle build` | compile + test + jar/war |
| `gradle bootWar` | WAR 모듈 19개 `bootWar` |
| `gradle bootRun` | **실행 가능 모듈마다** bootRun (동시 기동 아님 — 모듈 지정 권장) |

### 단일 모듈 (`:` prefix)

| 명령 | 설명 |
|------|------|
| `gradle :sv-service:bootWar` | SV WAR만 빌드 |
| `gradle :sv-service:bootRun` | SV embedded 기동 (:8085) |
| `gradle :common-etc:bootRun` | ET 기동 (:8098) |
| `gradle :demo-ui:bootRun` | demo-ui (:8099) |
| `gradle :common-core:build` | common-core JAR |
| `gradle :sv-service:dependencies` | 의존 트리 확인 |

### WAR 모듈 공통 패턴

업무·공통 WAS `build.gradle`:

```gradle
plugins {
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
    id 'war'
}

dependencies {
    implementation project(':common-web')
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'  // 외부 Tomcat
    // ...
}

bootWar { archiveFileName = 'sv.war' }
war     { archiveFileName = 'sv.war' }
```

- **`bootWar`**: executable WAR (내장 Tomcat 포함) — 로컬 `java -jar`용
- **`war`**: plain WAR — 외부 Tomcat 배포 시 `providedRuntime` Tomcat 제외
- **ztomcat 배포**: `bootWar` 산출물(`build/libs/*.war`) 사용

---

## 5. WAR·JAR 산출물

### 출력 경로

| 모듈 | 경로 |
|------|------|
| 업무 WAR | `{code}-service/build/libs/{code}.war` |
| ET / UD | `common-etc/build/libs/et.war`, `common-updownload/build/libs/ud.war` |
| demo-ui | `demo-ui/build/libs/demo-ui.jar` |
| common-core/web | `{module}/build/libs/{module}-0.1.0-SNAPSHOT.jar` |

### WAR 파일명 매핑 (19개)

| 코드 | Gradle 모듈 | `archiveFileName` |
|------|-------------|-------------------|
| cc … om | `{code}-service` | `{code}.war` |
| et | `common-etc` | `et.war` |
| ud | `common-updownload` | `ud.war` |

### bin/ 수집

[`bin/build-wars.sh`](../bin/README.md) — 업무 17개 WAR를 `bin/{code}.war`로 복사 (Gradle 빌드 후).

---

## 6. gradle.properties

```properties
org.gradle.jvmargs=-Xmx2g -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true
```

| Property | 설명 |
|----------|------|
| `jvmargs` | Gradle daemon heap 2GB, UTF-8 |
| `parallel` | 독립 프로젝트 병렬 빌드 |
| `caching` | 빌드 캐시 (재빌드 가속) |

로컬 추가 설정 예 (`~/.gradle/gradle.properties` 또는 프로젝트 `gradle.properties`):

```properties
org.gradle.daemon=true
org.gradle.configuration-cache=false
```

---

## 7. 로컬 개발 명령

### 자주 쓰는 조합

```bash
# 전체 컴파일·테스트
gradle clean build

# 19 WAR (Tomcat 배포 전)
gradle bootWar

# 단일 업무 개발
gradle :sv-service:bootRun

# demo-ui + SV (터미널 2개)
gradle :sv-service:bootRun
gradle :demo-ui:bootRun
```

### 스크립트 단축

| 스크립트 | Gradle 동작 |
|----------|-------------|
| [`scripts/build-all.sh`](../scripts/build-all.sh) | `clean build bootWar` |
| [`scripts/run-local-sv.sh`](../scripts/run-local-sv.sh) | `:sv-service:bootRun` |
| [`bin/build-wars.sh`](../bin/build-wars.sh) | `:{code}-service:bootWar` + `bin/` 복사 |
| [`ztomcat/deploy-wars.sh`](../ztomcat/deploy-wars.sh) | `bootWar` + Tomcat webapps |

### bootRun 포트

각 `*Application`의 `LocalBootRun.apply(포트)` — [`common-core/README.md`](../common-core/README.md#7-로컬-실행-boot)

| 모듈 | 포트 |
|------|-----:|
| cc … om | 8080 … 8096 |
| ud / et | 8097 / 8098 |
| demo-ui | 8099 |

---

## 8. IDE 연동

### IntelliJ IDEA / Cursor

1. **Open** → 프로젝트 루트 (`settings.gradle` 있는 디렉터리)
2. **JDK 21** Project SDK 설정
3. Gradle JVM도 21로 설정 (Settings → Build → Gradle → Gradle JVM)
4. `*Application.main()` 또는 Gradle `bootRun` 실행

### Import 팁

- Gradle 프로젝트로 import (Eclipse `.project` 없음)
- `common-web` 변경 시 업무 모듈이 자동 recompile
- WAR 모듈 Run Configuration: `:sv-service:bootRun` Gradle task

---

## 9. GitLab CI

`.gitlab-ci.yml`:

```yaml
stages: build → test → package

build:   ./gradlew clean compileJava
test:    ./gradlew test
package: ./gradlew bootWar
         artifacts: */build/libs/*.war (7일)
```

| Stage | 태스크 | 산출물 |
|-------|--------|--------|
| build | `compileJava` | — |
| test | `test` | — |
| package | `bootWar` | `*/build/libs/*.war` |

**주의:** CI는 `./gradlew`를 사용하지만 저장소에 Wrapper가 없으면 파이프라인이 실패합니다. [Wrapper 추가](#gradle-wrapper-추가-선택) 또는 CI script를 `gradle`로 변경 필요.

---

## 10. 신규 모듈 추가

### 1) settings.gradle

```gradle
include 'xx-service'
```

### 2) xx-service/build.gradle

`sv-service/build.gradle` 복사 후:

```gradle
bootWar { archiveFileName = 'xx.war' }
war     { archiveFileName = 'xx.war' }
```

### 3) Application·Handler

- `XxApplication extends NsightBootApplication`
- `LocalBootRun.apply(포트)` — 미사용 포트 선택
- `TransactionHandler` `@Component`

### 4) ztomcat / bin (선택)

- `ztomcat/deploy-wars.sh` — `ALL_MODULES` 배열에 항목 추가
- `bin/build-wars.sh` — `SERVICES` 배열에 코드 추가

---

## 11. 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| `Could not find Java 21` | JDK 21 미설치 | JDK 21 설치 또는 Toolchain auto-download |
| `gradle: command not found` | Gradle 미설치 | Gradle 8.x PATH |
| `FAIL_ON_PROJECT_REPOS` | 서브프로젝트에 `repositories {}` | 루트 `settings.gradle`만 사용 |
| bootWar 후 Tomcat 404 | JDK 21 아닌 Tomcat | [`ztomcat/README.md`](../ztomcat/README.md) |
| `compileJava` 한글 깨짐 | encoding | 루트 `build.gradle` UTF-8 설정됨 — IDE encoding UTF-8 |
| CI `./gradlew` 실패 | Wrapper 없음 | `gradle wrapper` 생성 또는 CI 수정 |
| Windows deploy-wars Gradle | 하드코oded 경로 | `ztomcat/deploy-wars.bat`의 `GRADLE=` 또는 PATH `gradle` |
| `:bootRun` 포트 충돌 | 동일 포트 모듈 | 한 모듈만 기동 또는 포트 변경 |

### 유용한 진단 명령

```bash
gradle :sv-service:dependencies --configuration runtimeClasspath
gradle :sv-service:bootWar --info
gradle build --scan          # Gradle 8+ (빌드 스캔, 선택)
```

---

## 12. 관련 문서

- [프로젝트 README](../README.md)
- [common-core/README.md](../common-core/README.md)
- [common-web/README.md](../common-web/README.md)
- [bin/README.md](../bin/README.md)
- [scripts/README.md](../scripts/README.md)
- [ztomcat/README.md](../ztomcat/README.md)
