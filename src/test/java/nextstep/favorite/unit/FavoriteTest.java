package nextstep.favorite.unit;

import nextstep.exception.BadRequestException;
import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.line.Line;
import nextstep.subway.section.Section;
import nextstep.subway.station.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FavoriteTest {

    @Test
    void 존재하지_않는_경로로_즐겨찾기_생성시_오류() {
        Line 이호선 = new Line(1L, "2호선", "green");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(2L, "선릉역");
        Section 강남_선릉_구간 = new Section(1L, 강남역, 선릉역, 10);
        이호선.addSection(강남_선릉_구간);

        Line 칠호선 = new Line(2L, "7호선", "dark-green");
        Station 반포역 = new Station(3L, "반포역");
        Station 학동역 = new Station(4L, "학동역");
        Section 반포_학동_구간 = new Section(2L, 반포역, 학동역, 5);
        칠호선.addSection(반포_학동_구간);

        List<Line> 노선들 = List.of(이호선, 칠호선);

        Assertions.assertThatThrownBy(() -> new Favorite(1L, 강남역, 반포역, 노선들))
                .isInstanceOf(BadRequestException.class);
    }
}
