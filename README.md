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

### 실습 - 구현사항자

- [X] 코드리뷰 반영
  - HandlerMethodArgumentResolver 처리
- [X] Github 호출 Client 단위 테스트 작성
  - [X] 권한증서로 GitHub Access Token 을 발급한다.
  - [X] 권한증서가 null 또는 공백이라면, GitHub Access Token 발급 요청 시 예외처리한다.
  - [X] Access Token 으로 GitHub 에서 사용자 프로필을 조회한다.
  - [X] GitHub 사용자 프로필 조회 시, Access Token 에 해당하는 사용자가 없으면 예외처리한다.
- [X] 인수테스트 조건 정의
  - [X] Github 로그인에 성공한다.
  - [X] 권한이 없는 사용자일 경우에는 예외처리한다.
- [X] 테스트 환경 설정
  - [X] Github 테스트 엔드포인트 생성
  - [X] 테스트 프로퍼티 설정 