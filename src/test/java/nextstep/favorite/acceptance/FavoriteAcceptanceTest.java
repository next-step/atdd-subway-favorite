package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.subway.line.LineRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.member.acceptance.MemberSteps.회원_토큰_로그인;
import static nextstep.subway.SubwaySteps.지하철_노선_생성;
import static nextstep.subway.SubwaySteps.지하철_역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private String 회원;
    private Long 교대역;
    private Long 강남역;
    private Long 이호선;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원 = 회원_토큰_로그인(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        교대역 = 지하철_역_생성("교대역").jsonPath().getLong("id");
        강남역 = 지하철_역_생성("강남역").jsonPath().getLong("id");
        이호선 = 지하철_노선_생성(new LineRequest("이호선", "green", 교대역, 강남역, 10L)).jsonPath().getLong("id");
    }

    /**
     * when 회원이 출발역과 도착역 정보로 즐겨찾기를 생성하면
     * then 회원의 즐겨찾기 목록에서 해당 즐겨찾기를 조회할 수 있다.
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void save_favorite() {
        // when
        RestAssured
                .given().log().all()
                .auth().oauth2(회원)
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(교대역, 강남역))
                .post("/favorites")
                .then()
                .statusCode(HttpStatus.CREATED.value());
        // then
        ExtractableResponse<Response> favoriteResponse = RestAssured.given().log().all()
                .auth().oauth2(회원)
                .when().get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(favoriteResponse.jsonPath().getList("source.id", Long.class)).containsExactly(교대역);
        assertThat(favoriteResponse.jsonPath().getList("target.id", Long.class)).containsExactly(강남역);
    }
}
