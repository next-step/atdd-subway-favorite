# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

---

## 1단계 미션

### 요구사항

- Form 기반 로그인과 Bearer 기반 로그인 기능을 구현하세요.
  - `UsernamePasswordAuthenticationFilter`, `BearerTokenAuthenticationFilter` 를 구현하세요.
  - `AuthAcceptanceTest` 테스트를 통해 기능 구현을 확인하세요.
- 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하도록 수정하세요.
  - Token 을 이용한 로그인을 통해 관리자 여부를 판단
  - 해당 API: 생성, 수정, 삭제 API
  - 조회 API는 권한이 필요없음

### TodoList
- [X] 초기 데이터 구성
- [ ] 인수테스트 구현
  - [X] Form 기반 로그인 인수테스트 구현
  - [ ] Bearer 기반 로그인 인수테스트 구현
  - [ ] API 인가 인수테스트 구현
    - [ ] 생성 API
      - [ ] 지하철역 생성
      - [ ] 노선 생성
      - [ ] 구간 생성
    - [ ] 수정 API
      - [ ] 노선 수정
    - [ ] 삭제 API
      - [ ] 지하철역 삭제
      - [ ] 노선 삭제
      - [ ] 구간 삭제
- [ ] 로그인 기능 구현
  - [X] Form 기반 로그인 기능
  - [ ] Bearer 기반 로그인 기능