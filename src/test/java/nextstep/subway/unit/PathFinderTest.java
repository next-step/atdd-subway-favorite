package nextstep.subway.unit;

import nextstep.exception.notfoundexception.StationNotFoundException;
import nextstep.exception.pathexception.PathException;
import nextstep.subway.applicaion.PathFinder;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {

    private String 출발역명 = "출발역";
    private String 중간역명 = "중간역";
    private String 도착역명 = "도착역";
    private Station 출발역;
    private Station 중간역;
    private Station 도착역;
    private Line 노선;
    private Section 출발역_중간역;
    private Section 중간역_도착역;
    private Section 출발역_도착역;
    private List<Section> sections;

    @BeforeEach
    public void setUp() {
        출발역 = new Station(1L, 출발역명);
        중간역 = new Station(2L, 중간역명);
        도착역 = new Station(3L, 도착역명);
        노선 = new Line();
        출발역_중간역 = new Section(노선, 출발역, 중간역, 5);
        중간역_도착역 = new Section(노선, 중간역, 도착역, 10);
        출발역_도착역 = new Section(노선, 출발역, 도착역, 16);
        sections = new ArrayList<>(List.of(출발역_중간역, 중간역_도착역, 출발역_도착역));
    }

    @Test
    void find() {
        //when
        PathResponse pathResponse = PathFinder.find(1L, 3L, sections);

        //then
        List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName).collect(Collectors.toList());
        assertThat(stationNames).containsExactly(출발역명, 중간역명, 도착역명);
        assertThat(pathResponse.getDistance()).isEqualTo(15);
    }

    @Test
    void 출발역과_도착역이_같은_경우() {
        //then
        assertThatThrownBy(() -> PathFinder.find(1L, 1L, sections))
                .isInstanceOf(PathException.class)
                .hasMessage("출발역과 도착역이 같습니다.");
    }

    @Test
    void 존재하지_않는_출발역인_경우() {
        //then
        assertThatThrownBy(() -> PathFinder.find(100_000_000L, 1L, sections))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    void 연결되어_있지_않는_출발역과_도착역인_경우() {
        //when
        Station 동떨어진역 = new Station(4L, "동떨어진역");
        Station 더동떨어진역 = new Station(5L, "더동떨어진역");
        Section 동떨어진구간 = new Section(new Line(), 동떨어진역, 더동떨어진역, 10);
        sections.add(동떨어진구간);

        //then
        assertThatThrownBy(() -> PathFinder.find(1L, 4L, sections))
                .isInstanceOf(PathException.class)
                .hasMessage("출발역과 도착역이 연결되어 있지 않습니다.");
    }
}
