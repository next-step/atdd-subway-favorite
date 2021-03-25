package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능을 관리한다.")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 판교역;
    private StationResponse 모란역;
    private Map<String, String> param;
    private Map<String, String> param2;
    private TokenResponse loginResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);
        모란역 = 지하철역_등록되어_있음("모란역").as(StationResponse.class);
        param = new HashMap<>();
        param.put("source",강남역.getId() + "");
        param.put("target", 광교역.getId() + "");

        param2 = new HashMap<>();
        param2.put("source",판교역.getId() + "");
        param2.put("target", 모란역.getId() + "");

        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(createResponse);

        // when
        loginResponse = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(param, loginResponse);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void searchFavorites() {
        // given
        즐겨찾기_생성_요청(param, loginResponse);
        즐겨찾기_생성_요청(param2, loginResponse);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(loginResponse);

        // then
        즐겨찾기_조회됨(response);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(Map<String, String> param, TokenResponse loginResponse) {
        String uri = "/favorites";
        return RestAssured.given().log().all()
                          .auth().oauth2(loginResponse.getAccessToken())
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .body(param)
                          .when()
                          .post(uri)
                          .then().log().all()
                          .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(TokenResponse loginResponse) {
        String uri = "/favorites";
        return RestAssured.given().log().all()
                          .auth().oauth2(loginResponse.getAccessToken())
                          .when()
                          .get(uri)
                          .then().log().all()
                          .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".")).isNotEmpty();
    }
}
