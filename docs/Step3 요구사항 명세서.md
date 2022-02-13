# Step3 요구사항 명세서 

* [] 즐겨 찾기 기능을 구현하기
* [] 로그인이 필요한 API 요청 시 유효하지 않은 경우 401 응답 내려주기

## 즐겨 찾기 기능 구현하기

### 생성 
#### Request

```http request
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

* accessToken
* source
* target

#### Response

```http request
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=204A5CC2753073508BE5CE2343AE26F5; Path=/; HttpOnly
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1
```

* 201 
* location url 

## 목록 조회 
#### Request

```http request
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
```

* accessToken

#### Response

```http request
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

* favoriteid
* source station 정보
* target station 정보

### 삭제 

### Request

```http request
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
```

* accessToken

### Response

```http request
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=587FCC78DBF0EE1B6705C6EC3E612968; Path=/; HttpOnly
Date: Mon, 22 Mar 2021 14:27:37 GMT
```
* noContent

## 401 응답 내려주기 

* 내 정보 관리 / 즐겨 찾기 기능은 로그인 된 상태에서만 가능
* 비로그인이거나 유효하지 않을 경우 401 Unauthorized 응답

* 처음 : 인터셉터에서 처리하면 될 것 같다.
* 구현시 : Argument Resolver 에서 Authentication 을 가져오는데 이때 검증한다.  
