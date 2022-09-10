package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 관리자가 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("관리자가 지하철 노선 생성")
    @Test
    void createLineByAdmin() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green", ADMIN_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ExtractableResponse<Response> listResponse = 지하철_노선_목록_조회_요청();

        assertThat(listResponse.jsonPath().getList("name")).contains("2호선");
    }

    /**
     * When 일반 사용자가 지하철 노선을 생성하면
     * Then 권한이 없다는 오류가 발생한다
     */
    @DisplayName("일반 사용자은 지하철 노선을 생성할 수 없다.")
    @Test
    void createLineByMember() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청("2호선", "green", MEMBER_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        지하철_노선_생성_요청("2호선", "green", ADMIN_TOKEN);
        지하철_노선_생성_요청("3호선", "orange", ADMIN_TOKEN);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).contains("2호선", "3호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("2호선", "green", ADMIN_TOKEN);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 관리자가 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("관리자가 지하철 노선 수정")
    @Test
    void updateLineByAdmin() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("2호선", "green", ADMIN_TOKEN);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("color", "red");
        final String location = createResponse.header("location");
        지하철_노선_수정_요청(params, location, ADMIN_TOKEN);

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("color")).isEqualTo("red");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 일반 사용자가 생성한 지하철 노선을 수정하면
     * Then 권한이 없다는 오류가 발생한다
     */
    @DisplayName("일반 사용자는 지하철 노선을 수정할 수 없다.")
    @Test
    void updateLineByMember() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("2호선", "green", ADMIN_TOKEN);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("color", "red");
        final String location = createResponse.header("location");
        final ExtractableResponse<Response> 지하철_노선_수정_응답 = 지하철_노선_수정_요청(params, location, MEMBER_TOKEN);

        // then
        assertThat(지하철_노선_수정_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 괸라자가 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("관리자가 지하철 노선 삭제")
    @Test
    void deleteLineByAdmin() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("2호선", "green", ADMIN_TOKEN);

        // when
        final String location = createResponse.header("location");
        final ExtractableResponse<Response> 지하철_노선_삭제_응답 = 지하철_노선_삭제_요청(location, ADMIN_TOKEN);

        // then
        assertThat(지하철_노선_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 일반 사용자가 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("일반 사용자는 지하철 노선을 삭제할 수 없다.")
    @Test
    void deleteLineByMember() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("2호선", "green", ADMIN_TOKEN);

        // when
        final String location = createResponse.header("location");
        final ExtractableResponse<Response> 지하철_노선_삭제_응답 = 지하철_노선_삭제_요청(location, MEMBER_TOKEN);

        // then
        assertThat(지하철_노선_삭제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
