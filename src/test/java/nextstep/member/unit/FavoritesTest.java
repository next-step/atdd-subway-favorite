package nextstep.member.unit;

import nextstep.member.application.exception.ErrorCode;
import nextstep.member.application.exception.FavoriteException;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.Favorites;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoritesTest {
    private Station 교대역 = new Station("교대역");
    private Station 양재역 = new Station("양재역");

    @Test
    @DisplayName("즐거찾기 추가 성공")
    void addFavorite() {
        Favorites favorites = new Favorites();

        favorites.add(new Favorite(1L, 교대역, 양재역));

        assertThat(favorites.getFavorites()).contains(new Favorite(1L, 교대역, 양재역));
    }

    @Test
    @DisplayName("즐겨찾기 삭제 성공")
    void deleteFavorite() {
        Favorites favorites = new Favorites(new Favorite(1L, 교대역, 양재역));

        favorites.remove(1L);

        assertThat(favorites.getFavorites()).doesNotContain(new Favorite(1L, 교대역, 양재역));
    }

    @Test
    @DisplayName("존재하지 않는 즐겨찾기 삭제 시 에러")
    void deleteFavoriteFail() {
        Favorites favorites = new Favorites(new Favorite(1L, 교대역, 양재역));

        assertThatThrownBy(() -> favorites.remove(2L))
                .isInstanceOf(FavoriteException.class)
                .hasMessage(ErrorCode.CANNOT_DELETE_NOT_EXIST_FAVORITE.getMessage());
    }
}
