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

### 실습 - 구현사항

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