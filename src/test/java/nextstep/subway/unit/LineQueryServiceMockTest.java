package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import nextstep.subway.application.DefaultLineQueryService;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.domain.model.Line;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.service.LineQueryService;

public class LineQueryServiceMockTest {
    private LineRepository lineRepository;
    private LineQueryService lineQueryService;

    @BeforeEach
    void setUp() {
        lineRepository = mock(LineRepository.class);
        lineQueryService = new DefaultLineQueryService(lineRepository);
    }

    @Nested
    @DisplayName("노선 목록 조회 기능")
    class FindAllLines {

        @Test
        @DisplayName("지하철 노선 목록을 조회한다")
        void findAllLines() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            when(lineRepository.findAll()).thenReturn(Collections.singletonList(line));

            // when
            List<LineResponse> responses = lineQueryService.findAllLines();

            // then
            assertThat(responses).hasSize(1);
            assertThat(responses.get(0).getName()).isEqualTo("2호선");
        }

        @Test
        @DisplayName("지하철 노선이 존재하지 않을 때 목록 조회가 실패한다")
        void findAllLinesWhenNoLinesExist() {
            // given
            when(lineRepository.findAll()).thenReturn(Collections.emptyList());

            // when
            List<LineResponse> responses = lineQueryService.findAllLines();

            // then
            assertThat(responses).isEmpty();
        }
    }

    @Nested
    @DisplayName("노선 단일 조회 기능")
    class FindLineById {

        @Test
        @DisplayName("지하철 노선을 조회한다")
        void findLineById() {
            // given
            Line line = new Line(1L, "2호선", "bg-red-600");
            when(lineRepository.findById(1L)).thenReturn(Optional.of(line));

            // when
            LineResponse response = lineQueryService.findLineById(1L);

            // then
            assertThat(response.getName()).isEqualTo("2호선");
        }

        @Test
        @DisplayName("존재하지 않는 지하철 노선을 조회할 때 실패한다")
        void findNonExistentLineById() {
            // given
            when(lineRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when // then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> lineQueryService.findLineById(999L))
                .withMessage(DefaultLineQueryService.LINE_NOT_FOUND_MESSAGE);
        }
    }
}
