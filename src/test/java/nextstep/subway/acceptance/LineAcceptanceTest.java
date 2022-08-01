package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.given;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_목록_조회_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.관리자_로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.권한_없는_회원은_거부됨;
import static nextstep.subway.acceptance.MemberSteps.유저_로그인_되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    String accessToken;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        accessToken = 관리자_로그인_되어_있음();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(accessToken, "2호선", "green");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ExtractableResponse<Response> listResponse = 지하철_노선_목록_조회_요청();

        assertThat(listResponse.jsonPath().getList("name")).contains("2호선");
    }

    @DisplayName("권한이 없는 경우 지하철 노선을 생성할 수 없다")
    @Test
    void createLineFailWhenNoAuthority() {
        // given
        accessToken = 유저_로그인_되어_있음();

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(accessToken, "2호선", "green");

        // then
        권한_없는_회원은_거부됨(response);
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
        지하철_노선_생성_요청(accessToken, "2호선", "green");
        지하철_노선_생성_요청(accessToken, "3호선", "orange");

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
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(accessToken, "2호선", "green");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(accessToken, "2호선", "green");

        // when
        Map<String, String> params = new HashMap<>();
        params.put("color", "red");
        given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(createResponse.header("location"))
                .then().log().all().extract();

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("color")).isEqualTo("red");
    }

    @DisplayName("권한이 없는 경우 지하철 노선을 수정할 수 없습니다")
    @Test
    void updateLineFailWhenNoAuthority() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(accessToken, "2호선", "green");
        accessToken = 유저_로그인_되어_있음();

        // when
        Map<String, String> params = new HashMap<>();
        params.put("color", "red");
        ExtractableResponse<Response> response = given(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(createResponse.header("location"))
                .then().log().all().extract();

        // then
        권한_없는_회원은_거부됨(response);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(accessToken, "2호선", "green");

        // when
        ExtractableResponse<Response> response = given(accessToken)
                .when().delete(createResponse.header("location"))
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("권한이 없는 경우 지하철 노선을 삭제할 수 없습니다")
    @Test
    void deleteLineFailWhenNoAuthority() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(accessToken, "2호선", "green");
        accessToken = 유저_로그인_되어_있음();

        // when
        ExtractableResponse<Response> response = given(accessToken)
                .when().delete(createResponse.header("location"))
                .then().log().all().extract();

        // then
        권한_없는_회원은_거부됨(response);
    }
}
