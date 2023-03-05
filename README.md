# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션


# 1단계 - 로그인
## 요구사항
### 기능 요구사항
- 토큰 생성
- 토큰을 이용하여 내 정보 조회

### 프로그래밍 요구사항
- 토큰을 이용한 인수 테스트를 작성하기

## 요구사항 설명
### 토근 생성 API
- 아이디와 패스워드를 이용하여 토큰을 생성하는 API
- `AuthAcceptanceTest` 테스트를 성공 시켜야 함.
- 인수 테스트 실행 시 미리 데이터가 있어야 하는 경우, 데이터 초기화도 함께 수행하기

#### Request
```http request
POST /login/token HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

#### Response
```http request
HTTP/1.1 200 
Content-Type: application/json

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
}
```

### 내 정보 조회 기능
- 로그인하여 생성한 토큰을 이용하여 내 정보를 조회하는 API
- `MemberAcceptanceTest`의 `getMyIfo` 테스트 완성하기 
- `MemberController`의 `findMemberOfMine` 메서드 구현하기

#### Request
```http request
GET /members/me HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjYxOTExLCJleHAiOjE2NzI2NjU1MTEsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiIsIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.3dFa5VjK9LuGCTOJZzpO6r5JC_QdqRLr_2Vnb_sdXe0
accept: application/json
host: localhost:8080
```

#### Response
```http request
HTTP/1.1 200 
Content-Type: application/json

{
    "id": 1,
    "email": "admin@email.com",
    "age": 20
}
```

## 요구사항 기반 시나리오
- 로그인 토큰 발급 인수 테스트
  - 회원 1명을 등록한다.
  - 등록한 회원으로 로그인을 요청한다.
  - 토큰을 발급받는다.

- 로그인 토큰 발급 예외 인수 테스트
  - 회원 1명을 등록한다.
  - 등록된 회원가 다른 이메일로 로그인 요청한다.
  - 예외가 발생한다.

- 로그인 토큰 발급 예외 인수 테스트
  - 회원 1명을 등록한다.
  - 등록된 회원과 다른 패스워드로 요청한다.
  - 예외가 발생한다.

- 내 정보 조회 인수 테스트
  - 회원 1명을 등록한다.
  - 등록한 회원으로 로그인을 요청한다.
  - 토큰을 발급받는다.
  - 발급받은 토큰을 이용하여 내 정보를 조회한다.

- 내 정보 조회 예외 인수 테스트
  - 토큰 발급 없이, 내 정보를 조회한다.
  - 예외가 발생한다.


# 2단계 - 깃헙 로그인
## 요구사항
### 기능 요구사항
- 깃허브를 이용한 로그인 구현(토큰 발행)
- 가입이 되어있지 않은 경우, 회원 가입으로 진행 후 토큰 발행

### 프로그래밍 요구사항
- GitHub 로그인을 검증할 수 있는 인수 테스트 구현(실제 GitHub 에 요청을 하지 않아도 됨)

## 요구사항 설명
### 깃헙 로그인 API
- `AuthAcceptanceTest` 테스트 만들기

#### Request
```http request
POST /login/github HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "code": "qwerasdfzxvcqwerasdfzxcv"
}
```

#### Response
- accessToken는 깃헙으로부터 받아온 정보가 아니라 subway 서비스에서 생성한 토큰
- 아이디/패스워드를 이용한 로그인 시 응답받는 토큰과 동일한 토큰
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
}
```

### code 별 응답 response
- 매번 실제 깃헙 서비스에 요청을 보낼 수 없으니 어떤 코드로 요청이 오면 정해진 response 를 응답하는 구조를 만든다.


### 요구사항 기반 시나리오
- Github 에 이미 등록된 유저의 권한증서(code) 를 가지고
  - Github Auth Server (fake) 로 로그인 요청한다.
  - Github Auth Server 로 부터 발급받은 accessToken 으로 Github Resource Server (fake) 로 깃헙 프로필(이메일)을 요청한다.
  - 받아온 이메일로 member 를 조회한다.
    - member 가 존재하지 않으면 새로운 멤버를 우리 서버에 등록한다.
      - 해당 멤버의 토큰을 발급한다.
    - member 가 존재하면 해당 멤버의 토큰을 발급한다.

# 3단계 - 즐겨찾기 기능 구현
## 요구사항
### 기능 요구사항
- `요구사항 설명`에서 제공되는 추가된 요구사항을 기반으로 **즐겨 찾기 기능**을 리팩터링하세요.
- 추가된 요구사항을 정의한 **인수 조건**을 도출하세요.
- 인수 조건을 검증하는 **인수 테스트**를 작성하세요.
- 예외 케이스에 대한 검증도 포함하세요.
  - 로그인이 필요한 API 요청 시 유효하지 않은 경우 401 응답 내려주기
