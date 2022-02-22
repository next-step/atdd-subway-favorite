### 인수테스트 통합

```
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
```

### myInfoWithBearerAuth
* Request
```
POST /login/token HTTP/1.1
Content-Type: application/json; charset=UTF-8
Host: localhost:62083
Content-Length: 72

{
"email" : "login@email.com",
"password" : "password"
}
```
* Response
```
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
```

### 즐겨찾기 인수테스트 시나리오

```
Feature: 즐겨찾기를 관리한다.

  Background 
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
    And 회원 등록되어 있음
    And 로그인 되어있음

  Scenario: 즐겨찾기를 관리
    When 즐겨찾기 생성을 요청
    Then 즐겨찾기 생성됨
    When 즐겨찾기 목록 조회 요청
    Then 즐겨찾기 목록 조회됨
    When 즐겨찾기 삭제 요청
    Then 즐겨찾기 삭제됨
```

### 즐겨찾기 엔드포인트
* 생성
```
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

```
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=204A5CC2753073508BE5CE2343AE26F5; Path=/; HttpOnly
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1
```

* 조회
```
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
```
```
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
* 삭제
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