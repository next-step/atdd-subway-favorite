package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import nextstep.member.application.dto.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.LoginSteps.깃헙_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 생성 요청 시 등록이 된다")
class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DataLoader dataLoader;
    private String accessToken;


    @BeforeEach
    public void setUp() {
        dataLoader.loadDataWithGithubUser();
        accessToken = 깃헙_로그인_요청(GithubResponses.사용자1.getCode()).jsonPath().getString("accessToken");
        지하철역_생성_요청("강남역");
        지하철역_생성_요청("신주쿠역");
        지하철역_생성_요청("가산역");
    }

    /**
     * When 즐겨찾기를 생성 요청 시
     * Then 등록이 된다
     */
    @DisplayName("즐겨찾기 생성 요청 시 등록이 된다")
    @Test
    void 즐겨찾기_생성_요청_시_등록이_된다() {
        // When
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청("1", "3");

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/favorites/1");
    }

    private ExtractableResponse<Response> 즐겨찾기_등록_요청(String source, String target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
        return response;
    }

}
