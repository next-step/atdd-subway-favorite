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

import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 양재역;
    
    private String accessToken;

    /**
     * GIVEN 지하철 역을 두개 생성하고
     * GIVEN 회원가입 후 로그인하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        회원_생성_요청("bactoria@gmail.com", "qwe123", 20);
        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청("bactoria@gmail.com", "qwe123");
        accessToken = Access_Token을_가져온다(베어러_인증_로그인_응답);
    }

    /**
     * WHEN 로그인한 회원이 출발역과 도착역을 즐겨찾기 하는 경우 <br>
     * THEN 즐겨찾기가 추가된다 <br>
     */
    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("source", 강남역 + "");
        params.put("target", 양재역 + "");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