### 프로그래밍 요구사항
- **인수 테스트 주도 개발 프로세스**에 맞춰서 기능을 구현하세요.
  - `요구사항 설명`을 참고하여 인수 조건을 정의
  - 인수 조건을 검증하는 인수 테스트 작성
  - 인수 테스트를 충족하는 기능 구현
- 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
  - 뼈대 코드의 인수 테스트를 참고
- `인수 테스트 이후 기능 구현은 TDD로 진행하세요.`
  - 도메인 레이어 테스트는 필수
  - 서비스 레이어 테스트는 선택

## 요구사항 설명
### Request / Response
#### 생성
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
```http request
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1
```

#### 조회
```http request
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
```

```http request
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

#### 삭제
```http request
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
```
```http request
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Date: Mon, 22 Mar 2021 14:27:37 GMT
```

### 권한이 없는 경우 401 Unauthorized 응답
- 내 정보 관리 / 즐겨 찾기 기능은 로그인 된 상태에서만 가능
- 비로그인이거나 유효하지 않을 경우 401 Unauthorized 응답

## 요구사항 기반 시나리오
```
 고속터미널역 --- *7호선* --- 강남구청역
 
 교대역    --- *2호선*  ---  강남역
   |                        |
 *3호선*                   *신분당선*
   |                        |
 남부터미널역  --- *3호선* ---  양재
```
- 즐겨 찾기 기능을 위한 초기 데이터 셋팅
  - 고속터미널역 - 강남구청역 (7호선) 이 포함된 노선을 등록한다.
  - 교대역 - 강남역 (2호선) 이 포함된 노선을 등록한다.
  - 강남역 - 양재역 (신분당선) 이 포함된 노선을 등록한다.
  - 교대역 - 남부터미널역 (3호선) 이 포함된 노선을 등록한다.
  - 남부터미널역 - 양재역 (3호선) 이 포함된 노선을 등록한다.

- 즐겨찾기 생성 인수 테스트
  - 회원가입된 사용자로 로그인 한다.
  - 유효한 토큰과 함께 교대역 - 양재역을 즐겨찾기로 생성한다.
  - 교대역 - 양재역이 즐겨찾기로 생성된다.

- 즐겨찾기 생성 예외 인수 테스트
  - 회원가입된 사용자로 로그인 한다.
  - 유효한 토큰과 함께 고속터미널역 - 양재역을 즐겨찾기로 생성한다.
  - 고속터미널역에서 양재역으로 갈 수 있는 경로가 존재하지 않으므로 즐겨찾기로 생성할 수 없다.

- 즐겨찾기 목록 조회 인수 테스트
  - 회원가입된 사용자로 로그인 한다.
  - 유효한 토큰과 함께 교대역 - 양재역을 즐겨찾기로 생성한다.
  - 유효한 토큰과 함께 강남역 - 남부터미널역을 즐겨찾기로 생성한다.
  - 유효한 토큰과 함께 즐겨찾기 목록을 조회한다.
  - 해당 사용자의 즐겨찾기 목록이 조회된다.

- 즐겨찾기 삭제 인수 테스트
  - 회원가입된 사용자로 로그인 한다.
  - 유효한 토큰과 함께 교대역 - 양재역을 즐겨찾기로 생성한다.
  - 교대역 - 양재역 즐겨찾기 삭제 요청한다.
  - 해당 사용자의 교대역 - 양재역 즐겨찾기가 삭제된다.

- 두 사용자의 즐겨찾기 목록 조회 인수 테스트
  - 회원가입된 사용자1 로 로그인 한다.
  - 사용자 1의 유효한 토큰과 함께 교대역 - 양재역을 즐겨찾기로 생성한다.
  - 회원가입된 사용자2 로 로그인 한다.
  - 사용자 2의 유효한 토큰과 함께 강남역 - 남부터미널역을 즐겨찾기로 생성한다.
  - 사용자 2의 즐겨찾기 목록을 조회한다.
  - 강남역 - 남부터미널역 즐겨찾기가 조회되고, 교대역 - 양재역 즐겨찾기는 사용자 1의 즐겨찾기이므로 조회되지 않는다.

- 다른 사용자의 즐겨찾기 삭제 예외 인수 테스트
  - 회원가입된 사용자1 로 로그인 한다.
  - 사용자 1의 유효한 토큰과 함께 교대역 - 양재역을 즐겨찾기로 생성한다.
  - 회원가입된 사용자2 로 로그인 한다.
  - 사용자 2의 유효한 토큰과 함께 강남역 - 남부터미널역을 즐겨찾기로 생성한다.
  - 사용자 2가 교대역 - 양재역 즐겨찾기 삭제 요청한다.
  - 교대역 - 양재역 즐겨찾기는 사용자 1의 즐겨찾기이므로 삭제할 수 없다.

- 즐겨찾기 관리 기능 유효하지않은 사용자에 대한 예외 인수 테스트
  - 유효하지 않은 토큰으로 즐겨찾기 생성, 목록 조회, 삭제 요청한다.
  - 예외가 발생한다.