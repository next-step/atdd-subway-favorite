package nextstep.favorite.unit;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.Exception.SubwayException;
import nextstep.subway.line.Line;
import nextstep.subway.station.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {
    private final Station 교대역 = new Station(1L, "교대역");
    private final Station 강남역 = new Station(2L, "강남역");
    private final Station 양재역 = new Station(3L, "양재역");
    private final Station 남부터미널역 = new Station(4L, "남부터미널역");
    private final Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10L);
    private final Line 삼호선 = new Line("삼호선", "orange", 양재역, 남부터미널역, 5L);

    @DisplayName("연결되지 않은 즐겨찾기 객체 생성")
    @Test
    void 생성() {
        assertThatThrownBy(() -> new Favorite(1L, 교대역, 양재역, List.of(이호선, 삼호선)))
                .hasMessage("연결되지 않은 역 정보입니다.")
                .isInstanceOf(SubwayException.class);
    }
}
