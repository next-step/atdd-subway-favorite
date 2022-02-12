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

## 💻 로그인 인증 프로세스 실습
### 요구사항
- [x] 패키지 구조 리팩터링(선택)
  - 뼈대 코드의 구조를 자신이 편한 구조로 리팩터링 하세요.
  - subway는 2주차까지의 미션의 샘플 코드 입니다.
  - auth는 인증과 관련된 로직입니다
  - member는 회원 관리 관련된 로직 입니다.

- [x] MemberAcceptanceTest의 인수 테스트 통합하기
  - 인수 조건
    ~~~
    Feature: 회원 정보를 관리한다.
    
      Scenario: 회원 정보를 관리
      When 회원 생성을 요청
      Then 회원 생성됨
      When 회원 정보 조회 요청
      Then 회원 정보 조회됨
      When 회원 정보 수정 요청
      Then 회원 정보 수정됨
      When 회원 삭제 요청
      Then 회원 삭제됨
    ~~~

- [x] AuthAcceptanceTest의 myInfoWithSession 테스트 메서드를 성공 시키기
  - GET /members/me 요청을 처리하는 컨트롤러 메서드를 완성하여 myInfoWithSession 인수 테스트를 성공시키세요
    - MemberController의 findMemberOfMine메서드를 구현하면 위의 요청을 처리할 수 있습니다.
  - Controller에서 로그인 정보 받아오기
    - SecurityContextHolder에 저장된 SecurityContext를 통해 Authentication 객체 조회
    - Authentication에 저장된 LoginMember의 정보로 존재하는 멤버인지 확인 후 멤버 정보 조회
  
## 🚀 1단계 - 토큰 기반 로그인 구현

### 요구 사항
- [x] AuthAcceptanceTest의 myInfoWithBearerAuth 테스트 메서드를 성공 시키기
  
~~~
Request

  POST /login/token HTTP/1.1
  Content-Type: application/json; charset=UTF-8
  Host: localhost:62083
  Content-Length: 72
  
  {
    "email" : "login@email.com",
    "password" : "password"
  }
~~~
  
~~~
Response

HTTP/1.1 200 OK
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 19 Mar 2021 02:04:36 GMT
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 383

{
  "accessToken" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjIsXCJlbWFpbFwiOlwiT1RIRVJfbG9naW5AZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwiT1RIRVJfcGFzc3dvcmRcIixcIm5hbWVcIjpcIuyCrOyaqeyekFwiLFwiY3JlZGVudGlhbHNcIjpcIk9USEVSX3Bhc3N3b3JkXCIsXCJwcmluY2lwYWxcIjpcIk9USEVSX2xvZ2luQGVtYWlsLmNvbVwifSIsImlhdCI6MTYxNjExOTQ3NywiZXhwIjoxNjE2MTIzMDc3fQ.XWoW0hzX09OUiO8LETcBp_oeXNctt1jjTGtlBpD1Zhk"
}
~~~
- [x] TokenAuthenticationInterceptor 구현하기
- [x] 내 정보 관리 인수 테스트 구현
  - [x] /members/me 로 멤버 조회/수정/삭제 기능을 요청하는 인수 테스트 작성
  - [x] 로그인 후 token을 응답 받은 후 요청 시 포함시키기
- [x] @AuthenticationPrincipal 적용
  - [x] Controller에서 LoginMember 정보를 받아올 때 @AuthenticationPrincipal를 활용하여 받기
  - [x] AuthenticationPrincipalArgumentResolver를 참고하여 ArgumentResolver 기능을 사용하기
 
- [x] MemberAcceptanceTest의 manageMyInfo 성공 시키기
  - [x] @AuthenticationPrincipal을 활용하여 로그인 정보 받아오기