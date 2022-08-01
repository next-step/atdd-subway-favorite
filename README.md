# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 3단계 - 즐겨찾기 기능 구현
- 요구사항
  - 기능 요구사항
    - 요구사항 설명에서 제공되는 추가된 요구사항을 기반으로 즐겨 찾기 기능을 리팩터링하세요. 
    - 추가된 요구사항을 정의한 인수 조건을 도출하세요. 
    - 인수 조건을 검증하는 인수 테스트를 작성하세요. 
    - 예외 케이스에 대한 검증도 포함하세요. 
    - 로그인이 필요한 API 요청 시 유효하지 않은 경우 401 응답 내려주기

- 프로그래밍 요구사항
  - 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현하세요.
    - 요구사항 설명을 참고하여 인수 조건을 정의
    - 인수 조건을 검증하는 인수 테스트 작성
    - 인수 테스트를 충족하는 기능 구현
  - 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
    - 뼈대 코드의 인수 테스트를 참고
  - 인수 테스트 이후 기능 구현은 TDD로 진행하세요.
    - 도메인 레이어 테스트는 필수
    - 서비스 레이어 테스트는 선택

## 🚀 2단계 - 인증 로직 리팩터링
- 요구사항
- 프로그래밍 요구사항
  - [x] 인증 로직(auth 패키지)에 대한 리팩터링을 진행하세요.
- 요구사항 설명
  - 리팩터링 포인트
  - [x] XXXAuthenticationFilter의 구조화
    - [x] AuthenticationFilter 성격상 두 분류로 구분할 수 있음
    - [x] TokenAuthenticationInterceptor와 UsernamePasswordAuthenticationFilter는 인증 성공 후 더이상의 Interceptor chain을 진행하지 않고 응답을 함
    - [x] BasicAuthenticationFilter와 BearerTokenAuthenticationFilter는 인증 성공 후 다음 Interceptor chain을 수행함 
    이 차이를 참고하여 각각 추상화 가능
  - auth 패키지와 member 패키지에 대한 의존 제거
    - [x] 현재 auth 패키지와 member 패키지는 서로 의존하고 있음
    - [x] UserDetailsService를 추상화 하여 auth -> member 의존을 제거하기

## 🚀 1단계 - 인증 기반 인수 테스트 도구

- 요구사항
- 기능 요구사항 
- [x] 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하도록 수정하세요. 
  - 해당 API: 생성, 수정, 삭제 API 
  - 조회 API는 권한이 필요없음
- [x] Form 기반 로그인과 Bearer 기반 로그인 기능을 구현하세요.
  - [x] UsernamePasswordAuthenticationFilter, 
  - [x] BearerTokenAuthenticationFilter를 구현하세요.
  - AuthAcceptanceTest 테스트를 통해 기능 구현을 확인하세요.
요구사항 설명
  - [x] 관리자 전용 API 접근 제한
  - 초기 설정
    - 관리자 전용 API에 대해 접근 제한 기능을 구현하기 위해서는 관리자 멤버와 역할이 미리 설정되어있어야 합니다.
    - 인수 테스트를 수행하기 전 공통으로 필요한 멤버, 역할은 초기에 설정할 수 있도록 하세요.
    - DataLoader를 활용할 수 있습니다.
  - [x] 권한 검증
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