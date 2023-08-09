package nextstep.favorite.domain;

import nextstep.exception.ShortPathSameStationException;
import nextstep.exception.StationNotExistException;
import nextstep.line.domain.Line;
import nextstep.line.domain.PathFinder;
import nextstep.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.line.LineTestField.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {

    private static final Station GANGNAM_STATION = new Station("강남역");
    private static final Station SEOLLEUNG_STATION = new Station("선릉역");
    private static final Station SUWON_STATION = new Station("수원역");
    private static final Station NOWON_STATION = new Station("노원역");
    private static final Station DEARIM_STATION = new Station("대림역");

    private Line shinbundangLine;
    private Line twoLine;
    private Line threeLine;
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        shinbundangLine = new Line(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, GANGNAM_STATION, SEOLLEUNG_STATION, 2);
        twoLine = new Line(TWO_LINE_NAME, TWO_LINE_COLOR, SEOLLEUNG_STATION, SUWON_STATION, 3);
        threeLine = new Line(THREE_LINE_NAME, TRHEE_LINE_COLOR, GANGNAM_STATION, NOWON_STATION, 5);
        threeLine.addSection(NOWON_STATION, SUWON_STATION, 3);
        pathFinder = new PathFinder(List.of(shinbundangLine, twoLine, threeLine));
    }

    @DisplayName("경로가 정상일경우 즐겨찾기가 생성된다.")
    @Test
    void createFavorite() {
        // when
        Favorite favorite = new Favorite(1L, GANGNAM_STATION, NOWON_STATION, pathFinder);

        // then
        assertThat(favorite.getSource()).isEqualTo(GANGNAM_STATION);
        assertThat(favorite.getTarget()).isEqualTo(NOWON_STATION);
    }

    @DisplayName("경로에 포함되지 않은 역을 즐겨찾기로 등록할 경우 에러를 던진다")
    @Test
    void createFavorite_fail_not_exist_station_in_line() {
        // when then
        assertThatThrownBy(() -> new Favorite(1L, GANGNAM_STATION, DEARIM_STATION, pathFinder))
                .isExactlyInstanceOf(StationNotExistException.class)
                .hasMessage("노선에 역이 존재하지 않습니다.");
    }

    @DisplayName("동일한 역을 즐겨찾기로 등록할 경우 에러를 던진다")
    @Test
    void createFavorite_fail_source_target_same() {
        // when then
        assertThatThrownBy(() -> new Favorite(1L, GANGNAM_STATION, GANGNAM_STATION, pathFinder))
                .isExactlyInstanceOf(ShortPathSameStationException.class)
                .hasMessage("최단경로 시작역, 종착역이 동일할 수 없습니다.");
    }

}
