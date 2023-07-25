# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 1단계 - 내 정보 조회 기능 구현
### 기능 요구사항

## 요구사항 설명
### 인수 조건 도출
- 아래 시나리오를 참고하여 인수 조건을 도출하세요.
```java
## 내 정보 조회 시나리오
- 회원 생성
- 로그인 요청
- 토큰 발급
- 토큰을 이용한 내 정보 조회 요청
- 응답 확인
```
### 인수 테스트 작성
- <strong>MemberAcceptanceTest</strong>의 <strong>getMyInfo</strong> 메서드에 인수 테스트를 작성하세요.
- 내 정보 조회 API 요청 / 응답 포맷을 참고하세요.
#### Request
```java
GET /members/me HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjg3MDczNzcyLCJleHAiOjE2ODcwNzczNzIsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.1RtnNjTzhPg1uQejOdfBzpp2H0734KWjBFe59j5nZcM
accept: application/json
host: localhost:8080
```

#### Response
```java
HTTP/1.1 200 OK
Content-Type: application/json

{
    "id": 1,
    "email": "admin@email.com",
    "age": 20
}
```