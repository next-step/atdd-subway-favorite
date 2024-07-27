package nextstep.subway.unit.query;

import nextstep.subway.domain.entity.line.Line;
import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;
import nextstep.subway.domain.query.PathReader;
import nextstep.subway.domain.view.PathView;
import nextstep.subway.setup.BaseTestSetup;
import nextstep.subway.unit.testing.LineDbUtil;
import nextstep.subway.unit.testing.StationDbUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class PathReaderTest extends BaseTestSetup {

    @Autowired
    private PathReader sut;

    @Autowired
    private StationDbUtil stationDbUtil;

    @Autowired
    private LineDbUtil lineDbUtil;

    @Test
    public void sut_throws_error_if_not_found_source_station() {
        // given
        Long source = 22L;
        Station targetStation = stationDbUtil.insertStations("선릉역").get(0);

        // when
        SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.findShortestPath(source, targetStation.getId()));

        // then
        assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
    }

    @Test
    public void sut_throws_error_if_not_found_target_station() {
        // given
        Station targetStation = stationDbUtil.insertStations("선릉역").get(0);
        Long target = 22L;

        // when
        SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.findShortestPath(targetStation.getId(), target));

        // then
        assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
    }

    @Test
    public void sut_returns_shortest_path() {
        // given
        List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역", "선릉역", "삼성역");
        Line line = lineDbUtil.insertLine(stations.get(0).getId(), stations.get(1).getId());
        lineDbUtil.insertSection(line, stations.get(1).getId(), stations.get(2).getId(), 10L);
        lineDbUtil.insertSection(line, stations.get(2).getId(), stations.get(3).getId(), 10L);

        // when
        PathView.Main actual = sut.findShortestPath(stations.get(0).getId(), stations.get(2).getId());

        // then
        assertThat(actual.getStations().size()).isEqualTo(3);
        assertThat(actual.getDistance()).isEqualTo(20L);
    }
}

