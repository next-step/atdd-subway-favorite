package nextstep.subway.unit;

import static nextstep.subway.application.DefaultLineCommandService.*;
import static nextstep.subway.domain.model.Sections.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import nextstep.subway.application.DefaultLineCommandService;
import nextstep.subway.application.DefaultSectionAdditionStrategyFactory;
import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.application.dto.SectionResponse;
import nextstep.subway.application.strategy.addition.AddSectionAfterLastDownStationStrategy;
import nextstep.subway.application.strategy.addition.AddSectionAfterUpStationStrategy;
import nextstep.subway.application.strategy.addition.AddSectionBeforeDownStationStrategy;
import nextstep.subway.domain.model.Line;
import nextstep.subway.domain.model.Section;
import nextstep.subway.domain.model.Station;
import nextstep.subway.domain.repository.InMemoryLineRepository;
import nextstep.subway.domain.repository.InMemoryStationRepository;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.domain.service.LineCommandService;
import nextstep.subway.domain.service.SectionAdditionStrategyFactory;

public class LineCommandServiceWithoutMockTest {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private LineCommandService lineCommandService;

    private SectionAdditionStrategyFactory sectionAdditionStrategyFactory;

    @BeforeEach
    void setUp() {
        lineRepository = new InMemoryLineRepository();
        stationRepository = new InMemoryStationRepository();
        sectionAdditionStrategyFactory = new DefaultSectionAdditionStrategyFactory(
            List.of(
                new AddSectionAfterLastDownStationStrategy(),
                new AddSectionAfterUpStationStrategy(),
                new AddSectionBeforeDownStationStrategy()
            )
        );
        lineCommandService = new DefaultLineCommandService(lineRepository, stationRepository, sectionAdditionStrategyFactory);
    }

    @Nested
    @DisplayName("노선 추가 기능")
    class AddLine {
        @Test
        @DisplayName("지하철 노선을 생성한다")
        void saveLine() {
            // given
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");

            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);

            String lineName = "2호선";
            String lineColor = "bg-red-600";

            LineRequest lineRequest = new LineRequest(lineName, lineColor, 1L, 2L, 10);

            // when
            LineResponse response = lineCommandService.saveLine(lineRequest);

            // then
            Optional<Line> savedLine = lineRepository.findById(response.getId());
            assertThat(savedLine).isPresent();
            assertThat(savedLine.get().getName()).isEqualTo(lineName);
            assertThat(savedLine.get().getColor()).isEqualTo(lineColor);
        }

        @Test
        @DisplayName("존재하지 않는 지하철 역으로 노선을 생성하려고 하면 실패한다")
        void saveLineWithNonExistentStation() {
            // given
            Station gangnamStation = new Station(1L, "강남역");

            stationRepository.save(gangnamStation);

            LineRequest lineRequest = new LineRequest("2호선", "bg-red-600", 1L, 2L, 10);

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.saveLine(lineRequest))
                .withMessage(STATION_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("노선 수정 기능")
    class UpdateLine {
        @Test
        @DisplayName("지하철 노선을 정상적으로 수정한다")
        void updateLineSuccessfully() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            lineRepository.save(line);

            LineRequest updateRequest = new LineRequest("신분당선", "bg-blue-600", 1L, 2L, 10);

            // when
            lineCommandService.updateLine(1L, updateRequest);

            // then
            Optional<Line> updatedLine = lineRepository.findById(1L);
            assertThat(updatedLine).isPresent();
            assertThat(updatedLine.get().getName()).isEqualTo("신분당선");
            assertThat(updatedLine.get().getColor()).isEqualTo("bg-blue-600");
        }

        @Test
        @DisplayName("존재하지 않는 지하철 노선을 수정할 때 실패한다")
        void updateNonExistentLine() {
            // given
            LineRequest updateRequest = new LineRequest("신분당선", "bg-blue-600", 1L, 2L, 10);

            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.updateLine(999L, updateRequest))
                .withMessage(LINE_NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("삭제된 지하철 노선을 수정할 때 실패한다")
        void updateDeletedLine() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            lineRepository.save(line);

            lineRepository.deleteById(1L);

            LineRequest updateRequest = new LineRequest("신분당선", "bg-blue-600", 1L, 2L, 10);

            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.updateLine(1L, updateRequest))
                .withMessage(LINE_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("노선 삭제 기능")
    class DeleteLine {
        @Test
        @DisplayName("지하철 노선을 정상적으로 삭제한다")
        void deleteLineSuccessfully() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            lineRepository.save(line);

            // when
            lineCommandService.deleteLineById(1L);

            // then
            assertThat(lineRepository.findById(1L)).isNotPresent();
        }

        @Test
        @DisplayName("존재하지 않는 지하철 노선을 삭제할 때 실패한다")
        void deleteNonExistentLine() {
            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.deleteLineById(999L))
                .withMessage(LINE_NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("이미 삭제된 지하철 노선을 다시 삭제할 때 실패한다")
        void deleteAlreadyDeletedLine() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            lineRepository.save(line);
            lineRepository.deleteById(1L);

            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.deleteLineById(1L))
                .withMessage(LINE_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("구간 추가 기능")
    class AddSection {
        @Test
        @DisplayName("지하철 노선에 구간을 정상적으로 등록한다")
        void addSectionSuccessfully() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");
            Station seolleungStation = new Station(3L, "선릉역");

            Section initialSection = new Section(line, gangnamStation, yeoksamStation, 10);
            line.addSection(initialSection);

            lineRepository.save(line);
            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);
            stationRepository.save(seolleungStation);

            SectionRequest sectionRequest = new SectionRequest(2L, 3L, 8);

            // when
            SectionResponse response = lineCommandService.addSection(1L, sectionRequest);

            // then
            assertThat(response.getUpStationId()).isEqualTo(2L);
            assertThat(response.getDownStationId()).isEqualTo(3L);
            assertThat(response.getDistance()).isEqualTo(8);

            Line updatedLine = lineRepository.findById(1L).orElseThrow();
            assertThat(updatedLine.getUnmodifiableSections()).hasSize(2);
        }

        @Test
        @DisplayName("상행역 기준으로 신규 구간을 추가한다")
        void addSectionByUpStation() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");
            Station seolleungStation = new Station(3L, "선릉역");

            Section initialSection = new Section(line, gangnamStation, seolleungStation, 10);
            line.addSection(initialSection);

            lineRepository.save(line);
            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);
            stationRepository.save(seolleungStation);

            SectionRequest sectionRequest = new SectionRequest(1L, 2L, 5);

            // when
            SectionResponse response = lineCommandService.addSection(1L, sectionRequest);

            // then
            assertThat(response.getUpStationId()).isEqualTo(1L);
            assertThat(response.getDownStationId()).isEqualTo(2L);
            assertThat(response.getDistance()).isEqualTo(5);

            Line updatedLine = lineRepository.findById(1L).orElseThrow();
            assertThat(updatedLine.getUnmodifiableSections()).hasSize(2);
        }

