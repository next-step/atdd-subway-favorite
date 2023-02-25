# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 1단계 - 로그인

### 요구사항

#### 기능 요구사항

- [x] 토큰 생성
- [x] 토큰을 이용하여 내 정보 조회

#### 프로그래밍 요구사항

- [x] 토큰을 이용한 인수 테스트를 작성하기

#### 토큰 생성 API

- [x] AuthAcceptanceTest 테스트 성공 시켜야함
- [x] 인수 테스트 실행 시 미리 데이터가 있어야 하는 경우 데이터를 초기화도 함께 수행하기

``` Request
POST /login/token HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "password": "password",
    "email": "admin@email.com"
}
```

``` Response
HTTP/1.1 200 
Content-Type: application/json

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
}
```

#### 내 정보 조회 기능

- [x] 로그인하여 생성한 토큰을 이용하여 내 정보를 조회하는 API
- [x] MemberAcceptanceTest의 getMyInfo 테스트 완성하기
- [x] MemberController 의 findMemberOfMine 메서드 구현하기

``` Request
GET /members/me HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjYxOTExLCJleHAiOjE2NzI2NjU1MTEsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIiwiUk9MRV9BRE1JTiIsIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.3dFa5VjK9LuGCTOJZzpO6r5JC_QdqRLr_2Vnb_sdXe0
accept: application/json
host: localhost:8080
```

``` Response
HTTP/1.1 200 
Content-Type: application/json

{
    "id": 1,
    "email": "admin@email.com",
    "age": 20
}
```

### 🚀 2단계 - 깃헙 로그인

#### 기능 요구사항

- [ ] 깃허브를 이용한 로그인 구현(토큰 발행)
- [ ] 가입이 되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행

#### 프로그래밍 요구사항

- [ ] GitHub 로그인을 검증할 수 있는 인수 테스트 구현(실제 GitHub에 요청을 하지 않아도 됨)
- [ ] 깃헙 로그인 API AuthAcceptanceTest 테스트 만들기

``` Request
POST /login/github HTTP/1.1
content-type: application/json
host: localhost:8080

{
    "code": "qwerasdfzxvcqwerasdfzxcv"
}
```

``` Response
{
"accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
}
```