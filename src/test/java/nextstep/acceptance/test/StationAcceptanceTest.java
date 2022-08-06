package nextstep.acceptance.test;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.step.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static nextstep.acceptance.step.AuthSteps.givenUserRole;
import static nextstep.acceptance.step.AuthSteps.권한검사에_실패한다;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    @DisplayName("관리자 권한으로 지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        StationSteps.지하철역_생성(강남역);

        // then
        지하철역들이_존재한다(강남역);
    }

    @DisplayName("일반 사용자는 지하철역을 생성할 수 없다.")
    @Test
    void createStation_Exception() {
        // when
        var createResponse = 일반사용자_권한으로_지하철역_생성();

        // then
        권한검사에_실패한다(createResponse);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // when
        StationSteps.지하철역_생성(강남역);
        StationSteps.지하철역_생성(역삼역);

        // then
        지하철역들이_존재한다(강남역, 역삼역);
    }

    @DisplayName("관리자는 지하철역을 제거할 수 있다.")
    @Test
    void deleteStation() {
        // given
        var createResponse = StationSteps.지하철역_생성(강남역);

        // when
        StationSteps.지하철역_제거(createResponse.header("location"));

        // then
        지하철역이_존재하지_않는다(강남역);
    }

    @DisplayName("일반 사용자는 지하철역을 제거할 수 없다.")
    @Test
    void deleteStation_Exception() {
        // given
        var createResponse = StationSteps.지하철역_생성(강남역);

        // when
        var deleteResponse = 일반사용자_권한으로_지하철역_제거(createResponse.header("location"));

        // then
        권한검사에_실패한다(deleteResponse);
    }

    @DisplayName("노선에 포함된 지하철역은 제거할 수 없다.")
    @Test
    void deleteStation_Exception2() {
        // given
        Long 지하철역 = 지하철_노선_생성_요청("2호선", "green").jsonPath().getLong("id");
        var 강남역_응답 = 지하철역_생성(강남역);
        var 역삼역_응답 = 지하철역_생성(역삼역);

        Map<String, String> sectionParams = createSectionCreateParams(
                강남역_응답.jsonPath().getLong("id"),
                역삼역_응답.jsonPath().getLong("id")
        );
        var sectionResponse = 지하철_노선에_지하철_구간_생성_요청(지하철역, sectionParams);
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        String 강남역 = 강남역_응답.header("location");
        지하철역_제거에_실패한다(강남역);
    }

    private ExtractableResponse<Response> 일반사용자_권한으로_지하철역_생성() {
        return givenUserRole()
                .body(Map.of("name", 강남역))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 일반사용자_권한으로_지하철역_제거(String location) {
        return givenUserRole()
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    private void 지하철역들이_존재한다(String... stationNames) {
        List<String> responseNames = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        assertThat(responseNames).contains(stationNames);
    }

    private void 지하철역이_존재하지_않는다(String stationName) {
        List<String> stationNames = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).doesNotContain(stationName);
    }

    private void 지하철역_제거에_실패한다(String location) {
        var deleteResponse = 지하철역_제거(location);

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}