package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_목록_조회_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_삭제_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_수정_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.EMAIL;
import static nextstep.subway.acceptance.MemberSteps.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선을 생성하면 Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ExtractableResponse<Response> listResponse = 지하철_노선_목록_조회_요청();

        assertThat(listResponse.jsonPath().getList("name")).contains("2호선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고 When 지하철 노선 목록을 조회하면 Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        지하철_노선_생성_요청("2호선", "green");
        지하철_노선_생성_요청("3호선", "orange");

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).contains("2호선", "3호선");
    }

    /**
     * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 조회하면 Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
    }

    /**
     * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 수정하면 Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("2호선", "green");

        // when
        Map<String, String> params = new HashMap<>();
        params.put("color", "red");
        지하철_노선_수정_요청(로그인_되어_있음(EMAIL, PASSWORD), params, createResponse);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("color")).isEqualTo("red");
    }

    /**
     * Given 지하철 노선을 생성하고 When 생성한 지하철 노선을 삭제하면 Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(로그인_되어_있음(EMAIL, PASSWORD), createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
