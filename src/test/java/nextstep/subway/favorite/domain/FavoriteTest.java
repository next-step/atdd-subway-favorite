package nextstep.subway.favorite.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("지하철 즐겨찾기 Domain 단위 테스트")
class FavoriteTest {

    private Station savedStationGangnam;
    private Station savedStationCheonggyesan;

    private Line lineNewBunDang;

    private Favorite favorite;

    @BeforeEach
    void setUp() {
        savedStationGangnam = new Station(1L, "강남역");
        savedStationCheonggyesan = new Station(2L, "청계산입구역");

        lineNewBunDang = new Line(1L, "신분당선", "bg-red-600");
        lineNewBunDang.addSection(savedStationGangnam, savedStationCheonggyesan, 10);

        favorite = new Favorite();
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void createFavorite() {
        // when
        favorite.createFavorite(savedStationGangnam, savedStationCheonggyesan);

        // then
        assertThat(favorite.getFavorites()).hasSize(1);
        assertThat(favorite.getFavorites()).containsAll(savedStationGangnam, savedStationCheonggyesan);
    }

    @Test
    @DisplayName("이미 존재하는 구간을 즐겨찾기 등록할 경우 에러 발생")
    void validateSameFavorite() {
        // given
        favorite.createFavorite(savedStationGangnam, savedStationCheonggyesan);

        // when & then
        assertThatExceptionOfType(FavoriteExistException.class)
                .isThrownBy(() -> {
                    favorite.createFavorite(savedStationGangnam, savedStationCheonggyesan);
                });
    }
}
