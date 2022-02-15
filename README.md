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

# 🚀 3단계 - 즐겨찾기 기능 구현
# 요구사항

---

- [x]  즐겨찾기 기능 구현하기
   - [x] 인수 테스트 작성
   - [x] 인수테스트를 통과하기 위한 상세 기능 단위 테스트 TDD
- [x]  로그인이 필요한 API 요청 시 유효하지 않은 경우 401 응답 내려주기
   - [ ]  `**AuthenticationInterceptor` 응답 메시지 반환하기**
   - [ ]  `AuthenticationPrincipalArgumentResolver` 테스트 작성하기

# 요구사항 설명

---

## Request / Response

### 생성

```java
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

```

```java
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=204A5CC2753073508BE5CE2343AE26F5; Path=/; HttpOnly
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1

```

### 조회

```java
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate

```

```java
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

```

### 삭제

```
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate

```

```
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=587FCC78DBF0EE1B6705C6EC3E612968; Path=/; HttpOnly
Date: Mon, 22 Mar 2021 14:27:37 GMT

```

## 즐겨찾기 기능 구현

- 회원 별로 즐겨찾기를 관리할 수 있도록 기능변경
- TDD 사이클을 적용해서 구현
- 기존 로직에 대하여 테스트 작성 연습

## 권한이 없는 경우 401 Unauthorized 응답

- 내 정보 관리 / 즐겨 찾기 기능은 로그인 된 상태에서만 가능
- 비로그인이거나 유효하지 않을 경우 401 Unauthorized 응답
