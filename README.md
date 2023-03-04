# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 🚀 1단계 - 로그인

## 요구사항

### 기능 요구사항

- 토큰 생성
- 토큰을 이용하여 내 정보 조회

### 프로그래밍 요구사항

- 토큰을 이용한 인수 테스트를 작성하기

# 🚀 2단계 - 깃헙 로그인

## 요구사항

### 기능 요구사항

- 깃허브를 이용한 로그인 구현(토큰 발행)
- 가입이 되어있지 않은 경우 회원 가입으로 진행 후 토큰 발행

### 프로그래밍 요구사항

- GitHub 로그인을 검증할 수 있는 인수 테스트 구현(실제 GitHub에 요청을 하지 않아도 됨)

## 요구사항 설명

### 깃헙 로그인 API

- AuthAcceptanceTest 테스트 만들기

**Request**

```http
-POST /login/github HTTP/1.1
content-type: application/json
host: localhost: 8080

{
"code": "qwerasdfzxvcqwerasdfzxcv"
}
```

**Response**

- // 02.24 추가된 내용
- accessToken는 깃헙으로부터 받아온게 아니라 subway 애플리케이션에서 생성한 토큰
- 아이디/패스워드를 이용한 로그인 시 응답받는 토큰과 동일한 토큰

```http
  {
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
  }
```
