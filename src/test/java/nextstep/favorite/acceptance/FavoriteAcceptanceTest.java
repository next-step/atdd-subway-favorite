package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.subway.line.LineRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.favorite.acceptance.FavoriteSteps.*;
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
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 삼호선;


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원 = 회원_토큰_로그인(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        교대역 = 지하철_역_생성("교대역").jsonPath().getLong("id");
        강남역 = 지하철_역_생성("강남역").jsonPath().getLong("id");
        양재역 = 지하철_역_생성("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철_역_생성("남부터미널역").jsonPath().getLong("id");
        이호선 = 지하철_노선_생성(new LineRequest("이호선", "green", 교대역, 강남역, 10L)).jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성(new LineRequest("삼호선", "orange", 양재역, 남부터미널역, 5L)).jsonPath().getLong("id");
    }

    /**
     * when 회원이 출발역과 도착역 정보로 즐겨찾기를 생성하면
     * then 201 Created 코드로 응답받는다.
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void save_favorite() {
        // when
        var response = 즐겨찾기_생성(회원, new FavoriteRequest(교대역, 강남역));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * given 회원이 출발역과 도착역 정보로 즐겨찾기를 생성하고
     * when 회원의 즐겨찾기 목록을 조회하면
     * then 해당 즐겨찾기가 포함되어 있다.
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void find_favorites() {
        // given
        즐겨찾기_생성(회원, new FavoriteRequest(교대역, 강남역));

        // when
        var response = 즐겨찾기_조회(회원);

        // then
        assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(교대역);
        assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(강남역);
    }

    /**
     * given 회원이 출발역과 도착역 정보로 즐겨찾기를 생성하고
     * when 해당 즐겨찾기를 삭제하면
     * then 즐겨찾기 목록 조회 시 해당 즐겨찾기가 포함되지 않는다.
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void delete_favorite() {
        // given
        var createResponse = 즐겨찾기_생성(회원, new FavoriteRequest(교대역, 강남역));
        Long favoriteId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        // when
        즐겨찾기_삭제(회원, createResponse);

        // then
        var findResponse = 즐겨찾기_조회(회원);

        assertThat(findResponse.jsonPath().getList("id", Long.class)).doesNotContain(favoriteId);
    }

    /**
     * when 회원이 경로가 존재하지 않는 출발역, 도착역 정보로 즐겨찾기를 등록하면
     * then 403 Bad Request 코드로 응답한다.
     */
    @DisplayName("존재하지 않는 경로를 즐겨찾기로 추가")
    @Test
    void error_존재하지_않는_경로_등록() {
        RestAssured.given().log().all()
                .auth().oauth2(회원)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(교대역, 양재역))
                .when().post("/favorites")
                .then().log().all().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * when 회원이 등록하지 않은 즐겨찾기 정보를 삭제하면
     * then 500 Internal server error 코드로 응답한다.
     */
    @Test
    void error_존재하지_않는_즐겨찾기_삭제() {
        RestAssured.given().log().all()
                .auth().oauth2(회원)
                .when().delete("/favorites/" + 1L)
                .then().log().all().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
