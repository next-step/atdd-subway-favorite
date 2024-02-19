package nextstep.favorite.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;

public class FavoriteTest {
    // 생성
    @Test
    void 즐겨_찾기_생성() {
        // given
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실역");

        // when
        final Favorite favorite = new Favorite(station1, station2, 1L);

        // then
        assertThat(favorite).isNotNull();
    }

    // 같은 역은 생성이 불가하다.
    @Test
    void 즐겨_찾기_생성__같은_역은_생석이_불가하다() {
        // given
        final Station station1 = new Station("잠실역");

        // then
        assertThatThrownBy(() -> new Favorite(station1, station1, 1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    // 수정
    @Test
    void 즐겨_찾기_수정() {
        // given
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실역");
        final Favorite favorite = new Favorite(station1, station2, 1L);

        // when
        final Station newStation1 = new Station("잠실새내역");
        final Station newStation2 = new Station("잠실새내역");
        favorite.update(newStation1, newStation2);

        // then
        assertThat(favorite.getSourceStation()).isEqualTo(newStation1);
        assertThat(favorite.getTargetStation()).isEqualTo(newStation2);
    }

    @Test
    void 즐겨_찾기_수정__같은_역은_수정이_불가하다() {
        // given
        final Station station1 = new Station("잠실역");
        final Station station2 = new Station("잠실역");
        final Favorite favorite = new Favorite(station1, station2, 1L);

        // then
        assertThatThrownBy(() -> favorite.update(station1, station1))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
