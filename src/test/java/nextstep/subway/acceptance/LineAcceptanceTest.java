package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;

@DisplayName("지하철 노선 관련 기능 인수테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LineAcceptanceTest {
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void testCreateLine() {
        // given
        createStation("강남역");
        createStation("역삼역");
        LineRequest request = new LineRequest("2호선", "bg-red-600", 1L, 2L, 10);

        // when
        ExtractableResponse<Response> response = createLine(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<LineResponse> lines = getAllLines().jsonPath().getList(".", LineResponse.class);
        assertThat(lines).hasSize(1);
        assertThat(lines.get(0).getName()).isEqualTo("2호선");
    }

    @DisplayName("존재하지 않는 역 ID로 지하철 노선을 생성할 때 실패한다")
    @Test
    void testCreateLineWithNonExistentStationId() {
        // given
        createStation("강남역");
        LineRequest request = new LineRequest("신분당선", "bg-red-600", 999L, 2L, 10);

        // when
        ExtractableResponse<Response> response = createLine(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void testGetLines() {
        // given
        createStation("강남역");
        createStation("역삼역");
        createLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));

        createStation("수서역");
        createStation("가천대역");
        createLine(new LineRequest("분당선", "bg-green-600", 3L, 4L, 20));

        // when
        ExtractableResponse<Response> response = getAllLines();

        // then
        List<LineResponse> lines = response.jsonPath().getList(".", LineResponse.class);
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0).getName()).isEqualTo("2호선");
        assertThat(lines.get(1).getName()).isEqualTo("분당선");
    }

    @DisplayName("지하철 노선을 조회한다")
    @Test
    void testGetLine() {
        // given
        createStation("강남역");
        createStation("역삼역");
        ExtractableResponse<Response> createResponse = createLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        Long lineId = createResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = getLine(lineId);

        // then
        LineResponse line = response.jsonPath().getObject(".", LineResponse.class);
        assertThat(line.getName()).isEqualTo("2호선");
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회할 때 실패한다")
    @Test
    void testGetNonExistentLine() {
        // when
        ExtractableResponse<Response> response = getLine(999L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("지하철 노선을 수정한다")
    @Test
    void testUpdateLine() {
        // given
        createStation("강남역");
        createStation("역삼역");
        ExtractableResponse<Response> createResponse = createLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        Long lineId = createResponse.jsonPath().getLong("id");

        // when
        LineRequest updateRequest = new LineRequest("신분당선", "bg-blue-600", 1L, 2L, 10);
        ExtractableResponse<Response> response = updateLine(lineId, updateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        LineResponse updatedLine = getLine(lineId).jsonPath().getObject(".", LineResponse.class);
        assertThat(updatedLine.getName()).isEqualTo("신분당선");
    }

    @DisplayName("존재하지 않는 지하철 노선을 수정할 때 실패한다")
    @Test
    void testUpdateNonExistentLine() {
        // when
        LineRequest updateRequest = new LineRequest("신분당선", "bg-blue-600", 1L, 2L, 10);
        ExtractableResponse<Response> response = updateLine(999L, updateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("삭제된 지하철 노선을 수정할 때 실패한다")
    @Test
    void testUpdateDeletedLine() {
        // given
        createStation("강남역");
        createStation("역삼역");
        ExtractableResponse<Response> createResponse = createLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        Long lineId = createResponse.jsonPath().getLong("id");
        deleteLine(lineId);

        // when
        LineRequest updateRequest = new LineRequest("신분당선", "bg-blue-600", 1L, 2L, 10);
        ExtractableResponse<Response> response = updateLine(lineId, updateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void testDeleteLine() {
        // given
        createStation("강남역");
        createStation("역삼역");
        ExtractableResponse<Response> createResponse = createLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        Long lineId = createResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = deleteLine(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<LineResponse> lines = getAllLines().jsonPath().getList(".", LineResponse.class);
        assertThat(lines).isEmpty();
    }

    @DisplayName("존재하지 않는 지하철 노선을 삭제할 때 실패한다")
    @Test
    void testDeleteNonExistentLine() {
        // when
        ExtractableResponse<Response> response = deleteLine(999L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("이미 삭제된 지하철 노선을 다시 삭제할 때 실패한다")
    @Test
    void testDeleteAlreadyDeletedLine() {
        // given
        createStation("강남역");
        createStation("역삼역");
        ExtractableResponse<Response> createResponse = createLine(new LineRequest("2호선", "bg-red-600", 1L, 2L, 10));
        Long lineId = createResponse.jsonPath().getLong("id");

        // when
        deleteLine(lineId);
        ExtractableResponse<Response> response = deleteLine(lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
