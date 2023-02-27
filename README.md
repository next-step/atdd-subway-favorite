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