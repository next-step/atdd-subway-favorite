# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 1단계 - 인증 기반 인수 테스트 도구

- 요구사항
- 기능 요구사항 
- [ ] 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하도록 수정하세요. 
  - 해당 API: 생성, 수정, 삭제 API 
  - 조회 API는 권한이 필요없음
- [x] Form 기반 로그인과 Bearer 기반 로그인 기능을 구현하세요.
  - [x] UsernamePasswordAuthenticationFilter, 
  - [x] BearerTokenAuthenticationFilter를 구현하세요.
  - AuthAcceptanceTest 테스트를 통해 기능 구현을 확인하세요.
요구사항 설명
  - [ ] 관리자 전용 API 접근 제한
  - 초기 설정
    - 관리자 전용 API에 대해 접근 제한 기능을 구현하기 위해서는 관리자 멤버와 역할이 미리 설정되어있어야 합니다.
    - 인수 테스트를 수행하기 전 공통으로 필요한 멤버, 역할은 초기에 설정할 수 있도록 하세요.
    - DataLoader를 활용할 수 있습니다.
  - [ ] 권한 검증
    - API 별 권한 검증을 위해 @Secured 를 활용하세요.
```
    @PostMapping("/stations")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResponse station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }
```

### 힌트
- 기존 인수 테스트 리팩터링 
  - 권한이 필요해진 API를 인수 테스트에서 호출 할 때 많은 부분에서의 코드 수정이 필요 
  - 공통으로 적용될 부분을 중복 제거한 상태라면 코드 수정의 부분이 줄어들 수 있음 
  - 인수 테스트에서 아래 부분을 메서드로 분리해놓을 경우 변경을 최소화 할 수 있음