        @Test
        @DisplayName("존재하지 않는 상행역으로 구간을 추가하려고 하면 실패한다")
        void addSectionWithNonExistentStation() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");

            Section initialSection = new Section(line, gangnamStation, yeoksamStation, 10);
            line.addSection(initialSection);
            lineRepository.save(line);
            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);

            SectionRequest sectionRequest = new SectionRequest(999L, 1L, 5);

            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.addSection(1L, sectionRequest))
                .withMessage(STATION_NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("상행역 기준으로 구간 추가 시 기존 구간 거리보다 큰 거리값을 요청하면 실패한다")
        void addSectionWithShorterDistanceThanExistingUpStation() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");
            Station seolleungStation = new Station(3L, "선릉역");

            Section initialSection = new Section(line, gangnamStation, seolleungStation, 10);
            line.addSection(initialSection);

            lineRepository.save(line);
            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);
            stationRepository.save(seolleungStation);

            SectionRequest sectionRequest = new SectionRequest(1L, 2L, 15);

            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.addSection(1L, sectionRequest))
                .withMessageContaining(CANNOT_ADD_SECTION_MESSAGE);
        }

        @Test
        @DisplayName("하행역 기준으로 신규 구간을 추가한다")
        void addSectionByDownStation() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");
            Station seolleungStation = new Station(3L, "선릉역");

            Section initialSection = new Section(line, gangnamStation, seolleungStation, 10);
            line.addSection(initialSection);

            lineRepository.save(line);
            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);
            stationRepository.save(seolleungStation);

            SectionRequest sectionRequest = new SectionRequest(2L, 3L, 8);

            // when
            SectionResponse response = lineCommandService.addSection(1L, sectionRequest);

            // then
            assertThat(response.getUpStationId()).isEqualTo(2L);
            assertThat(response.getDownStationId()).isEqualTo(3L);
            assertThat(response.getDistance()).isEqualTo(8);

            Line updatedLine = lineRepository.findById(1L).orElseThrow();
            assertThat(updatedLine.getUnmodifiableSections()).hasSize(2);
        }

        @Test
        @DisplayName("존재하지 않는 하행역으로 구간을 추가하려고 하면 실패한다")
        void addSectionWithNonExistentDownStation() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");

            Section initialSection = new Section(line, gangnamStation, yeoksamStation, 10);
            line.addSection(initialSection);
            lineRepository.save(line);
            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);

            SectionRequest sectionRequest = new SectionRequest(1L, 999L, 5);

            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.addSection(1L, sectionRequest))
                .withMessage(STATION_NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("하행역 기준으로 구간 추가 시 기존 구간 거리보다 큰 거리값을 요청하면 실패한다")
        void addSectionWithShorterDistanceThanExistingDownStation() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");
            Station seolleungStation = new Station(3L, "선릉역");

            Section initialSection = new Section(line, gangnamStation, seolleungStation, 10);
            line.addSection(initialSection);

            lineRepository.save(line);
            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);
            stationRepository.save(seolleungStation);

            SectionRequest sectionRequest = new SectionRequest(2L, 3L, 15);

            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.addSection(1L, sectionRequest))
                .withMessageContaining(CANNOT_ADD_SECTION_MESSAGE);
        }

        @Test
        @DisplayName("구간 추가 후 마지막 구간에 다시 구간을 추가한다")
        void addSectionToEndOfLine() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            Station gangnamStation = new Station(1L, "강남역");
            Station yeoksamStation = new Station(2L, "역삼역");
            Station seolleungStation = new Station(3L, "선릉역");
            Station samsungStation = new Station(4L, "삼성역");

            Section initialSection = new Section(line, gangnamStation, yeoksamStation, 10);
            line.addSection(initialSection);
            Section additionalSection = new Section(line, yeoksamStation, seolleungStation, 8);
            line.addSection(additionalSection);

            lineRepository.save(line);
            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);
            stationRepository.save(seolleungStation);
            stationRepository.save(samsungStation);

            SectionRequest sectionRequest = new SectionRequest(3L, 4L, 7);

            // when
            SectionResponse response = lineCommandService.addSection(1L, sectionRequest);

            // then
            assertThat(response.getUpStationId()).isEqualTo(3L);
            assertThat(response.getDownStationId()).isEqualTo(4L);
            assertThat(response.getDistance()).isEqualTo(7);

            Line updatedLine = lineRepository.findById(1L).orElseThrow();
            assertThat(updatedLine.getUnmodifiableSections()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("구간 삭제 기능")
    class DeleteSection {
        private Line line;

        private Station gangnamStation;

        private Station yeoksamStation;

        private Station seolleungStation;

        private Section initialSection;

        private Section additionalSection;

        @BeforeEach
        void setUp() {
            saveInitialLine();
        }

        private void saveInitialLine() {
            gangnamStation = new Station(1L, "강남역");
            yeoksamStation = new Station(2L, "역삼역");
            seolleungStation = new Station(3L, "선릉역");

            stationRepository.save(gangnamStation);
            stationRepository.save(yeoksamStation);
            stationRepository.save(seolleungStation);

            line = new Line(1L, "2호선", "bg-red-600");
            initialSection = new Section(line, gangnamStation, yeoksamStation, 10);
            additionalSection = new Section(line, yeoksamStation, seolleungStation, 8);

            line.addSection(initialSection);
            line.addSection(additionalSection);

            lineRepository.save(line);
        }

        @Test
        @DisplayName("첫 구간의 상행역을 제거하려고 하면 구간이 제거된다")
        void removeFirstSectionSuccessfully() {
            // when
            lineCommandService.removeSection(1L, 1L);

            // then
            Line updatedLine = lineRepository.findById(1L).orElseThrow();

            List<Section> orderedSections = updatedLine.getOrderedUnmodifiableSections();

            assertThat(orderedSections).hasSize(1);
            assertThat(orderedSections).containsExactly(additionalSection);
        }

        @Test
        @DisplayName("마지막 구간의 하행역을 제거하려고 하면 구간이 제거된다")
        void removeLastSectionSuccessfully() {
            // when
            lineCommandService.removeSection(1L, 3L);

            // then
            Line updatedLine = lineRepository.findById(1L).orElseThrow();

            List<Section> orderedSections = updatedLine.getOrderedUnmodifiableSections();

            assertThat(orderedSections).hasSize(1);
            assertThat(orderedSections).containsExactly(initialSection);
        }

        @Test
        @DisplayName("중간에 존재하는 역을 제거하려고 하면 구간이 제거된다")
        void removeMiddleSectionSuccessfully() {
            // when
            lineCommandService.removeSection(1L, 2L);

            // then
            Line updatedLine = lineRepository.findById(1L).orElseThrow();

            List<Section> orderedSections = updatedLine.getOrderedUnmodifiableSections();

            assertThat(orderedSections).containsExactly(initialSection);
            assertThat(orderedSections).doesNotContain(additionalSection);
            assertThat(initialSection.getUpStation()).isEqualTo(gangnamStation);
            assertThat(initialSection.getDownStation()).isEqualTo(seolleungStation);
        }

        @Test
        @DisplayName("구간 삭제 시 없는 상행역을 요청하면 구간 삭제가 실패한다")
        void removeSectionWithNonExistentUpStation() {
            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.removeSection(1L, 999L))
                .withMessage(STATION_NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("구간 삭제 시 없는 하행역을 요청하면 구간 삭제가 실패한다")
        void removeSectionWithNonExistentDownStation() {
            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineCommandService.removeSection(1L, 1000L))
                .withMessage(STATION_NOT_FOUND_MESSAGE);
        }

    }
}



