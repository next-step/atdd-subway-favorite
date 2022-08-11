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

### 인증 필터 - Authentication Filter

인증 필터는 사용자의 로그인을 돕고, 더이상의 Interceptor chain을 진행하지 않고 응답합니다.
인증 필터에서는 TokenAuthenticationInterceptor와 UsernamePasswordAuthenticationFilter가 포함됩니다.

### 인증 필터 동작

1. request에서 사용자의 정보를 가져옵니다.
2. 사용자 principal을 이용해 사용자의 정보를 찾습니다.
3. 사용자 credential이 일치하는지 확인합니다.
4. Response Status OK 응답을 보냅니다.

### 인가 필터 - AuthorizationFilter

인가 필터에서는 이용하려는 리소스에 권한이 있는지 확인합니다. 성공 후 다음 Interceptor chain을 수행합니다.
인가 필터에서는 BasicAuthenticationFilter와 BearerTokenAuthenticationFilter가 포함됩니다.

### 인가 필터 동작

1. request header에서 Authentications 키 값에 저장된 토큰이 유효한지 검사합니다.
2. 토큰을 이용해 사용자의 정보를 찾습니다.
4. SecurityContext에 사용자 정보와 권한을 저장합니다.

## 세 번째 미션 요구 사항

### 기능 요구사항

- 요구사항 설명에서 제공되는 추가된 요구사항을 기반으로 즐겨 찾기 기능을 리팩터링하세요.
- 추가된 요구사항을 정의한 인수 조건을 도출하세요.
- 인수 조건을 검증하는 인수 테스트를 작성하세요.
- 예외 케이스에 대한 검증도 포함하세요.
  - 로그인이 필요한 API 요청 시 유효하지 않은 경우 401 응답 내려주기

### 프로그래밍 요구사항

- 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현하세요.
  - 요구사항 설명을 참고하여 인수 조건을 정의
  - 인수 조건을 검증하는 인수 테스트 작성
  - 인수 테스트를 충족하는 기능 구현
- 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
  - 뼈대 코드의 인수 테스트를 참고
- 인수 테스트 이후 기능 구현은 TDD로 진행하세요.
  - 도메인 레이어 테스트는 필수
  - 서비스 레이어 테스트는 선택

### 추가되는 인수테스트

- 로그인한 사용자가 즐겨찾기에 등록합니다.
- 로그인한 사용자가 즐겨찾기에 등록하고 즐겨찾기를 조회합니다.
- 로그인한 사용자가 즐겨찾게이 등록하고 즐겨찾기를 삭제합니다.
- 로그인하지 않은 사용자가 즐겨찾기와 관련된 요청을 진행하면 401 Unauthorized 응답을 받습니다.

### 추가되는 모델링

Favorite

- 상태
  - 즐겨 찾기의 식별자, 사용자의 식별자, 그리고 출발역과 도착역의 식별자를 가집니다.
    - 출발역과 도착역의 식별자는 같을 수 없습니다.

### 추가되는 클래스

FavoriteService

- 행위
  - 로그인한 사용자의 정보와 출발역, 그리고 도착역을 가져와 즐겨찾기를 등록합니다.
  - 로그인한 사용자의 정보를 가져와 사용자의 즐겨찾기 목록을 조회합니다.
  - 로그인한 사용자의 정보와 즐겨찾기의 식별자를 가져와 즐겨찾기를 삭제합니다.
    - 등록한 사용자가 아닌 경우 Unauthorized 예외가 발생합니다.

FavoriteController

- 행위
  - 로그인한 사용자의 정보와 출발역, 그리고 도착역을 가져와 즐겨 찾기를 등록합니다.
    - 로그인된 사용자가 아닌경우 Unauthorized 예외가 발생합니다.
    - 즐겨찾기에 등록되면 `201 Created`를 반환합니다.
  - 로그인한 사용자의 정보를 가져와 사용자의 즐겨찾기 목록을 조회합니다.
    - 로그인된 사용자가 아닌경우 Unauthorized 예외가 발생합니다.
    - 응답은 즐겨찾기 식별자와 출발역의 정보, 그리고 도착역의 정보의 리스트를 가집니다.
    - 목록이 조회되면 `200 Ok`를 반환합니다.
  - 로그인한 사용자의 정보와 즐겨찾기의 식별자를 가져와 등록된 사용자의 즐겨찾기를 삭제합니다.
    - 로그인된 사용자가 아닌경우 Unauthorized 예외가 발생합니다.
    - 삭제가 완료되면 `204 No Content`를 반환합니다.
