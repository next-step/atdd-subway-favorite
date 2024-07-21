package nextstep.subway.unit.command;

import autoparams.AutoSource;
import nextstep.subway.domain.command.LineCommand;
import nextstep.subway.domain.command.LineCommander;
import nextstep.subway.domain.entity.line.Line;
import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.setup.BaseTestSetup;
import nextstep.subway.unit.testing.LineDbUtil;
import nextstep.subway.unit.testing.StationDbUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineCommanderTest extends BaseTestSetup {

    @Autowired
    private LineCommander sut;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationDbUtil stationDbUtil;

    @Autowired
    private LineDbUtil lineDbUtil;

    @Nested
    @DisplayName("createLine")
    class CreateLineTest {
        public void changeUpDownStationField(LineCommand.CreateLine command, Long upStationId, Long downStationId) {
            try {
                Field upField = command.getClass().getDeclaredField("upStationId");
                upField.setAccessible(true);
                upField.set(command, upStationId);

                Field downField = command.getClass().getDeclaredField("downStationId");
                downField.setAccessible(true);
                downField.set(command, downStationId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_if_not_found_upStation(LineCommand.CreateLine command) {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역");
            changeUpDownStationField(command, 123213L, stations.get(1).getId());

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.createLine(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_if_not_found_downStation(LineCommand.CreateLine command) {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역");
            changeUpDownStationField(command, stations.get(0).getId(), 123213L);

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.createLine(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        @ParameterizedTest
        @AutoSource
        public void sut_creates_line(LineCommand.CreateLine command) {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역");
            changeUpDownStationField(command, stations.get(0).getId(), stations.get(1).getId());

            // when
            Long id = sut.createLine(command);

            // then
            transactionTemplate.execute(status -> {
                Line actual = lineRepository.findByIdOrThrow(id);
                assertAll("assert created line",
                        () -> assertThat(actual.getName()).isEqualTo(command.getName()),
                        () -> assertThat(actual.getColor()).isEqualTo(command.getColor()),

                        // section
                        () -> assertThat(actual.getSections().size()).isEqualTo(1),
                        () -> assertThat(actual.getSections().getLastSection().getUpStationId()).isEqualTo(command.getUpStationId()),
                        () -> assertThat(actual.getSections().getLastSection().getDownStationId()).isEqualTo(command.getDownStationId()),
                        () -> assertThat(actual.getSections().getLastSection().getDistance()).isEqualTo(command.getDistance())
                );
                return null;
            });
        }
    }

    @Nested
    @DisplayName("updateLine")
    class UpdateLineTest {
        @ParameterizedTest
        @AutoSource
        public void sut_updates_line(String lineName, String color) {
            // given
            Line line = lineDbUtil.insertLine(111L, 211L);
            LineCommand.UpdateLine command = new LineCommand.UpdateLine(line.getId(), lineName, color);

            // when
            sut.updateLine(command);

            // then
            Line actual = lineRepository.findByIdOrThrow(command.getId());
            assertThat(actual.getName()).isEqualTo(command.getName());
            assertThat(actual.getColor()).isEqualTo(command.getColor());
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_if_not_found_line(LineCommand.UpdateLine command) {
            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.updateLine(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_LINE);
        }
    }

    @Nested
    @DisplayName("deleteLineById")
    class DeleteLineTest {
        @ParameterizedTest
        @AutoSource
        public void sut_deletes_line() {
            // given
            Line line = lineDbUtil.insertLine(111L, 211L);

            // when
            sut.deleteLineById(line.getId());

            // then
            Optional<Line> actual = lineRepository.findById(line.getId());
            assertThat(actual).isEmpty();
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_if_not_found_line(Long id) {
            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.deleteLineById(id));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_LINE);
        }
    }

    @Nested
    @DisplayName("addSection")
    class AddSectionTest {
        @Test
        public void sut_throws_if_not_found_line() {
            // given
            List<Station> upDownStation = stationDbUtil.insertStations("삼성역", "잠실역");
            LineCommand.AddSection command = new LineCommand.AddSection(
                    1728321378313L,
                    upDownStation.get(0).getId(),
                    upDownStation.get(1).getId(),
                    20L
            );

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.addSection(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_LINE);
        }

        @Test
        public void sut_throws_if_not_found_upStation() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역");
            Line line = lineDbUtil.insertLine(stations.get(0).getId(), stations.get(1).getId());
            LineCommand.AddSection command = new LineCommand.AddSection(
                    line.getId(),
                    17238123L,
                    stations.get(1).getId(),
                    20L
            );

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.addSection(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        @Test
        public void sut_throws_if_not_found_downStation() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역");
            Line line = lineDbUtil.insertLine(stations.get(0).getId(), stations.get(1).getId());
            LineCommand.AddSection command = new LineCommand.AddSection(
                    line.getId(),
                    stations.get(0).getId(),
                    12783L,
                    20L
            );

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.addSection(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        @Test
        public void sut_add_section_first() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역", "선릉역", "강남역");
            Line line = lineDbUtil.insertLine(stations.get(1).getId(), stations.get(2).getId());
            LineCommand.AddSection command = new LineCommand.AddSection(
                    line.getId(),
                    stations.get(0).getId(),
                    stations.get(1).getId(),
                    20L
            );

            // when
            sut.addSection(command);

            // then
            transactionTemplate.execute(status -> {
                Line actual = lineRepository.findByIdOrThrow(line.getId());
                assertAll("assert section added",
                        () -> assertThat(actual.getSections().size()).isEqualTo(2),
                        () -> assertThat(actual.getSections().getFirstSection().getUpStationId()).isEqualTo(command.getUpStationId()),
                        () -> assertThat(actual.getSections().getFirstSection().getDownStationId()).isEqualTo(command.getDownStationId()),
                        () -> assertThat(actual.getSections().getFirstSection().getDistance()).isEqualTo(command.getDistance())
                );
                return null;
            });
        }

        @Test
        public void sut_add_section_last() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역", "선릉역", "강남역");
            Line line = lineDbUtil.insertLine(stations.get(0).getId(), stations.get(1).getId());
            LineCommand.AddSection command = new LineCommand.AddSection(
                    line.getId(),
                    stations.get(1).getId(),
                    stations.get(2).getId(),
                    20L
            );

            // when
            sut.addSection(command);

            // then
            transactionTemplate.execute(status -> {
                Line actual = lineRepository.findByIdOrThrow(line.getId());
                assertAll("assert section added",
                        () -> assertThat(actual.getSections().size()).isEqualTo(2),
                        () -> assertThat(actual.getSections().getLastSection().getUpStationId()).isEqualTo(command.getUpStationId()),
                        () -> assertThat(actual.getSections().getLastSection().getDownStationId()).isEqualTo(command.getDownStationId()),
                        () -> assertThat(actual.getSections().getLastSection().getDistance()).isEqualTo(command.getDistance())
                );
                return null;
            });
        }

        @Test
        public void sut_add_section_middle() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역", "선릉역", "강남역");
            Line line = lineDbUtil.insertLine(stations.get(0).getId(), stations.get(2).getId());
            LineCommand.AddSection command = new LineCommand.AddSection(
                    line.getId(),
                    stations.get(0).getId(),
                    stations.get(1).getId(),
                    8L
            );

            // when
            sut.addSection(command);

            // then
            transactionTemplate.execute(status -> {
                Line actual = lineRepository.findByIdOrThrow(line.getId());
                assertAll("assert section added",
                        () -> assertThat(actual.getSections().size()).isEqualTo(2),

                        () -> assertThat(actual.getSections().getFirstSection().getUpStationId()).isEqualTo(stations.get(0).getId()),
                        () -> assertThat(actual.getSections().getFirstSection().getDownStationId()).isEqualTo(stations.get(1).getId()),
                        () -> assertThat(actual.getSections().getFirstSection().getDistance()).isEqualTo(8L),

                        () -> assertThat(actual.getSections().getLastSection().getUpStationId()).isEqualTo(stations.get(1).getId()),
                        () -> assertThat(actual.getSections().getLastSection().getDownStationId()).isEqualTo(stations.get(2).getId()),
                        () -> assertThat(actual.getSections().getLastSection().getDistance()).isEqualTo(2L)
                );
                return null;
            });
        }
    }

    @Nested
    @DisplayName("deleteSection")
    class DeleteSectionTest {
        @Test
        public void sut_throws_if_not_found_line() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역", "선릉역", "강남역");

            LineCommand.DeleteSection command = new LineCommand.DeleteSection(1728321378313L, stations.get(0).getId());

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.deleteSection(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_LINE);
        }

        @Test
        public void sut_throws_if_section_size_one() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역", "선릉역", "강남역");
            Line line = lineDbUtil.insertLine(stations.get(0).getId(), stations.get(1).getId());

            LineCommand.DeleteSection command = new LineCommand.DeleteSection(line.getId(), stations.get(1).getId());

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.deleteSection(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.INVALID_SECTION_SIZE);
        }

        @Test
        public void sut_throws_if_station_not_equal_to_last_line_downStation() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역", "선릉역", "강남역");
            Line line = lineDbUtil.insertLine(stations.get(0).getId(), stations.get(1).getId());
            lineDbUtil.insertSection(line, stations.get(1).getId(), stations.get(2).getId(), 20L);

            LineCommand.DeleteSection command = new LineCommand.DeleteSection(line.getId(), stations.get(1).getId());

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.deleteSection(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.INVALID_STATION);
        }

        @Test
        public void sut_delete_section() {
            // given
            List<Station> stations = stationDbUtil.insertStations("삼성역", "잠실역", "선릉역", "강남역");
            Line line = lineDbUtil.insertLine(stations.get(0).getId(), stations.get(1).getId());
            lineDbUtil.insertSection(line, stations.get(1).getId(), stations.get(2).getId(), 20L);

            LineCommand.DeleteSection command = new LineCommand.DeleteSection(line.getId(), stations.get(2).getId());

            // when
            sut.deleteSection(command);

            // then
            transactionTemplate.execute(status -> {
                Line actual = lineRepository.findByIdOrThrow(line.getId());
                assertThat(actual.getSections().size()).isEqualTo(1);
                assertThat(actual.getSections().stream()
                        .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                        .collect(Collectors.toList())
                ).doesNotContain(stations.get(2).getId());
                return null;
            });
        }
    }
}
