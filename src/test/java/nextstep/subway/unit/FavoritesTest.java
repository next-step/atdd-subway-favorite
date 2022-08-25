package nextstep.subway.unit;

import nextstep.member.domain.Favorite;
import nextstep.member.domain.Favorites;
import nextstep.member.domain.exception.FavoriteException;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FavoritesTest {
    @Nested
    @DisplayName("성공")
    class Success {
        @DisplayName("즐겨찾기 추가")
        @Test
        void 즐겨찾기_역_추가() {
            // given
            Station 강남역 = new Station("강남역");
            Station 역삼역 = new Station("역삼역");
            Favorite favorite = new Favorite(강남역, 역삼역);
            Favorites favorites = new Favorites();

            // when
            favorites.add(favorite);

            // then
            assertThat(favorites.getFavorites()).contains(favorite);
        }

        @DisplayName("즐겨찾기 삭제")
        @Test
        void 즐겨찾기_역_삭제() {
            // given
            Station 강남역 = new Station("강남역");
            Station 역삼역 = new Station("역삼역");
            Favorite favorite = new Favorite(강남역, 역삼역);
            Favorites favorites = new Favorites();
            favorites.add(favorite);

            // when
            favorites.delete(favorite);

            // then
            assertThat(favorites.getFavorites()).doesNotContain(favorite);
        }
    }

    @Nested
    @DisplayName("실패")
    class Failure {
        @DisplayName("즐겨찾기에 같은 출발역과 도착역이 등록되어 있으면 추가 실패")
        @Test
        void 중복_즐겨찾기_노선_추가_실패() {
            // given
            Station 강남역 = new Station("강남역");
            Station 역삼역 = new Station("역삼역");
            Favorites favorites = new Favorites();

            // when
            Favorite favorite = new Favorite(강남역, 역삼역);
            favorites.add(favorite);

            // then
            assertThatThrownBy(() -> favorites.add(favorite))
                    .isInstanceOf(FavoriteException.class)
                    .hasMessage(FavoriteException.DUPLICATION_FAVORITE);
        }

        @DisplayName("출발역과 도착역이 같으면 즐겨찾기 추가 실패")
        @Test
        void 즐겨찾기_같은역_추가_실패() {
            // given
            Station 강남역 = new Station("강남역");

            // when
            // then
            assertThatThrownBy(() -> new Favorite(강남역, 강남역))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(FavoriteException.SAME_SOURCE_AND_TARGET);
        }
    }
}
