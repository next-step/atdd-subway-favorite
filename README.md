# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

--- 

## 2단계 미션

### 요구사항

- xxxAuthenticationFilter 의 구조화
  - 추상화를 통해 리팩터링
- auth 패키지와 member 패키지에 대한 순환 의존 제거
  - 현재 auth 패키지와 member 패키지는 서로 의존하고 있음
  - UserDetailsService 를 추상화하여 auth -> member 의존 제거하기

### TodoList
- [X] Chain Filter 추상화
- [X] Non-Chain Filter 추상화
- [X] auth, member 패키지 간의 순환 의존 제거

---

## 1단계 미션

### 요구사항

- Form 기반 로그인과 Bearer 기반 로그인 기능을 구현하세요.
  - `UsernamePasswordAuthenticationFilter`, `BearerTokenAuthenticationFilter` 를 구현하세요.
  - `AuthAcceptanceTest` 테스트를 통해 기능 구현을 확인하세요.
- 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하도록 수정하세요.
  - Token 을 이용한 로그인을 통해 관리자 여부를 판단
  - 해당 API: 생성, 수정, 삭제 API
  - 조회 API는 권한이 필요없음

### TodoList
- [X] 초기 데이터 구성
- [X] 인수테스트 구현
  - [X] Form 기반 로그인 인수테스트 구현
  - [X] Bearer 기반 로그인 인수테스트 구현
  - [X] API 인가 인수테스트 구현
    - [X] 생성 API
      - [X] 지하철역 생성
        - [X] 관리자인 경우
        - [X] 관리자가 아닌 경우
      - [X] 노선 생성
        - [X] 관리자인 경우
        - [X] 관리자가 아닌 경우
      - [X] 구간 생성
        - [X] 관리자인 경우
        - [X] 관리자가 아닌 경우
    - [X] 수정 API
      - [X] 노선 수정
        - [X] 관리자인 경우
        - [X] 관리자가 아닌 경우
    - [X] 삭제 API
      - [X] 지하철역 삭제
        - [X] 관리자인 경우
        - [X] 관리자가 아닌 경우
      - [X] 노선 삭제
        - [X] 관리자인 경우
        - [X] 관리자가 아닌 경우
      - [X] 구간 삭제
        - [X] 관리자인 경우
        - [X] 관리자가 아닌 경우
- [X] 로그인 기능 구현
  - [X] Form 기반 로그인 기능
  - [X] Bearer 기반 로그인 기능