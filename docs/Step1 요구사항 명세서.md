# 요구사항 설명
## 1. myInfoWithBearerAuth 테스트 메서드를 성공 시키기
### 요청 
```http request
POST /login/token HTTP/1.1
Content-Type: application/json; charset=UTF-8
Host: localhost:62083
Content-Length: 72

{
  "email" : "login@email.com",
  "password" : "password"
}
```

### 응답 
```http request
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

## 2. TokenAuthenticationInterceptor 구현하기

* TokenAuthenticationInterceptorTest 단위 테스트를 작성하며 TDD로 기능 구현하기

## 3. 내 정보 관리 인수 테스트

* `/members/me` 로 멤버 조회/수정/삭제 기능을 요청하는 인수 테스트 작성   
* 로그인 후 token을 응답 받은 후 요청 시 포함시키기  

## 4. @AuthenticationPrincipal 적용

* Controller에서 LoginMember 정보를 받아올 때 @AuthenticationPrincipal를 활용하여 받기  
* AuthenticationPrincipalArgumentResolver를 참고하여 ArgumentResolver 기능을 사용하기

# 정리하자면 

1. TokenAuthenticationInterceptor TDD로 구현하기
2. myInfoWithBearerAuth 테스트 메서드를 성공 시키기
3. 전체적으로 `/members/me` 로 멤버 조회/수정/삭제 기능을 요청하는 인수 테스트 작성
4. LoginMember 가져오는 구문이 SecurityContextHolder 에서 직접 조회면, AuthenticationPrincipal를 활용 
