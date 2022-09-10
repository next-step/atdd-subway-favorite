# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 1단계 - 인증 기반 인수 테스트 도구
### 요구사항
- [x] Form 기반 로그인과 Bearer 기반 로그인 기능을 구현
    - UsernamePasswordAuthenticationFilter, BearerTokenAuthenticationFilter를 구현
    - AuthAcceptanceTest 테스트를 통해 기능 구현을 확인
- [ ] 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하도록 수정
    - Token을 이용한 로그인을 통해 관리자 여부를 판단
    - 해당 API: 생성, 수정, 삭제 API
    - 조회 API는 권한이 필요없음