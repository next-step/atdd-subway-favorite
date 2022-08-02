package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.givenUserRole;
import static nextstep.subway.acceptance.AuthSteps.권한검사에_실패한다;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성;
import static nextstep.subway.acceptance.StationSteps.지하철역_제거;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("관리자 권한으로 지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        지하철역_생성("강남역");

        // then
        지하철역들이_존재한다("강남역");
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
        지하철역_생성("강남역");
        지하철역_생성("역삼역");

        // then
        지하철역들이_존재한다("강남역", "역삼역");
    }

    @DisplayName("관리자는 지하철역을 제거할 수 있다.")
    @Test
    void deleteStation() {
        // given
        var createResponse = 지하철역_생성("강남역");

        // when
        지하철역_제거(createResponse.header("location"));

        // then
        지하철역이_존재하지_않는다("강남역");
    }

    @DisplayName("일반 사용자는 지하철역을 제거할 수 없다.")
    @Test
    void deleteStation_Exception() {
        // given
        var createResponse = 지하철역_생성("강남역");

        // when
        var deleteResponse = 일반사용자_권한으로_지하철역_제거(createResponse.header("location"));

        // then
        권한검사에_실패한다(deleteResponse);
    }

    private ExtractableResponse<Response> 일반사용자_권한으로_지하철역_생성() {
        return givenUserRole()
                .body(Map.of("name", "강남역"))
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
}