package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_등록되어_있음;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private LineResponse 이호선;
    private TokenResponse 토큰;

    public static ExtractableResponse<Response> 지하철_즐겨찾기_생성_요청(String token, Map<String, String> params) {
        return RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(params)
                .auth().oauth2(token)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);

        지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10).as(LineResponse.class);

        회원_등록되어_있음("test@gmail.com", "test1234", 20);

        토큰 = 로그인_되어_있음("test@gmail.com", "test1234");
    }

    @DisplayName("지하철 즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(교대역.getId()));
        params.put("target", String.valueOf(강남역.getId()));

        ExtractableResponse<Response> response = 지하철_즐겨찾기_생성_요청(토큰.getAccessToken(), params);

        // then
        지하철_즐겨찾기_생성됨(response);
    }

    public void 지하철_즐겨찾기_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
