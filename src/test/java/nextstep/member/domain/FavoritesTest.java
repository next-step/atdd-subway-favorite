package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.auth.AuthenticationException;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoritesTest {

    @DisplayName("즐겨 찾기 목록에 없는 츨겨 찾기를 삭제하면 예외가 발생한다")
    @Test
    void deleteNotExistsFavorites() {
        // given
        Favorites favorites = new Favorites();
        Station gangnamStation = new Station("강남역");
        Station yangjaeStation = new Station("양잭역");
        Favorite favorite = new Favorite(gangnamStation, yangjaeStation);

        // when,then
        assertThatThrownBy(() -> favorites.delete(favorite))
                .isInstanceOf(AuthenticationException.class);
    }

}
