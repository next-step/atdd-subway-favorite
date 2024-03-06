package nextstep.favorite.acceptance;

import static nextstep.member.acceptance.AuthSteps.인증_요청_하기;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.member.domain.Member;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Member member;
    private String accessToken;
    private Long 북한역;
    private Long 남한역;

    private Long 한국선;
    private Long 강남역;
    private Long 양재역;
    private Long 양재시민의숲역;
    private Long 판교역;
    private Long 신사역;
    private Long 잠원역;
    private Long 고속터미널역;
    private Long 교대역;
    private Long 남부터미널역;
    private Long 신분당선;
    private Long 삼호선;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        String email = "test@test.com";
        String password = "test";
        int age = 30;
        회원_생성_요청(email, password, age);
        accessToken = 인증_요청_하기(email, password).jsonPath().get("accessToken");
        신분당선_3호선_만들기();
    }

    private void 신분당선_3호선_만들기() {
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        양재시민의숲역 = 지하철역_생성_요청("양재시민의숲역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        신사역 = 지하철역_생성_요청("신사역").jsonPath().getLong("id");
        잠원역 = 지하철역_생성_요청("잠원역").jsonPath().getLong("id");
        고속터미널역 = 지하철역_생성_요청("고속터미널역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        북한역 = 지하철역_생성_요청("북한역").jsonPath().getLong("id");
        남한역 = 지하철역_생성_요청("남한역").jsonPath().getLong("id");
        신분당선 = 지하철_노선_생성_요청("신분당선", "RED", 강남역, 양재역, 10).jsonPath().getLong("id");
        삼호선 = 지하철_노선_생성_요청("3호선", "YELLOW", 신사역, 잠원역, 10).jsonPath().getLong("id");
        한국선 = 지하철_노선_생성_요청("한국선", "RED", 북한역, 남한역, 10).jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, 양재역, 판교역, 10);
        지하철_노선에_지하철_구간_생성_요청(신분당선, 판교역, 양재시민의숲역, 10);
        지하철_노선에_지하철_구간_생성_요청(삼호선, 강남역, 신사역, 10);
        지하철_노선에_지하철_구간_생성_요청(삼호선, 잠원역, 고속터미널역, 10);
        지하철_노선에_지하철_구간_생성_요청(삼호선, 고속터미널역, 교대역, 10);
        지하철_노선에_지하철_구간_생성_요청(삼호선, 교대역, 남부터미널역, 10);
    }


    /**
     * Given 출발역, 도착역이 주어진다.
     * When 즐겨찾기를 추가한다.
     * Then 즐겨찾기가 추가된다.
     */
    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void addFavorite() {
        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(accessToken, 강남역, 교대역);
        // then
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * When 로그인이 되어 있지 않으면
     * Then 즐겨찾기를 추가할 수 없다.
     */
    @DisplayName("로그인이 되어 있지 않으면 즐겨찾기를 추가할 수 없다.")
    @Test
    void addFavoriteWithoutLogin() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(강남역));
        params.put("target", String.valueOf(교대역));
        // when
        ExtractableResponse<Response> response = FavoriteSteps.로그인_없이_즐겨찾기_생성_요청(강남역, 교대역);
        // then
        assertThat(response.statusCode()).isEqualTo(401);
    }

    /**
     * Given 출발역, 도착역이 주어진다.
     * When 즐겨찾기를 추가할 때 출발역과 도착역이 연결된 경로를 찾을 수 없으면
     * Then 즐겨찾기가 추가 되지 않고 예외가 발생한다.
     */
    @DisplayName("즐겨찾기를 추가하면 예외가 발생한다")
    @Test
    void addFavoriteNotExistsLocation() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("source", "1");
        params.put("target", String.valueOf(북한역));
        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(accessToken, 1L, 북한역);
        // then
        assertThat(response.statusCode()).isEqualTo(400);
    }

    /**
     * Given 즐겨찾기가 주어진다.
     * When 즐겨찾기를 조회한다.
     * Then 즐겨찾기가 조회된다
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavorites() {
        // given
        FavoriteSteps.즐겨찾기_생성_요청(accessToken, 강남역, 교대역);
        FavoriteSteps.즐겨찾기_생성_요청(accessToken, 강남역, 신사역);
        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_목록_조회(accessToken);
        // then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body().jsonPath().getList("id")).isNotEmpty();
    }

    /**
     * Given 즐겨찾기가 주어진다.
     * When 즐겨찾기를 삭제한다.
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(accessToken, 강남역, 교대역);
        String location = response.header("Location");
        assertThat("/favorites/1").isEqualTo(location);
        // when
        response = FavoriteSteps.즐겨찾기_삭제_요청(accessToken, location);
        // then
        assertThat(response.statusCode()).isEqualTo(204);
    }
}
