# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 1단계 - 인증 기반 인수 테스트 도구
## 기능 요구사항
- [x] Form 기반 로그인과 Bearer 기반 로그인 기능을 구현
  - [x] UsernamePasswordAuthenticationFilter 구현
  - [x] BearerTokenAuthenticationFilter 구현
  - [x] AuthAcceptanceTest 테스트를 통해 구현 확인
- [x] 지하철역, 노선, 구간을 변경하는 API는 관리자만 이용 가능하도록 수정
  - Token을 이용한 로그인을 통해 관리자 여부를 판단
  - 해당 API: 생성, 수정, 삭제 API
  - 조회 API는 권한이 필요없음

# 2단계 - 인증 로직 리팩터링
## 기능 요구사항
- [] XXXAuthenticationFilter의 구조화
  - [x] TokenAuthenticationInterceptor와 UsernamePasswordAuthenticationFilter
  - [] BasicAuthenticationFilter와 BearerTokenAuthenticationFilter
- [] auth 패키지와 member 패키지에 대한 의존 제거
