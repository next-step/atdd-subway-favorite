package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineTest {
    @Test
    void getStations() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line line = new Line("2호선", "green", 강남역, 역삼역, 10);

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations.size()).isEqualTo(2);
    }

    @Test
    void addSection() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green", 강남역, 역삼역, 10);

        line.addSection(역삼역, 삼성역, 5);

        assertThat(line.getSections().size()).isEqualTo(2);
    }

    @DisplayName("목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green", 강남역, 역삼역, 10);

        line.addSection(강남역, 삼성역, 5);

        assertThat(line.getSections().size()).isEqualTo(2);
    }

    @DisplayName("이미 존재하는 역 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line line = new Line("2호선", "green", 강남역, 역삼역, 10);

        assertThatThrownBy(() -> line.addSection(역삼역, 강남역, 5)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void removeSection() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green", 강남역, 역삼역, 10);
        line.addSection(역삼역, 삼성역, 5);

        line.removeSection(삼성역);

        assertThat(line.getSections().size()).isEqualTo(1);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line line = new Line("2호선", "green", 강남역, 역삼역, 10);

        assertThatThrownBy(() -> line.removeSection(역삼역)).isInstanceOf(RuntimeException.class);
    }
}
