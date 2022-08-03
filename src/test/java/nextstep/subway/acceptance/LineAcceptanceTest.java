package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.givenUserRole;
import static nextstep.subway.acceptance.AuthSteps.권한검사에_실패한다;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_목록_조회_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_삭제_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_수정_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("관리자는 지하철 노선을 생성할 수 있다.")
    @Test
    void createLine() {
        // when
        지하철_노선_생성_요청("2호선", "green");

        // then
        지하철_노선이_존재한다("2호선");
    }

    @DisplayName("일반사용자는 지하철 노선을 생성할 수 없다.")
    @Test
    void createLine_Exception() {
        // when
        var createResponse = 일반_사용자로_지하철_노선_생성_요청("2호선", "green");

        // then
        권한검사에_실패한다(createResponse);
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // when
        지하철_노선_생성_요청("2호선", "green");
        지하철_노선_생성_요청("3호선", "orange");

        // then
        지하철_노선이_존재한다("2호선", "3호선");
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // when
        var createResponse = 지하철_노선_생성_요청("2호선", "green");
        String location = createResponse.header("location");

        // then
        노선_정보가_일치한다(location, "2호선", "green");
    }

    @DisplayName("관리자는 지하철 노선을 수정할 수 있다.")
    @Test
    void updateLine() {
        // given
        var createResponse = 지하철_노선_생성_요청("2호선", "green");
        String location = createResponse.header("location");

        // when
        지하철_노선_수정_요청(location, "3호선", "orange");

        // then
        노선_정보가_일치한다(location, "3호선", "orange");
    }

    @DisplayName("일반사용자는 지하철 노선을 수정할 수 없다.")
    @Test
    void updateLine_Exception() {
        // given
        var createResponse = 지하철_노선_생성_요청("2호선", "green");
        String location = createResponse.header("location");

        // when
        var updateResponse = 일반사용자_권한으로_지하철_노선_수정(location, "3호선", "orange");

        // then
        권한검사에_실패한다(updateResponse);
    }

    @DisplayName("관리자는 지하철 노선을 삭제할 수 있다.")
    @Test
    void deleteLine() {
        // given
        var createResponse = 지하철_노선_생성_요청("2호선", "green");
        String location = createResponse.header("location");

        // when
        지하철_노선_삭제_요청(location);

        // then
        지하철_노선이_존재하지_않는다("2호선");
    }

    @DisplayName("일반사용자는 지하철 노선을 삭제할 수 없다.")
    @Test
    void deleteLine_Exception() {
        // given
        var createResponse = 지하철_노선_생성_요청("2호선", "green");
        String location = createResponse.header("location");

        // when
        var deleteResponse = 일반사용자_권한으로_지하철_노선_삭제(location);

        // then
        권한검사에_실패한다(deleteResponse);
    }

    private ExtractableResponse<Response> 일반_사용자로_지하철_노선_생성_요청(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return givenUserRole()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private void 노선_정보가_일치한다(String location, String stationName, String stationColor) {
        var response = 지하철_노선_조회_요청(location);

        assertThat(response.jsonPath().getString("name")).isEqualTo(stationName);
        assertThat(response.jsonPath().getString("color")).isEqualTo(stationColor);
    }

    private ExtractableResponse<Response> 일반사용자_권한으로_지하철_노선_수정(String location, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return givenUserRole()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(location)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 일반사용자_권한으로_지하철_노선_삭제(String location) {
        return givenUserRole()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선이_존재한다(String... lineNames) {
        var listResponse = 지하철_노선_목록_조회_요청();

        assertThat(listResponse.jsonPath().getList("name")).contains(lineNames);
    }

    private void 지하철_노선이_존재하지_않는다(String lineName) {
        var listResponse = 지하철_노선_목록_조회_요청();

        assertThat(listResponse.jsonPath().getList("name")).doesNotContain(lineName);
    }
}
