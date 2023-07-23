package nextstep.api.subway.acceptance.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import static nextstep.api.subway.acceptance.station.StationSteps.지하철역을_생성한다;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import nextstep.api.subway.acceptance.AcceptanceTest;
import nextstep.api.subway.applicaion.line.dto.request.LineCreateRequest;
import nextstep.api.subway.applicaion.line.dto.request.LineUpdateRequest;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철노선을 생성한다")
    @Test
    void createLine() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역을_생성한다("강남역").getId(),
                지하철역을_생성한다("양재역").getId(),
                10
        );

        // when
        LineSteps.지하철노선을_생성한다(request);

        // then
        Assertions.assertThat(LineSteps.모든_지하철노선을_조회한다()).hasSize(1);
    }

    @DisplayName("모든 지하철노선 목록을 조회한다")
    @Test
    void showLines() {
        final var requests = List.of(
                new LineCreateRequest(
                        "신분당선",
                        "bg-red-600",
                        지하철역을_생성한다("강남역").getId(),
                        지하철역을_생성한다("양재역").getId(),
                        10
                ),
                new LineCreateRequest(
                        "2호선",
                        "bg-green-600",
                        지하철역을_생성한다("강남역").getId(),
                        지하철역을_생성한다("역삼역").getId(),
                        10
                )
        );

        // given
        LineSteps.지하철노선을_생성한다(requests);

        // when
        final var responses = LineSteps.모든_지하철노선을_조회한다();

        // then
        assertThat(responses).hasSize(requests.size());
    }

    @DisplayName("지하철노선을 조회한다")
    @Test
    void findLine() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역을_생성한다("강남역").getId(),
                지하철역을_생성한다("양재역").getId(),
                10
        );

        // given
        final Long lineId = LineSteps.지하철노선을_생성한다(request).getId();

        // when
        final var response = LineSteps.지하철노선을_조회한다(lineId);

        // then
        assertThat(response.getId()).isPositive();
    }

    @DisplayName("지하철노선을 수정한다")
    @Nested
    class UpdateLineTest {

        @Nested
        class Success {

            @Test
            void 등록되어있는_지하철노선을_수정한다() {
                final var createRequest = new LineCreateRequest(
                        "신분당선",
                        "bg-red-600",
                        지하철역을_생성한다("강남역").getId(),
                        지하철역을_생성한다("양재역").getId(),
                        10
                );
                final var updateRequest = new LineUpdateRequest("2호선", "bg-blue-123");

                // given
                final Long lineId = LineSteps.지하철노선을_생성한다(createRequest).getId();

                // when
                LineSteps.지하철노선을_수정한다(lineId, updateRequest);

                // then
                final var response = LineSteps.지하철노선을_조회한다(lineId);
                assertAll(
                        () -> assertThat(response.getName()).isEqualTo(updateRequest.getName()),
                        () -> assertThat(response.getColor()).isEqualTo(updateRequest.getColor())
                );
            }
        }

        @Nested
        class Fail {

            @DisplayName("지하철노선을 수정한다")
            @Test
            void 등록되지_않은_지하철노선을_수정한다() {
                LineSteps.지하철노선_수정에_실패한다(0L, new LineUpdateRequest("2호선", "bg-blue-123"));
            }
        }
    }


    @DisplayName("지하철노선을 삭제한다")
    @Test
    void deleteLine() {
        final var request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역을_생성한다("강남역").getId(),
                지하철역을_생성한다("양재역").getId(),
                10
        );

        // given
        final Long lineId = LineSteps.지하철노선을_생성한다(request).getId();

        // when
        LineSteps.지하철노선을_제거한다(lineId);

        // then
        Assertions.assertThat(LineSteps.모든_지하철노선을_조회한다()).isEmpty();
    }
}
