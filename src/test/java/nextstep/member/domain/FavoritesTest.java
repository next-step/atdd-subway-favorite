package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.member.application.exception.UnAuthorizedException;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("즐겨찾기 목록 관련 기능")
class FavoritesTest {

    @DisplayName("즐겨찾기 목록에 즐겨찾기를 추가한다.")
    @Test
    void add() {
        Favorites favorites = new Favorites();
        Favorite favorite = new Favorite(new Station("강남역"), new Station("양재역"));

        favorites.add(favorite);

        Assertions.assertThat(favorites.getFavorites()).hasSize(1).containsExactly(favorite);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    public void delete() {
        Favorites favorites = new Favorites();
        Favorite favorite = new Favorite(new Station("강남역"), new Station("양재역"));
        ReflectionTestUtils.setField(favorite, "id", 1L);
        favorites.add(favorite);

        favorites.delete(1L);

        assertThat(favorites.getFavorites()).hasSize(0).doesNotContain(favorite);
    }

    @DisplayName("즐겨찾기 삭제시 포함되지 않은 아이디이면 예외처리힌다.")
    @Test
    public void deleteDoesNotContainId() {
        Favorites favorites = new Favorites();
        Favorite favorite = new Favorite(new Station("강남역"), new Station("양재역"));
        ReflectionTestUtils.setField(favorite, "id", 1L);
        favorites.add(favorite);

        assertThatThrownBy(() -> favorites.delete(3L)).isInstanceOf(UnAuthorizedException.class);
    }
}
