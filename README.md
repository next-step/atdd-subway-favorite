# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 1단계 - 인증 기반 인수 테스트 도구

- [x] Form 기반 로그인과 Bearer 기반 로그인 기능을 구현하세요.
  - [x] UsernamePasswordAuthenticationFilter, BearerTokenAuthenticationFilter를 구현하세요.
  - [x] AuthAcceptanceTest 테스트를 통해 기능 구현을 확인하세요.
- [x] 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하도록 수정하세요.
  - [x] Token을 이용한 로그인을 통해 관리자 여부를 판단
  - [x] 해당 API: 생성, 수정, 삭제 API
  - [x] 조회 API는 권한이 필요없음

- [x] DataLoader 활용
- [x] 기존 인수 테스트 리팩터링 (given)


## 🚀 2단계 - 인증 로직 리팩터링

- [x] XXXAuthenticationFilter의 구조화
   - [x] AuthenticationFilter 성격상 두 분류로 구분할 수 있음
   - [x] TokenAuthenticationInterceptor와 UsernamePasswordAuthenticationFilter는 인증 성공 후 더이상의 Interceptor chain을 진행하지 않고 응답을 함
   - [x] BasicAuthenticationFilter와 BearerTokenAuthenticationFilter는 인증 성공 후 다음 Interceptor chain을 수행함
  이 차이를 참고하여 각각 추상화 가능

- [x] auth 패키지와 member 패키지에 대한 의존 제거

## 🚀 3단계 - 즐겨찾기 기능 구현

- [x] 즐겨찾기 생성 API 추가
- [x] 즐겨찾기 조회 API 추가
- [x] 즐겨찾기 제거 API 추가
- [x] 권한이 없는 경우 401 Unauthorized 응답
