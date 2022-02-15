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

# 🚀 로그인 인증 프로세스 실습
- [x]  패키지 구조 리팩토링(선택)
- [x]  MemberAcceptanceTest의 인수 테스트 통합하기
- [x]  AuthAcceptanceTest의 myInfoWithSession 테스트 메서드를 성공 시키기

# 🚀 1단계 - 토큰 기반 로그인 구현
- [x]  AuthAcceptanceTest의 myInfoWithBearerAuth 테스트 메서드를 성공 시키기
    - [x]  TokenAuthenticationInterceptor 구현하기
- [x]  MemberAcceptanceTest의 manageMyInfo 성공 시키기
    - [x]  @AuthenticationPrincipal을 활용하여 로그인 정보 받아오기

# 🚀 2단계 - 토큰 기반 인증 로직 리팩토링
- [x] 인증 로직 리팩토링
