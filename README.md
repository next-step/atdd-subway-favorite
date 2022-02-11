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
- [ ] new ObjectMapper 제거
- AuthenticationInterceptorTest 불필요
  - `따로 SessionAuthenticationInterceptorTest와 TokenAuthenticationInterceptorTest를 잘 만들어주셔서 요 테스트는 꼭 없어도 될 것 같습니다 :)`
  - public으로 열려있으니 무조건 테스트 해야겠다는 생각만 들어서 테스트를 작성하다 보니 중복 테스트가 생겨서 상위 클래스의 메소드를 테스트 했는데
  - 피드백을 받고 생각해보니 XXXAuthenticationInterceptorTest 에서 preHandle을 통해 간접 테스트가 가능하니 꼭 있어야할 테스트는 아닌 것 같다.
  - 이왕 작성해뒀으니 굳이 삭제 하지 않아도 될 것 같다.
