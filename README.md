# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

### step 1. 인증 기반 인수 테스트 도구
1. 기능 요구사항
   1. 지하철역, 노선, 구간을 변경하는 API 는 `관리자`만 접근이 가능하도록 수정!
      1. 해당 API: 생성, 수정, 삭제 API
      2. 조회 API 는 권한이 필요없음
   2. Form 기반 로그인과 Bearer 기반 로그인 기능을 구현!
      1. `UsernamePasswordAuthenticationFilter`, `BearerTokenAuthenticationFilter` 를 구현!
      2. `AuthAcceptanceTest` 테스트를 통해 기능 구현을 확인!
2. 요구사항 설명
   1. **관리자 전용 API 접근 제한**
   2. **초기 설정**
      1. 관리자 전용 API 에 대한 접근 제한 기능을 구현하기 위해서는 관리자 멤버와 역할이 `미리 설정`되어있어야 한다.
      2. 인수 테스트를 수행하기 전 공통으로 필요한 `멤버`, `역할`은 초기에 설정할 수 있도록 하자.
      3. `DataLoader` 를 활용할 수 있다.
   3. **권한 검증**
      1. API 별 권한 검증을 위해 `@Secured` 를 활용하자.
```java
public class StationController {

   @PostMapping("/stations")
   @Secured("ROLE_ADMIN")
   public ResponseEntity<StationRepsonse> createStation(@RequestBody StationRequest stationRequest) {
      StationResponse station = stationService.saveStation(stationRequest);
      return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
   }
}
```

### step 2. 인증 로직 리팩터링
1. 프로그래밍 요구사항
   1. 인증 로직(auth 패키지)에 대한 리팩터링을 해보자
2. 요구사항 설명 
   <br> **리팩터링 포인트** <br>
   `XXXAuthenticationFilter` 의 구조화
   - `AuthenticationFilter` 성격상 두 분류로 구분할 수 있음
   - `TokenAuthenticationInterceptor` 와 `UsernamePasswordAuthenticationFilter` 는 인증 성공 후 더 이상의 Interceptor chain 을 진행하지 않고 응답을 함
   - `BasicAuthenticationFilter` 와 `BearerTokenAuthenticationFilter` 는 인증 성공 후 다음 Interceptor chain 을 수행함
   - 이 차이를 참고하여 각각 `추상화` 가능

    <br>**auth 패키지와 member 패키지에 대한 의존 제거**
  - 현재 auth 패키지와 member 패키지는 서로 의존하고 있음
  - UserDetailsService 를 추상화하여 auth -> member 의존을 제거하기

3. 피드백 리스트
   1. (실무에서도) 작성된 e2e 테스트로도 충분하다 생각되면 추가적인 테스트 작성의 필요성은 두각되지 않는다고 말씀해주셨다. 
   2. 그에 따라서 각 클래스에 대한 단위 테스트가 필요하다고 생각되면 테스트 작성을 진행하자!
   3. LoginMemberService 를 UserDetailsService 로 추상화한거처럼, LoginMember 도 추상화를 해보자.
   4. 기존에 있던 인증, 인가를 담당하는 클래스를 구분해보자 (가독성을 위해 패키지 분리!)
   5. 구현체를 보면 중복되는 로직이 존재한다. 이는 변하지 않는 부분일 수 있는데 이 부분을 template method pattern 을 적용하여 분리해보자.
   6. (Optional) 범용적인 이름을 사용한 Custom Interceptor 에 좀 더 구체적인 역할을 표현할 수 있는 네이밍을 부여해보자.