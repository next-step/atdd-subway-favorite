# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

---
## 🚀 1단계 - 즐겨찾기 기능 완성

### 기능 요구사항
- [x] 요구사항 설명에서 제공되는 추가된 요구사항을 기반으로 즐겨 찾기 기능을 완성
- [x] 예외 케이스에 대한 검증도 포함하세요.
  - [x] 로그인이 필요한 API 유효하지 않은 경우
  - [x] 존재하지 않는 경로인 경우
  - [x] 등등
### 프로그래밍 요구사항
- [x] 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현
    - [x] 요구사항 설명을 참고하여 인수 조건을 정의
    - [x] 인수 조건을 검증하는 인수 테스트 작성
    - [x] 인수 테스트를 충족하는 기능 구현
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성
    - [x] 뼈대 코드의 인수 테스트를 참고
- [x] 인수 테스트 이후 기능 구현은 TDD로 진행
    - [x] 도메인 레이어 테스트는 필수
    - [ ] 서비스 레이어 테스트는 선택
  
### 요구사항 설명
### 1. 즐겨 찾기 기능 인수 테스트 작성
- 아래의 스펙에 맞춰 즐겨 찾기 기능의 인수 테스트를 작성
- 뼈대 코드로 제공되는 기능과는 다른 스펙이니 유의
#### API 종류
- 즐겨찾기 생성
- 즐겨찾기 조회
- 즐겨찾기 삭제

#### Request/Response
##### 생성
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
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1
```
##### 조회
```
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflat
```
```
HTTP/1.1 200 
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
            "name": "교대역"
        },
        "target": {
            "id": 3,
            "name": "양재역"
        }
    }
]
```
##### 삭제
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
Date: Mon, 22 Mar 2021 14:27:37 GMT
```

### 2. 즐겨 찾기 기능 개선하기
- 현재 뼈대 코드로 제공되는 즐겨 찾기 기능은 모든 사람이 공유하는 즐겨찾기 입니다.
- 개인별로 관리할 수 있는 즐겨찾기 기능으로 만들어주세요.

### 3. 예외처리
- 즐겨 찾기 기능이 정상적으로 동작하기 위해 필요한 예외 처리를 진행
  - 예시) 내가 등록하지 않은 즐겨찾기를 제거하려고 할 경우 등
- 예외 처리에 대한 테스트는 어느 레이어에서 진행해야 할지 고민

---
## 🚀 2단계 - 깃헙 로그인 구현

### 요구사항
### 기능 요구사항
- [x] 깃허브를 이용한 로그인 구현(토큰 발행)
- [x] 가입이 되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행

### 프로그래밍 요구사항
- [x] GitHub 로그인을 검증할 수 있는 인수 테스트 구현(실제 GitHub에 요청을 하지 않아도 됨)

### 요구사항 설명
- [x] AuthAcceptanceTest 테스트 만들기

#### Request
```
POST /login/github HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "code": "qwerasdfzxvcqwerasdfzxcv"
}
```
#### Response
- accessToken는 깃헙으로부터 받아온게 아니라 subway 애플리케이션에서 생성한 토큰
```
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
}
```

#### code 별 응답 response
- 매번 실제 깃헙 서비스에 요청을 보낼 수 없으니 어떤 코드로 요청이 오면 정해진 response를 응답하는 구조를 만든다.