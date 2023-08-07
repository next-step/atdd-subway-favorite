package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.member.acceptance.FavoriteSteps.*;
import static nextstep.member.steps.MemberSteps.회원_생성_요청;
import static nextstep.member.steps.TokenSteps.로그인_요청_토큰_반환;
import static nextstep.subway.steps.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.steps.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.steps.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 인수 테스트")
class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 서울역;
    private Long 용산역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;
    private Long 일호선;

    private String accessToken;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */

    /**
     * Given 지하철 노선과 역을 생성하고
     * And 회원가입을 생성하고
     * */
    @BeforeEach
    public void setUp() {
        super.setUp();

        서울역 = 지하철역_생성_요청("서울역").jsonPath().getLong("id");
        용산역 = 지하철역_생성_요청("용산역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        일호선 = 지하철_노선_생성_요청("1호선", "blue", 서울역, 용산역, 10);
        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, 남부터미널역, 양재역, 3);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_요청_토큰_반환(EMAIL, PASSWORD);
    }

    /**
     * When 출발역과 도착역으로 즐겨찾기 생성 요청을 하면
     * Then 즐겨찾기 조회 시 생성된 즐겨찾기를 확인할 수 있다
     * */
    @Test
    @DisplayName("즐겨찾기 생성")
    void createFavorite() {
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<Long> favoriteIds = 즐겨찾기_조회_요청(accessToken)
                .jsonPath().getList("id", Long.class);
        assertThat(favoriteIds.size()).isEqualTo(1);
    }

    /**
     * When 경로조회가 불가능한 출발역과 도착역으로 즐겨찾기 생성 요청을 하면
     * Then 즐겨찾기 생성에 실패한다
     * */
    @Test
    @DisplayName("즐겨찾기 생성 실패 - 경로 조회가 불가능한 경우")
    void createFavoriteFail() {
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 서울역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        List<Long> favoriteIds = 즐겨찾기_조회_요청(accessToken)
                .jsonPath().getList("id", Long.class);
        assertThat(favoriteIds.size()).isEqualTo(0);
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 생성된 즐겨찾기 삭제 요청을 하면
     * Then 해당 즐겨찾기는 삭제된다
     * */
    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() {
        String location = 즐겨찾기_생성_요청(accessToken, 교대역, 양재역).header("Location");
        즐겨찾기_삭제_요청(accessToken, location);

        List<Long> favoriteIds = 즐겨찾기_조회_요청(accessToken)
                .jsonPath().getList("id", Long.class);
        assertThat(favoriteIds.size()).isEqualTo(0);
    }

    /**
     * When 권한 없이 즐겨찾기 조회 요청을 하면
     * Then 권한 오류가 발생한다
     * */
    @Test
    @DisplayName("즐겨찾기 조회 실패 - 권한 오류")
    void findFavoriteFailAuth() {
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청("wrong_acccess_token");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
