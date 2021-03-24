package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteTest {
  private Favorites favorites;
  private Favorite favorite;
  private static final String EMAIL = "email@email.com";
  private static final String PASSWORD = "password";
  private static final int AGE = 20;
  private Station 강남역;
  private Station 광교중앙역;
  private Member 테스트회원;

  @BeforeEach
  void init(){
    favorites = new Favorites();
    favorite = new Favorite();
    강남역 = new Station("강남역");
    광교중앙역 = new Station("광교중앙역");
    테스트회원 = new Member(EMAIL,PASSWORD,AGE);
  }

  @DisplayName("즐겨찾기를 추가한다")
  @Test
  void addFavorite(){
    //given
    Favorite favorite = new Favorite(테스트회원,광교중앙역,강남역);
    //when
    favorites.add(favorite);
    //then
    assertThat(favorites.getFavorite().size()).isEqual(1);
  }

}
