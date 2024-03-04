package nextstep.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.line.LineSteps;
import nextstep.station.StationSteps;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.FavoriteSteps.*;
import static nextstep.auth.AuthSteps.토큰_생성;
import static nextstep.member.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String OTHER_EMAIL = "email2@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 수내역;
    private Long 서현역;
    private Long 이호선;
    private Long 신분당선;
    private Long 수인분당선;
    private String accessToken;

    /**
     * Given 지하철 역과 노선을 생성하고, 회원가입을 하고, 로그인한 사용자가
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역 = 역_생성("교대역");
        강남역 = 역_생성("강남역");
        양재역 = 역_생성("양재역");
        수내역 = 역_생성("수내역");
        서현역 = 역_생성("서현역");
        이호선 = 노선_생성("2호선", "green", 교대역, 강남역, 10L);
        신분당선 = 노선_생성("신분당선", "red", 강남역, 양재역, 10L);
        수인분당선 = 노선_생성("수인분당선", "yellow", 수내역, 서현역, 10L);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 토큰_생성(EMAIL, PASSWORD);
    }

    /**
     * When 토큰을 가지고 즐겨찾기를 생성하면
     * Then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * When 토큰 없이 즐겨찾기를 생성하면
     * Then 에러를 반환한다.
     */
    @DisplayName("토큰 없이 즐겨찾기를 생성하면 에러를 반환한다.")
    @Test
    void createFavoriteWithoutToken() {
        // when
        ExtractableResponse<Response> response = 토큰_없이_즐겨찾기_생성_요청(교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * When 찾을 수 없는 경로로 즐겨찾기를 생성하면
     * Then 에러를 반환한다.
     */
    @DisplayName("찾을 수 없는 경로를 등록할 경우 에러를 반환한다.")
    @Test
    void validatePathExists() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 수내역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).isEqualTo("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 토큰을 가지고 즐겨찾기를 조회하면
     * Then 즐겨찾기가 조회된다.
     */
    @DisplayName("즐겨찾기를 조회한다")
    @Test
    void getFavorite() {
        // given
        즐겨찾기_생성_요청(accessToken, 교대역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);

        // then
        즐겨찾기_조회됨(response, 교대역, 양재역);
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 토큰을 가지고 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 교대역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createResponse, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 토큰 없이 즐겨찾기를 삭제하면
     * Then 에러를 반환한다.
     */
    @DisplayName("토큰 없이 즐겨찾기를 삭제하면 에러를 반환한다")
    @Test
    void deleteFavoriteWithoutToken() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 교대역, 양재역);

        // when
        ExtractableResponse<Response> response = 토큰_없이_즐겨찾기_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 다른 회원이 즐겨찾기를 생성하고
     * When 본인이 작성하지 않은 즐겨찾기를 삭제하면
     * Then 에러를 반환한다.
     */
    @DisplayName("본인이 작성하지 않은 즐겨찾기를 삭제하면 에러를 반환한다")
    @Test
    void deleteFavoriteByOther() {
        // given
        회원_생성_요청(OTHER_EMAIL, PASSWORD, AGE);
        String otherAccessToken = 토큰_생성(OTHER_EMAIL, PASSWORD);
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(otherAccessToken, 교대역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createResponse, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static Long 역_생성(String name) {
        return StationSteps.createStation(name).jsonPath().getLong("id");
    }

    private static Long 노선_생성(String name, String color, Long upStation, Long downStation, Long distance) {
        return LineSteps.createLine(name, color, upStation, downStation, distance).jsonPath().getLong("id");
    }

}