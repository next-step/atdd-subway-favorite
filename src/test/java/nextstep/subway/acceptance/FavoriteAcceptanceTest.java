package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "member@email.com";
    private static final String PASSWORD = "password";

    private Long 수서역;
    private Long 복정역;
    private String accessToken;

    /**
     * 수서역 -----<분당선>----- 복정역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        수서역 = 지하철역_생성_요청("수서역").jsonPath().getLong("id");
        복정역 = 지하철역_생성_요청("복정역").jsonPath().getLong("id");

        지하철_노선_생성_요청("분당선", "yellow", 수서역, 복정역, 20);

        accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * When 출발역과 도착역으로 즐겨찾기 구간 생성을 요청하면
     * Then 해당 구간이 즐겨찾기 구간으로 생성된다.
     */
    @DisplayName("즐겨찾기 구간을 생성한다.")
    @Test
    void createFavoriteSection() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(수서역));
        params.put("target", String.valueOf(복정역));

        ExtractableResponse<Response> createResponse = RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/favorites")
            .then().log().all().extract();

        // then
        assertAll(
            () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(createResponse.header("Location")).isEqualTo("/favorites/1")
        );
    }
}
