# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 기능 요구 사항
### 1단계 - 인증 기반 인수 테스트 도구
- [X] Form 기반 로그인과 Bearer 기반 로그인 기능을 구현한다.
- [ ] 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하다.
  - [ ] 관리자 멤버와 역할을 설정한다.
  - [ ] Token을 이용한 로그인을 통해 관리자 여부를 판단한다.
  - [ ] 생성, 수정, 삭제 API는 권한이 필요하다.
  - [ ] 조회 API는 권한이 필요없다.