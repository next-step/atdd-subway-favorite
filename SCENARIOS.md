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

