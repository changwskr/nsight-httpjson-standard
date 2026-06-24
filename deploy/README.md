# deploy — 운영 배포 설정 예시

운영·스테이징 환경에서 NSIGHT WAS를 **Apache 앞단**에 두는 라우팅 설정 예시입니다.

---

## 디렉터리

| 경로 | 설명 |
|------|------|
| [`apache/nsight-marketing-routing.conf`](apache/nsight-marketing-routing.conf) | Apache `ProxyPass` — 업무별 context path 라우팅 |

---

## Tomcat / bootRun 로컬 개발

로컬 WAR 배포·기동은 [`ztomcat/`](../ztomcat/README.md)을 사용합니다.  
이 `deploy/` 디렉터리는 **운영 Apache 설정 참고용**입니다.

---

## 관련 문서

- [프로젝트 README](../README.md)
- [`ztomcat/README.md`](../ztomcat/README.md)
