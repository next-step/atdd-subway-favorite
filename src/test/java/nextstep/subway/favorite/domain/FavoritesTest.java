package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.exception.FavoriteAlreadyExistException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("지하철 즐겨찾기 Domain 단위 테스트")
class FavoritesTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private Station savedStationGangnam;
    private Station savedStationCheonggyesan;

    private Line lineNewBunDang;
    private Favorites favorites;
    private Member member;


    @BeforeEach
    void setUp() {
        savedStationGangnam = new Station(1L, "강남역");
        savedStationCheonggyesan = new Station(2L, "청계산입구역");

        lineNewBunDang = new Line(1L, "신분당선", "bg-red-600");
        lineNewBunDang.addSection(savedStationGangnam, savedStationCheonggyesan, 10);

        member = new Member(1L, EMAIL, PASSWORD, AGE);
        favorites = new Favorites();

    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void createFavorite() {
        // when
        Favorite favorite = new Favorite(1L, member, savedStationGangnam, savedStationCheonggyesan);
        favorites.add(member, favorite);

        // then
        assertThat(favorites.getFavorites()).hasSize(1);
        assertThat(member.getFavorites()).containsExactly(favorite);
    }

    @Test
    @DisplayName("이미 존재하는 구간을 즐겨찾기 등록할 경우 에러 발생")
    void validateSameFavorite() {
        // given
        Favorite favorite = new Favorite(1L, member, savedStationGangnam, savedStationCheonggyesan);
        favorites.add(member, favorite);

        // when & then
        assertThatExceptionOfType(FavoriteAlreadyExistException.class)
                .isThrownBy(() -> favorites.add(member, favorite));
    }
}
