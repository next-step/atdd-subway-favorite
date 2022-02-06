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

# 💻 로그인 인증 프로세스 실습
- MemberAcceptanceTest의 인수 테스트 통합하기
- AuthAcceptanceTest의 myInfoWithSession 테스트 메서드를 성공 시키기

# 🚀 1단계 - 토큰 기반 로그인 구현
- AuthAcceptanceTest의 myInfoWithBearerAuth 테스트 메서드를 성공 시키기
  - TokenAuthenticationInterceptor 구현하기
- MemberAcceptanceTest의 manageMyInfo 성공 시키기
  - @AuthenticationPrincipal을 활용하여 로그인 정보 받아오기
  

# 🚀 2단계 - 인증 로직 리팩터링
### 1. 1,2단계에서 구현한 인증 로직에 대한 리팩터링
- AuthenticationConverter 추상화
  - AuthenticationConverter 인터페이스 생성
  - SessionAuthenticationConverter 와 TokenAuthenticationConverter 테스트 작성 및 구현
  - 기존 코드 대체
  - 전체 테스트 수행 후 실패 테스트 확인
  - 기존 코드 제거
- AuthenticationInterceptor 추상화
  - AuthenticationInterceptor 인터페이스 생성
  - 기존 코드 및 관련 테스트는 그대로 둠(SessionAuthenticationInterceptor, TokenAuthenticationInterceptor)
  - TokenAuthenticationInterceptor2 와 SessionAuthenticationInterceptor2 테스트 작성 및 구현
  - 기존 코드 대체
  - 전체 테스트 수행 후 실패 테스트 확인
  - 기존 코드 제거
- auth 패키지와 member 패키지의 양방향 의존 제거
  - UserDetailsService 인터페이스 생성
  - CustomUserDetailService 가 UserDetailsService 를 구현하도록 수정
  - 전체 테스트 수행 후 실패 테스트 확인
- SecurityContextInterceptor 추상화
  - SecurityContextInterceptor 인터페이스 생성
  - 기존 코드 및 관련 테스트는 그대로 둠(SessionSecurityContextPersistenceInterceptor, TokenSecurityContextPersistenceInterceptor)
  - SessionSecurityContextPersistenceInterceptor2, TokenSecurityContextPersistenceInterceptor2 테스트 작성 및 구현
  - 기존 코드 대체
  - 전체 테스트 수행 후 실패 테스트 확인
  - 기존 코드 제거

### 2. 내 정보 수정 / 삭제 기능을 처리하는 기능 구현
- Controller에서 @ㅐ너테이션을 활용하여 Login 정보에 접근
  
# 🚀 3단계 - 즐겨찾기 기능 구현
- 즐겨 찾기 기능을 구현하기
- 로그인이 필요한 API 요청 시 유효하지 않은 경우 401 응답 내려주기

