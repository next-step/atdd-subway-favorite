package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.LineSteps.afterTokenLogin;
import static nextstep.subway.acceptance.MemberSteps.ADMIN_EMAIL;
import static nextstep.subway.acceptance.MemberSteps.MEMBER_EMAIL;
import static nextstep.subway.acceptance.MemberSteps.PASSWORD;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_제거_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.comparable;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {


    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(adminAccessToken, "강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * when 일반 사용자가 역을 생성한다
     * then 401 에러가 발생한다
     */
    @DisplayName("일반사용자가 역을 생성할 경우 에러")
    @Test
    void createStationWithMember() {

        //when
        ExtractableResponse<Response> response = 지하철역_생성_요청(memberAccessToken, "강남역");
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
     }
    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        지하철역_생성_요청(adminAccessToken, "강남역");
        지하철역_생성_요청(adminAccessToken, "역삼역");

        // when
        ExtractableResponse<Response> stationResponse = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        // then
        List<StationResponse> stations = stationResponse.jsonPath().getList(".", StationResponse.class);
        assertThat(stations).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(adminAccessToken, "강남역");

        // when
        String location = createResponse.header("location");
        지하철역_제거_요청(adminAccessToken, location);

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).doesNotContain("강남역");
    }

    /**
     * when 일반 사용자가 역을 제거 한다
     * then 401 에러가 발생한다.
     */
    @DisplayName("일반 사용자가 역을 제거할 경우 에러")
    @Test
    void deleteStationWithMember() {

        //when
        String location = 지하철역_생성_요청(adminAccessToken, "강남역").header("location");
        ExtractableResponse<Response> response = 지하철역_제거_요청(memberAccessToken, location);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
     }
}
