package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineSteps;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberSteps;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static int AGE = 10;

    private TokenResponse token;


    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        Map<String, String> lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "신분당선");
        lineCreateParams.put("color", "bg-red-600");
        lineCreateParams.put("upStationId", 강남역.getId() + "");
        lineCreateParams.put("downStationId", 양재역.getId() + "");
        lineCreateParams.put("distance", 10 + "");
        신분당선 = 지하철_노선_등록되어_있음(lineCreateParams).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 5);
        지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 광교역, 6);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        token = MemberSteps.로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("Scenario : 즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {

        // given
        Map<String, String> params = new HashMap<>();

        // when
        ExtractableResponse<Response> createResponse = RestAssured.given().log().all(true)
                .auth().oauth2(token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(params)
                .post("/favorites")
                .then().log().all(true)
                .extract();

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> readAllResponse = RestAssured.given().log().all(true)
                .auth().oauth2(token.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/favorites")
                .then().log().all(true)
                .extract();

        // then
        assertThat(readAllResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        // when
        ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all(true)
                .auth().oauth2(token.getAccessToken())
                .when()
                .delete(createResponse.header("Location"))
                .then().log().all(true)
                .extract();

        // then
        assertThat(readAllResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
