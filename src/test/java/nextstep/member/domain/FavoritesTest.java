package nextstep.member.domain;

import nextstep.member.domain.exception.CantAddFavoriteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoritesTest {

    @DisplayName("즐겨찾기 추가")
    @Test
    void add() {
        // given
        Favorites favorites = new Favorites();

        // when
        Favorite favorite = new Favorite(1L, 2L);
        favorites.add(favorite);

        // then
        assertThat(favorites.matchingFavorite(1L, 2L)).isEqualTo(favorite);
    }

    @DisplayName("출발역과 종착역이 같은 즐겨찾기 추가시 예외")
    @Test
    void add_Exception1() {
        // given
        Favorites favorites = new Favorites();

        Favorite invalidFavorite = new Favorite(1L, 1L);

        // when + then
        assertThatThrownBy(() -> favorites.add(invalidFavorite))
                .isInstanceOf(CantAddFavoriteException.class)
                .hasMessage(CantAddFavoriteException.INVALID_SOURCE_AND_TARGET);
    }

    @DisplayName("이미 등록된 즐겨찾기를 또 추가시 예외")
    @Test
    void add_Exception2() {
        // given
        Favorites favorites = new Favorites();
        Favorite favorite = new Favorite(1L, 2L);

        favorites.add(favorite);

        // when + then
        assertThatThrownBy(() -> favorites.add(favorite))
                .isInstanceOf(CantAddFavoriteException.class)
                .hasMessage(CantAddFavoriteException.ALREADY_ADDED);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void delete() {
        // given
        Favorites favorites = new Favorites();
        Favorite favorite = new Favorite(1L, 2L);
        favorites.add(favorite);

        // when
        Long favoriteId = 1L;
        ReflectionTestUtils.setField(favorite, "id", favoriteId);
        favorites.delete(favoriteId);

        // when
        assertThatThrownBy(() -> favorites.matchingFavorite(1L, 2L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
