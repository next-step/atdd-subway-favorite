package nextstep.favorite.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.favorite.domain.Favorite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 엔티티 단위 테스트")
class FavoriteTest {
  @DisplayName("사용자가 즐겨찾기를 소유하고 있는지 확인한다.")
  @Test
  void isOwner() {
    Favorite favorite =
        Favorite.builder().id(1L).sourceStationId(1L).targetStationId(2L).memberId(1L).build();
    assertThat(favorite.isOwner(1L)).isTrue();
    assertThat(favorite.isOwner(2L)).isFalse();
  }

  @DisplayName("memberId가 null인 경우 false를 반환한다.")
  @Test
  void isOwner_memberIdIsNull() {
    Favorite favorite = Favorite.of(1L, 2L, null);
    assertThat(favorite.isOwner(2L)).isFalse();
  }
}
