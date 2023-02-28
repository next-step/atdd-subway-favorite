package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DataLoader;
import nextstep.subway.stub.GithubResponses;
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
    private String 사용자_AccessToken;


    @BeforeEach
    public void setUp() {
        dataLoader.loadDataWithGithubUser();
        사용자_AccessToken = 깃헙_로그인_요청(GithubResponses.사용자1.getCode()).jsonPath().getString("accessToken");
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

    /**
     * Given 즐겨찾기 등록 요청
     * When 즐겨찾기 조회 요청 시
     * Then 조회가 된다
     */
    @DisplayName("즐겨찾기 조회 요청 시 조회가 된다")
    @Test
    void 즐겨찾기_조회_요청_시_조회가_된다() {
        // Given
        즐겨찾기_등록_요청("1", "3");

        // When
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청();

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(사용자_AccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();

        return response;
    }

    private ExtractableResponse<Response> 즐겨찾기_등록_요청(String source, String target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .auth().oauth2(사용자_AccessToken)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
        return response;
    }

}
