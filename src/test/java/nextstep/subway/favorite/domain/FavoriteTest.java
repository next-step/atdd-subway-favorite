package nextstep.subway.favorite.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.section.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.JGraphPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.testhelper.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FavoriteTest {

    private PathFinder pathFinder;
    private Lines lines;

    @BeforeEach
    void setUp() {
        Line 일호선 = new Line("일호선", "blue", StationFixture.잠실역, StationFixture.강남역, 10L);
        Line 이호선 = new Line("이호선", "green", StationFixture.강남역, StationFixture.삼성역, 10L);
        Line 삼호선 = new Line("삼호선", "orange", StationFixture.잠실역, StationFixture.선릉역, 2L);
        Line 사호선 = new Line("삼호선", "orange", StationFixture.교대역, StationFixture.서초역, 5L);
        Section addSection = new Section(
                StationFixture.선릉역,
                StationFixture.삼성역,
                3L);
        삼호선.addSection(addSection);
        pathFinder = new JGraphPathFinder();
        lines = Lines.from(List.of(일호선, 이호선, 삼호선, 사호선));
    }

    @Test
    @DisplayName("즐겨찾기를 등록 할 수 있다")
    void createFavorite1() {
        assertDoesNotThrow(() -> new Favorite(pathFinder, lines, StationFixture.강남역, StationFixture.잠실역, new Member()));
    }

    @Test
    @DisplayName("즐겨찾기를 등록 시 출발역과 도착역이 같을 경우 에러 발생")
    void createFavorite2() {
        assertThrows(IllegalArgumentException.class, () -> new Favorite(pathFinder, lines, StationFixture.강남역,
                StationFixture.강남역, new Member()));
    }

    @Test
    @DisplayName("즐겨찾기를 등록 시 출발역과 도착역을 포함하는 라인을 찾지 못했을 경우 에러 발생")
    void createFavorite3() {
        assertThrows(IllegalArgumentException.class, () -> new Favorite(pathFinder, lines, StationFixture.강남역,
                StationFixture.서초역, new Member()));
    }

    @Test
    @DisplayName("즐겨찾기를 등록 시 시작역과 도착역을 찾을 수 없는 경우 에러 발생")
    void createFavorite4() {
        assertThrows(IllegalArgumentException.class, () -> new Favorite(pathFinder, lines, StationFixture.강남역,
                StationFixture.오이도역, new Member()));
    }

}
