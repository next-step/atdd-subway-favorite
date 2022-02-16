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

## 🚀 2단계 - 인증 로직 리팩터링

### 요구사항 - 인증 로직 리팩터링 및 기능 추가
- [x] 1,2단계에서 구현한 인증 로직에 대한 리팩터링을 진행하세요
- [x] 내 정보 수정 / 삭제 기능을 처리하는 기능을 구현하세요.
- [x] Controller에서 @ㅐ너테이션을 활용하여 Login 정보에 접근


### 요구사항 설명
#### TDD를 활용한 리팩터링 방법
1. 기존 코드는 그대로 두고 새로운 테스트를 만들기
   기존 코드나 기존 테스트를 먼저 제거한다면 엄청난 재앙이 시작됨...ㄷㄷ
2. 새로운 테스트를 만족하는 프로덕션 코드 만들기
   이때 불가피하게 코드 중복이 발생함
3. 기존 코드를 모두 대체했다면 그 때 기존 테스트와 함께 지우기
   이렇게 하면 리팩터링 하는 도중에 코드작업을 멈추거나 다른 개발을 하더라도 롤백해하는 일이 없음
   단, 코드 중복이 되어있는 상태를 짧게 가져가도록 해야함


## 3단계 - 즐겨찾기 기능 구현

### 요구사항
- [] 즐겨 찾기 기능을 구현하기
  - [] 회원 별로 즐겨찾기를 관리할 수 있도록 기능변경
  - [] TDD 사이클을 적용해서 구현
  - [] 기존 로직에 대하여 테스트 작성 연습
- [] 로그인이 필요한 API 요청 시 유효하지 않은 경우 401 응답 내려주기
  - [] 내 정보 관리 / 즐겨 찾기 기능은 로그인 된 상태에서만 가능
  - [] 비로그인이거나 유효하지 않을 경우 401 Unauthorized 응답

### 요구사항 설명
#### Request / Response
생성
~~~
POST /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
content-type: application/json; charset=UTF-8
content-length: 27
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate

{
    "source": "1",
    "target": "3"
}
~~~

~~~
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=204A5CC2753073508BE5CE2343AE26F5; Path=/; HttpOnly
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1
~~~

조회
~~~
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
~~~

~~~
HTTP/1.1 200 
Set-Cookie: JSESSIONID=B1F46939E516565DA3808E69D673F3B1; Path=/; HttpOnly
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 22 Mar 2021 14:27:37 GMT
Keep-Alive: timeout=60
Connection: keep-alive

[
    {
        "id": 1,
        "source": {
            "id": 1,
            "name": "교대역",
            "createdDate": "2021-03-22T23:27:37.185",
            "modifiedDate": "2021-03-22T23:27:37.185"
        },
        "target": {
            "id": 3,
            "name": "양재역",
            "createdDate": "2021-03-22T23:27:37.329",
            "modifiedDate": "2021-03-22T23:27:37.329"
        }
    }
]
~~~

삭제
~~~
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
~~~

~~~
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=587FCC78DBF0EE1B6705C6EC3E612968; Path=/; HttpOnly
Date: Mon, 22 Mar 2021 14:27:37 GMT
~~~

