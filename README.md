# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# Step1 TODO list

- [ ] 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하도록 수정
- [ ] Form 기반 로그인과 Bearer 기반 로그인 기능을 구현
  - [ ] UsernamePasswordAuthenticationFilter 구현
  - [ ] BearerTokenAuthenticationFilter 구현
- [ ] AuthAcceptanceTest 테스트를 통해 기능 구현을 확인하기
- [x] 인수 테스트를 수행하기 전 공통으로 필요한 멤버, 역할은 초기에 설정하기 - DataLoader 활용
- [ ] API 별 권한 검증을 위해 @Secured 를 사용하기
