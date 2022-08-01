# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션


## 첫 번째 미션 요구 사항

### 기능 요구사항

- Form 기반 로그인과 Bearer 기반 로그인 기능을 구현하세요.
  - `UsernamePasswordAuthenticationFilter`, `BearerTokenAuthenticationFilter를` 구현하세요.
  - `AuthAcceptanceTest` 테스트를 통해 기능 구현을 확인하세요.
- 지하철역, 노선, 구간을 변경하는 API는 관리자만 접근이 가능하도록 수정하세요.
  - Token을 이용한 로그인을 통해 관리자 여부를 판단
  - 해당 API: 생성, 수정, 삭제 API
  - 조회 API는 권한이 필요없음

### 요구사항 설명

관리자 전용 API 접근 제한

**초기 설정**

- 관리자 전용 API에 대해 접근 제한 기능을 구현하기 위해서는 관리자 멤버와 역할이 미리 설정되어있어야 합니다.
- 인수 테스트를 수행하기 전 공통으로 필요한 멤버, 역할은 초기에 설정할 수 있도록 하세요.
- `DataLoader`를 활용할 수 있습니다.

**권한 검증**

- API 별 권한 검증을 위해 `@Secured` 를 활용하세요.

```java
@PostMapping("/stations")
@Secured("ROLE_ADMIN")
public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
    StationResponse station = stationService.saveStation(stationRequest);
    return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
}
```

**권한을 검증하는 인수 테스트**

- 관리자만 접근 가능한 기능을 검증하는 인수 테스트를 만들기
- 관리자 여부는 Header에 담기는 Token 정보를 이용
- 토큰이 유효하지 않은 경우 에러 응답이 오는지 확인

### BearerTokenAuthenticationFilter

행위
- 토큰값을 가져와 사용자 정보를 가져와 권한 정보를 context에 저장합니다.
  - 토큰이 존재하지 않으면 다음 필터로 넘어갑니다.
  - 토큰이 존재하면 해당 토큰이 유효한지 확인합니다.
  - 토큰에서 사용자의 정보를 확인합니다.
  - 사용자의 정보를 통해 사용자의 권한을 찾습니다.
  - 사용자의 정보와 권한을 저장해 컨텍스트에 저장합니다.
  - 다음 필터로 넘김니다.

### UsernamePasswordAuthenticationFilter

행위
- 사용자의 username과 password를 가져와 권한 정보를 context에 저장합니다.
  - 사용자 정보를 가져옵니다.
  - 비밀번호가 맞는지 확인합니다.
  - 사용자의 정보와 권한을 저장해 컨텍스트에 저장합니다.
  - 다음 필터로 넘김니다.


## 두 번째 미션 요구 사항

### 프로그래밍 요구사항

인증 로직(auth 패키지)에 대한 리팩터링을 진행하세요.

### 요구사항 설명 - 리팩터링 포인트

**XXXAuthenticationFilter의 구조화**

- AuthenticationFilter 성격상 두 분류로 구분할 수 있음
- TokenAuthenticationInterceptor와 UsernamePasswordAuthenticationFilter는 인증 성공 후 더이상의 Interceptor chain을 진행하지 않고 응답을 함
- BasicAuthenticationFilter와 BearerTokenAuthenticationFilter는 인증 성공 후 다음 Interceptor chain을 수행함
- 이 차이를 참고하여 각각 추상화 가능

**auth 패키지와 member 패키지에 대한 의존 제거** 

- 현재 auth 패키지와 member 패키지는 서로 의존하고 있음 
- UserDetailsService를 추상화 하여 auth -> member 의존을 제거하기

### 두 번째 미션 힌트 - TDD를 활용한 리팩터링 방법

1. 기존 코드는 그대로 두고 새로운 테스트를 만들기
   - 기존 코드나 기존 테스트를 먼저 제거한다면 엄청난 재앙이 시작된다.
2. 새로운 테스트를 만족하는 프로덕션 코드 만들기
   - 이때 불가피하게 코드 중복이 발생한다.
3. 기존 코드를 모두 대체했다면 그 때 기존 테스트와 함께 지우기
   - 이렇게 하면 리팩터링 하는 도중에 코드작업을 멈추거나 다른 개발을 하더라도 롤백하는 일이 없다. 
   - 단, 코드 중복이 되어있는 상태를 짧게 가져가도록 해야한다.
