<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```

---

### 로그인 인증 프로세스 실습
- [생략] 패키지 구조 리팩터링
- [x] MemberAcceptanceTest#manageMember 인수 테스트 통합하기
- [x] AuthAcceptanceTest#myInfoWithSession 테스트 통과하기

### 1단계 - 토큰 기반 로그인 구현

- [x] AuthAcceptanceTest#myInfoWithBearerAuth 테스트 통과하기
  - [x] TokenAuthenticationInterceptor#TODO 구현하기
  - [x] convert
  - [x] authenticate
  - [x] preHandle
- [x] MemberAcceptanceTest#manageMyInfo 테스트 통과하기
  - [x] @AuthenticationPrincipal 활용하기

### 1단계 - 피드백
- [x] ObjectMapper 를 static으로 만들어 쓰지 말고 필요한 객체에게 주입해서 쓸 수 있도록 변경
  - static 은 전역으로 관리되기 외부에서 접근이 가능하고
  - 상태가 변화될 경우 상태에 대한 추론이 어렵다

### 2단계 - 인증 로직 리팩터링
- [x] 인증 로직 리팩터링
  - [x] HandleInterceptor를 구현하는 구현체의 중복 제거 (추상화)
    - [x] AuthenticationInterceptor
    - [x] SecurityContextPersistenceInterceptor
  - [x] auth <-> member 양방향 의존성 제거
- [생략] 내 정보 수정 / 삭제 기능 구현
  - 1단계에서 구현

### 2단계 피드백
- [x] new ObjectMapper 제거
- AuthenticationInterceptorTest 불필요
  - `따로 SessionAuthenticationInterceptorTest와 TokenAuthenticationInterceptorTest를 잘 만들어주셔서 요 테스트는 꼭 없어도 될 것 같습니다 :)`
  - public으로 열려있으니 무조건 테스트 해야겠다는 생각만 들어서 테스트를 작성하다 보니 중복 테스트가 생겨서 상위 클래스의 메소드를 테스트 했는데
  - 피드백을 받고 생각해보니 XXXAuthenticationInterceptorTest 에서 preHandle을 통해 간접 테스트가 가능하니 꼭 있어야할 테스트는 아닌 것 같다.
  - 이왕 작성해뒀으니 굳이 삭제 하지 않아도 될 것 같다.

### 3단계 - 즐겨찾기 기능 구현
- [x] 즐겨찾기 기능 구현
  - [x] 회원별 즐겨찾기 기능
    - [x] 인수 조건 도출
    - [x] 인수 테스트 작성
    - [x] 즐겨찾기 등록
      - [x] 비정상 경로 등록 예외
    - [x] 즐겨찾기 목록조회
    - [x] 즐겨찾기 삭제
      - [x] 존재하지 않는 즐겨찾기 삭제 예외
- [x] 권한이 없을 떄 권한이 필요한 API 호출 시 401 코드 응답
  - [x] 내 정보 관리
  - [x] 즐겨 찾기 기능

###Feature: 즐겨찾기 관리 기능
    Background
      Given 지하철역이 등록되어 있고
      And 지하철 노선이 등록되어 있고
      And 지하철 노선에 구간이 등록되어 있고
      And 회원 등록되어 있고
      And 로그인 되어 있고
      And 출발역에서 도착역까지의 최단 경로를 조회했다

    Scenario: 즐겨찾기 관리
        When 최단 경로 즐겨찾기 생성 요청하면
        Then 즐겨찾기 생성된다
        When 즐겨찾기 목록 조회 요청하면
        Then 즐겨찾기 목록 조회된다
        When 즐겨찾기 삭제 요청하면
        Then 즐겨찾기 삭제된다

    Scenario: 비정상 경로 즐겨찾기 등록
        When 비정상 경로를 즐겨찾기 생성 요청하면
        Then 즐겨찾기 생성 실패한다

    Scenario: 존재하지 않은 즐겨찾기 삭제
        When 존재하지 않은 즐겨찾기 생성 요청하면
        Then 즐겨찾기 생성 실패한다

###Feature: 비로그인 상태에서 권한이 필요한 API 호출
    
    Background
        Given 로그인 되어 있지 않다

    Scenario 내 정보 관리 실패
        When 내 정보 조회 요청하면
        Then 내 정보 조회 실패한다
        When 내 정보 수정 요청하면
        Then 내 정보 수정 실패한다
        When 내 정보 삭제 요청하면
        Then 내 정보 삭제 실패한다

    Scenario 즐겨찾기 관리 실패
        When 즐겨찾기 등록 요청하면
        Then 즐겨찾기 등록 실패한다
        When 즐겨찾기 목록 조회 요청하면
        Then 즐겨찾기 목록 조회 실패한다
        When 즐겨찾기 삭제 요청하면
        Then 즐겨찾기 삭제 실패한다

### 3단계 피드백
- [x] 커스텀 예외를 통해 예외의 의도나 목적 노출
- [x] 연관관계 수정
  - 직접 참조는 의존성과 결합도가 굉장히 높다
  - 직접 참조 해야 하는 경우
    - 라이클 사이클이 같은 객체들 (생성과 삭제가 같이 되는 경우)
    - 제약사항을 공유하는 객체들
    - 본질적으로 결합도가 높은 객체들
  - 가능하다면 식별자를 이용한 간접 참조를 하자
- [ ] 연관관계 cascade
  - cascade 는 아직 어렵다..