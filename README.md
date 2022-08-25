# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 기능 요구 사항
### 1단계 - 인증 기반 인수 테스트 도구
- [X] Form 기반 로그인과 Bearer 기반 로그인 기능을 구현한다.
- [X] 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하다.
  - [X] 관리자 멤버와 역할을 설정한다.
  - [X] Token을 이용한 로그인을 통해 관리자 여부를 판단한다.
  - [X] 생성, 수정, 삭제 API는 권한이 필요하다.
  - [X] 조회 API는 권한이 필요없다.

### 2단계 - 인증 로직 리팩터링
- [ ] XXXAuthenticationFilter의 구조화 
  - [ ] AuthenticationFilter 성격상 두 분류로 구분할 수 있음
  - [ ] TokenAuthenticationInterceptor와 UsernamePasswordAuthenticationFilter는 인증 성공 후 더이상의 Interceptor chain을 진행하지 않고 응답을 함
  - [ ] BasicAuthenticationFilter와 BearerTokenAuthenticationFilter는 인증 성공 후 다음 Interceptor chain을 수행함
  이 차이를 참고하여 각각 추상화 가능
- [ ] auth 패키지와 member 패키지에 대한 의존 제거
  - [ ] 현재 auth 패키지와 member 패키지는 서로 의존하고 있음
  - [ ] UserDetailsService를 추상화 하여 auth -> member 의존을 제거하기