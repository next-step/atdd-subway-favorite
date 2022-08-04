package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.ADMIN_EMAIL;
import static nextstep.subway.acceptance.MemberSteps.MEMBER_EMAIL;
import static nextstep.subway.acceptance.MemberSteps.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private String adminAccessToken;
    private String memberAccessToken;

    @BeforeEach
    void beforeEach(){
        super.setUp();
        adminAccessToken = 로그인_되어_있음(ADMIN_EMAIL, PASSWORD);
        memberAccessToken = 로그인_되어_있음(MEMBER_EMAIL, PASSWORD);
    }
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(adminAccessToken, "2호선", "green");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        ExtractableResponse<Response> listResponse = 지하철_노선_목록_조회_요청();

        assertThat(listResponse.jsonPath().getList("name")).contains("2호선");
    }

    /**
     * given 일반 사용자가 로그인되어 있음
     * when 일반 사용자가 노선 생성 요청
     * then 권한이 없습니다. 에러 발생!
     */
    @DisplayName("일반 사용자가 노선 생성 요청")
    @Test
    void createLineWithMember() {

        //given //when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(memberAccessToken, "2호선", "green");

        //then
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
        지하철_노선_생성_요청(adminAccessToken, "2호선", "green");
        지하철_노선_생성_요청(adminAccessToken, "3호선", "orange");

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
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(adminAccessToken, "2호선", "green");

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
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(adminAccessToken,"2호선", "green");

        // when
        Map<String, String> params = new HashMap<>();
        params.put("color", "red");
        RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(createResponse.header("location"))
                .then().log().all().extract();

        // then
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(createResponse);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("color")).isEqualTo("red");
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
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(adminAccessToken, "2호선", "green");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete(createResponse.header("location"))
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("일반 사용자가 지하철 노선 삭제시 에러 발생")
    @Test
    void deleteLineWithMember() {

        // given
        String location = 지하철_노선_생성_요청(adminAccessToken, "2호선", "green").header("location");

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(memberAccessToken, location);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
     }


}
