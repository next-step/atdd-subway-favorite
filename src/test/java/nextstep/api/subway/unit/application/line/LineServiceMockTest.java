package nextstep.api.subway.unit.application.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.api.subway.applicaion.line.LineService;
import nextstep.api.subway.applicaion.line.dto.request.SectionRequest;
import nextstep.api.subway.domain.line.LineRepository;
import nextstep.api.subway.domain.line.exception.NoSuchLineException;
import nextstep.api.subway.domain.station.StationRepository;
import nextstep.api.subway.domain.station.exception.NoSuchStationException;
import nextstep.api.subway.unit.LineFixture;
import nextstep.api.subway.unit.StationFixture;

@ExtendWith(MockitoExtension.class)
class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private LineService lineService;

    @DisplayName("구간을 추가한다")
    @Nested
    class AppendSectionTest {

        @Nested
        class Success {

            @Test
            void 구간을_추가한다() {
                final var lineId = 1L;
                final var request = new SectionRequest(1L, 2L, 10);
                final var line = LineFixture.makeLine(StationFixture.강남역, StationFixture.역삼역);

                // given
                given(lineRepository.getById(lineId)).willReturn(line);
                given(stationRepository.getById(request.getUpStationId())).willReturn(StationFixture.역삼역);
                given(stationRepository.getById(request.getDownStationId())).willReturn(StationFixture.선릉역);

                // when
                lineService.appendSection(1L, new SectionRequest(1L, 2L, 10));

                // then
                final var actual = line.getStations();
                assertThat(actual).contains(StationFixture.강남역, StationFixture.역삼역, StationFixture.선릉역);
            }
        }

        @Nested
        class Fail {
            private final Long lineId = 1L;
            private final SectionRequest request = new SectionRequest(1L, 2L, 10);

            @Test
            void 노선이_존재하지_않는_경우() {
                // given
                given(lineRepository.getById(lineId)).willThrow(new NoSuchLineException(""));

                // when
                assertThatThrownBy(() -> lineService.appendSection(lineId, request))
                        .isInstanceOf(NoSuchLineException.class);
            }

            @Test
            void 상행역이_존재하지_않는_경우() {
                // given
                given(lineRepository.getById(lineId)).willReturn(
                        LineFixture.makeLine(StationFixture.강남역, StationFixture.역삼역));
                given(stationRepository.getById(request.getUpStationId())).willThrow(new NoSuchStationException(""));

                // when
                assertThatThrownBy(() -> lineService.appendSection(lineId, request))
                        .isInstanceOf(NoSuchStationException.class);
            }

            @Test
            void 하행역이_존재하지_않는_경우() {
                // given
                given(lineRepository.getById(lineId)).willReturn(
                        LineFixture.makeLine(StationFixture.강남역, StationFixture.역삼역));
                given(stationRepository.getById(request.getUpStationId())).willReturn(StationFixture.역삼역);
                given(stationRepository.getById(request.getUpStationId())).willThrow(new NoSuchStationException(""));

                // when
                assertThatThrownBy(() -> lineService.appendSection(lineId, request))
                        .isInstanceOf(NoSuchStationException.class);
            }
        }
    }

    @DisplayName("구간을 삭제한다")
    @Nested
    class RemoveSectionTest {
        private final Long lineId = 1L;
        private final Long stationId = 1L;

        @Nested
        class Success {

            @Test
            void 구간을_삭제한다() {
                // given
                final var line = LineFixture.makeLine(StationFixture.강남역, StationFixture.역삼역, StationFixture.선릉역);
                given(lineRepository.getById(lineId)).willReturn(line);
                given(stationRepository.getById(stationId)).willReturn(StationFixture.선릉역);

                // when
                lineService.removeSection(lineId, stationId);

                // then
                final var actual = line.getStations();
                assertThat(actual).contains(StationFixture.강남역, StationFixture.역삼역);
            }
        }

        @Nested
        class Fail {

            @Test
            void 노선이_존재하지_않는_경우() {
                // given
                given(lineRepository.getById(lineId)).willThrow(new NoSuchLineException(""));

                // then
                assertThatThrownBy(() -> lineService.removeSection(lineId, stationId))
                        .isInstanceOf(NoSuchLineException.class);
            }

            @Test
            void 역이_존재하지_않는_경우() {
                // given
                given(lineRepository.getById(lineId)).willReturn(
                        LineFixture.makeLine(StationFixture.강남역, StationFixture.역삼역, StationFixture.선릉역));
                given(stationRepository.getById(stationId)).willThrow(new NoSuchStationException(""));

                // then
                assertThatThrownBy(() -> lineService.removeSection(lineId, stationId))
                        .isInstanceOf(NoSuchStationException.class);
            }
        }
    }
}
