package nextstep.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

@DisplayName("즐겨찾기에 대한 테스트")
class FavoriteTest {
  private Long 회원아이디;
  private Station 첫번째역;
  private Station 두번째역;


  @BeforeEach
  void setUp(){
    Member member = new Member("asdfasdf", "sadfasdf",123);
    회원아이디 = 1L;
    첫번째역 = new Station("첫번째역");
    두번째역 = new Station("두번째역");
  }
  /**
   * Given 회원과 역 2개가 주어졌을 때,
   * When 즐겨찾기를 생성하면
   * Then 즐겨찾기 생성에 성공한다.
   */
  @Test
  @DisplayName("즐겨찾기를 생성한다.")
  void createFavorite(){
    // given

    // when
    Favorite favorite = Favorite.of(회원아이디 ,첫번째역, 두번째역);
    // then
    assertThat(favorite.getSource().equals(첫번째역));
    assertThat(favorite.getTarget().equals(두번째역));
    assertThat(favorite.getMemberId().equals(회원아이디));
  }
  /**
   * Given 회원과 역 2개가 주어졌을 때,
   * When 동일한 역으로 즐겨찾기를 생성하면
   * Then 즐겨찾기 생성에 성공한다.
   */
  @Test
  @DisplayName("오류 테스트: 같은 역으로는 즐겨찾기를 생성할 수 없다.")
  void createFavoriteWithSameStation(){
    // given

    // when
    Throwable thrown = catchThrowable(() -> { Favorite.of(회원아이디 ,첫번째역, 첫번째역); });

    // then
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
  }

}
