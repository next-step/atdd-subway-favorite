# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션


## 고민

- 즐겨찾기 관련 인수 테스트를 작성하다보니, 경로 조회 등.. 다른 인수테스트와 연관된 시나리오들이 적지않다.
  - 가령, 즐겨찾기 관련 테스트에서 작성한 `출발역과_도착역이_연결되지_않은_즐겨찾기_추가` 같은 경우도, 경로 조회 관련 테스트에서 충분히 검증한 것 같다고 생각된다.
  - 그러니깐, 도메인간 시나리오가 중복되는 느낌이 들면서, 굳이 여기서 해야할까?
    - 하지만, 이것은 내가 모든 도메인에 대해서 테스트를 진행하다보니 느끼는 것일 수 있다는 점이다.
    - 다른 개발자는 모든 테스트를 다 인지한 상태로 테스트를 구성하지 않을 것 같다.
      - 우선 중복된 테스트를 작성하더라도, 테스트는 많을 수록 좋다고 생각해보자.
- 즐겨찾기 조회 관련 서비스 레이어 테스트를 작성하고, 기능 구현을 완료했다.
  - 기존 `Favorite Service` 객체의 협력 객체 중 `Member Service` 객체를 `Favorite Controller`의 협력 객체로 이동시켰다.
    - 그렇다면, 기존 서비스 레이어 테스트 코드 중 회원 관련된 테스트 코드는 삭제하면 되는 것일까?
      - 인수 테스트로 커버되는 것일까?, 과연 좋은 방향으로 리팩토링을 진행한 것일지 궁금하다.
      - 이와 같이 의존성을 적절하게 분리하는 것이 좋은 방향일까? 
  ```
  @Nested
  class 회원_관련 {
      /**
       * When  즐겨 찾기 정보와 회원 정보를 통해 즐겨 찾기를 생성할 때,
       * When    회원 정보를 찾을 수 없을 경우
       * Then  해당 회원의 즐겨찾기 목록에 추가될 수 없다.
       */
      @Test
      void 존재하지_않는_회원() {
          // given
          Member member = new Member("test@test.com", "test001!", 30);
          ReflectionTestUtils.setField(member, "id", 999L);

          FavoriteRequest favoriteRequest = new FavoriteRequest(강남역_번호, 교대역_번호);

          // when
          assertThatExceptionOfType(NotFoundMemberException.class)
                  .isThrownBy(() -> {
                      favoriteService.createFavorite(favoriteRequest, member);
                  })
                  .withMessageMatching("회원 정보가 없습니다.");
      }
  }
  ```