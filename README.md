# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 1단계 - 로그인

### 요구사항

#### 기능 요구사항

- [] 토큰 생성
- [] 토큰을 이용하여 내 정보 조회

#### 프로그래밍 요구사항

- [] 토큰을 이용한 인수 테스트를 작성하기

#### 요구사항 설명

##### 토큰 생성 API

- 아이디와 패스워드를 이용하여 토큰을 생성하는 API
- AuthAcceptanceTest 테스트 성공 시켜야함
- 인수 테스트 실행 시 미리 데이터가 있어야 하는 경우 데이터를 초기화도 함께 수행하기

###### Request

```http
POST /login/token HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

###### Response

```http
HTTP/1.1 200 
Content-Type: application/json

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
}
```

##### 내 정보 조회 기능

- 로그인하여 생성한 토큰을 이용하여 내 정보를 조회하는 API
- `MemberAcceptanceTest` 의 `getMyInfo` 테스트 완성하기
- `MemberController` 의 `findMemberOfMine` 메서드 구현하기

###### Request

```http
GET /members/me HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjYxOTExLCJleHAiOjE2NzI2NjU1MTEsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiIsIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.3dFa5VjK9LuGCTOJZzpO6r5JC_QdqRLr_2Vnb_sdXe0
accept: application/json
host: localhost:8080

```

###### Response

```http
HTTP/1.1 200 
Content-Type: application/json

{
    "id": 1,
    "email": "admin@email.com",
    "age": 20
}
```

### 실습 - 구현사항자

- [X] 인수테스트 조건 정의
  - 로그인
    - 인증에 성공한다.
    - 패스워드 잘못 입력해서 인증에 실패한다.
  - 내 정보 조회
    - 내 정보 조회에 성공한다.
    - 토큰 정보가 없어서 내 정보 조회에 실패한다.
    - 토큰 정보가 유효하지 않아서 내 정보 조회에 실패한다.
    - 토큰에서 추출한 이메일 정보가 존재하지 않아서 정보 조회에 실패한다.
- [X] 인증 서비스 생성
  - [X] Bearer 토큰 인터셉터 생성
  - [X] Bearer 토큰 추출 클래스 생성
- [X] 에러 핸들러 처리
  - [X] 에러 메시지 생성
  - [X] 공통 에러 객체 생성
  - [X] 커스텀 익셉션 생성

## 🚀 2단계 - 깃헙 로그인

### 요구사항

#### 기능 요구사항

- [] 깃허브를 이용한 로그인 구현(토큰 발행)
- [] 가입이 되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행

#### 프로그래밍 요구사항

- [] GitHub 로그인을 검증할 수 있는 인수 테스트 구현(실제 GitHub에 요청을 하지 않아도 됨)

### 요구사항 설명

#### 깃헙 로그인 API

- [] AuthAcceptanceTest 테스트 만들기

##### Request

```http
POST /login/github HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "code": "qwerasdfzxvcqwerasdfzxcv"
}
```

##### Response

```http
{
    "accessToken": "asdfasdf.asdfasdf.asdfasfd"
}
```

#### code 별 응답 response

- 매번 실제 깃헙 서비스에 요청을 보낼 수 없으니 어떤 코드로 요청이 오면 정해진 response를 응답하는 구조를 만든다.

### 실습 - 구현사항

- [X] 코드리뷰 반영
  - HandlerMethodArgumentResolver 처리
- [X] Github 호출 Client 단위 테스트 작성
  - [X] 권한증서로 GitHub Access Token 을 발급한다.
  - [X] 권한증서가 null 또는 공백이라면, GitHub Access Token 발급 요청 시 예외처리한다.
  - [X] Access Token 으로 GitHub 에서 사용자 프로필을 조회한다.
  - [X] GitHub 사용자 프로필 조회 시, Access Token 에 해당하는 사용자가 없으면 예외처리한다.
- [X] 인수테스트 조건 정의
  - [X] Github 로그인에 성공한다.
  - [X] 가입을 하지 않은 사용자가 로그인을 요청할 경우에는 예외처리한다.
- [X] 테스트 환경 설정
  - [X] Github 테스트 엔드포인트 생성
  - [X] 테스트 프로퍼티 설정 

## 🚀 3단계 - 즐겨찾기 기능 구현

### 요구사항

#### 기능 요구사항

- 기존 인수 테스트와 동일

#### 프로그래밍 요구사항

- 기존 인수 테스트와 동일

### 요구사항 설명

#### Request / Response

##### 생성

```http
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

```http
HTTP/1.1 201 Created
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=204A5CC2753073508BE5CE2343AE26F5; Path=/; HttpOnly
Content-Length: 0
Date: Mon, 22 Mar 2021 14:27:37 GMT
Location: /favorites/1
```

##### 조회

```http
GET /favorites HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: application/json
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
```

```http
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

```http
DELETE /favorites/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwLFwicHJpbmNpcGFsXCI6XCJlbWFpbEBlbWFpbC5jb21cIixcImNyZWRlbnRpYWxzXCI6XCJwYXNzd29yZFwifSIsImlhdCI6MTYxNjQyMzI1NywiZXhwIjoxNjE2NDI2ODU3fQ.7PU1ocohHf-5ro78-zJhgjP2nCg6xnOzvArFME5vY-Y
accept: */*
host: localhost:60443
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/1.8.0_252)
accept-encoding: gzip,deflate
```

```http
HTTP/1.1 204 No Content
Keep-Alive: timeout=60
Connection: keep-alive
Set-Cookie: JSESSIONID=587FCC78DBF0EE1B6705C6EC3E612968; Path=/; HttpOnly
Date: Mon, 22 Mar 2021 14:27:37 GMT
```

#### 권한이 없는 경우 401 Unauthorized 응답

- 내 정보 관리 / 즐겨 찾기 기능은 로그인 된 상태에서만 가능
- 비로그인이거나 유효하지 않을 경우 401 Unauthorized 응답

### 실습 - 구현사항

- [X] 즐겨찾기 인수테스트 조건 정의
  - [] 등록
    - [] 로그인한 사용자는 즐겨찾기를 저장한다.
    - [] 등록되지 않는 역의 대한 즐겨찾기 요청은 예외처리한다.
    - [] 로그인하지 않은 사용자는 예외처리한다.
    - [] 등록되지 않은 역의 즐겨찾기 등록 요청은 예외처리한다
  - [] 조회
    - [] 로그인한 사용자는 저장된 즐겨찾기를 보여준다.
    - [] 로그인하지 않은 사용자는 예외처리한다.
    - [] 존재하지 않는 즐겨찾기 조회 요청은 예외처리한다.
  - [] 제거
    - [] 로그인한 사용자는 즐겨찾기 제거의 성공한다.
    - [] 로그인하지 않은 사용자는 예외처리한다.
    - [] 존재하지 않는 즐겨찾기는 제거 요청은 예외처리한다.
- [] 즐겨찾기 도메인
  - [] 즐겨찾기 객체 생성 
  - [] 즐겨찾기 서비스 테스트 작성
  - [] 즐겨찾기 DTO 생성