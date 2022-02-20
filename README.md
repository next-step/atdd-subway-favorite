<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 화이팅

|주차|실습|날짜|
|:---:|:---:|:---:|
|3주차|로그인 프로세스 실습|22/02/10|
|3주차|토큰 기반 로그인 구현|22/02/10|
|3주차|step1 리뷰 반영|22/02/13|
|3주차|인증 로직 리팩토링|22/02/13|
|3주차|step2 리뷰 반영|22/02/15|
|3주차|step3 즐겨찾 기능 구현|22/02/18|

### 로그인 프로세스 실습 요구 사항
- [x] 패키지 구조 리팩토링
- [x] MemberAcceptanceTest 의 인수 테스트 통합하기
- [x] AuthAcceptanceTest 의 myInfoWithSession 테스트 메서드를 성공 시키기

### 실습 요구사항
- [x] AuthAcceptanceTest 의 myInfoWithBearerAuth 테스트 메서드를 성공 시키기  
  TokenAuthenticationInterceptor 을 구현한다.
  - [x] request 에서 인증에 사용된 email, password 를 통해 AuthenticationToken 을 생성한다.
  - [x] AuthenticationToken 의 정보를 통해 Authentication 을 생성한다.
  - [x] authentication 을 통해 jwtToken 을 생성하여 response 에 담는다.
  - [x] session 에 SecurityContext 를 담는다.
- [x] MemberAcceptanceTest 의 manageMyInfo 성공 시키기  
  @AuthenticationPrincipal 을 활용하여 메서드 파라미터 주입을 시도해보자

**step2**
리팩터링
- [x] AuthenticationInterceptor 추상화
- [x] auth 패키지와 member 패키지에 대한 의존 제거 (UserDetailsService 추상화)
- [x] SecurityContextInterceptor 추상화

기능 구현
- [x] 내 정보 수정
- [x] 내 정보 삭제

**step3**
즐겨찾기를 하다 -> 선호 경로가 생성된다.  
즐겨찾기 기능을 구현한다.
- [x] 즐겨찾기 데이터를 저장할 엔티티를 새로 생성한다.
- [x] 인증된 회원에대해서만 로직을 처리하도록 구현한다.
- [x] 즐겨찾기 요청 
- [x] 즐겨찾기 취소 요청
- [x] 즐겨찾기 조회

### 예외 케이스
- [x] 인증없이 즐겨찾기 기능 API 에 접근하면 401 에러를 반환한다.
- [x] 본인의 선호 경로가 아닌 선호 경로에 대해 취소 요청을 하면 403 에러를 반환한다.
- [ ] 없는 선호 경로에 대해 취소 요청을 하면 404 에러를 반환한다.
- [ ] 없는 역을 대상으로 즐겨찾기를 요청하면 404 에러를 반환한다.
- [ ] 연결이 불가능한 역들을 대상으로 즐겨찾기를 요청하면 400 에러를 반환한다.

### 리뷰 반영사항
- [x] objectMapper Bean 잘 활용하기
- [x] 안쓰는 테스트 클래스 삭제하기
- [x] 인수 테스트 가독성 있게 메서드 명 수정하기 (개발자가 아닌 사람도 읽을 수 있도록)
- [x] 데이터 치환 과정에 대한 메서드 테스트 제거 (접근제어자 수정)
- [x] ObjectMapper 와 deserialize
- [x] 불필요한 interface 메서드 삭제
- [x] 내부 로직을 구성하는 메서드들을 interface 에서 삭제
- [x] 구현 클래스의 중복을 제거
- [x] 코드 리팩토링