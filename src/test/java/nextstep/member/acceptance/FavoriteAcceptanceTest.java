package nextstep.member.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.member.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.member.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.member.acceptance.MemberSteps.토큰_발급_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private String accessToken;
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        var 토큰_발급_응답 = 토큰_발급_요청(EMAIL, PASSWORD);
        accessToken = 토큰_발급_응답.jsonPath().getString("accessToken");

        Long 신분당선 = 지하철_노선_생성_요청("신분당선", "bg-red-600")
                .jsonPath()
                .getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(신분당선, createSectionCreateParams(교대역, 강남역));
    }

    /**
     * Given 로그인을 하고
     * When 즐겨찾기 생성을 요청하면
     * Then 즐겨찾기가 생성되고 조회할 수 있다
     */
    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void createFavorite() {
        // when
        var createResponse = 즐겨찾기_생성_요청(accessToken, 교대역, 강남역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        var selectResponse = 즐겨찾기_조회_요청(accessToken);

        assertThat(selectResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectResponse.jsonPath().getLong("[0].source.id")).isEqualTo(교대역);
        assertThat(selectResponse.jsonPath().getLong("[0].target.id")).isEqualTo(강남역);
    }


    /**
     * Given 로그인을 하고
     * And 즐겨찾기를 생성하고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제되고 조회할 수 없다
     */
    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {
        // and
        var createResponse = 즐겨찾기_생성_요청(accessToken, 교대역, 강남역);

        // when
        var deleteResponse = 즐겨찾기_삭제_요청(accessToken, createResponse);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        var selectResponse = 즐겨찾기_조회_요청(accessToken);
        assertThat(selectResponse.jsonPath().getList(".").size()).isEqualTo(0);
    }

    /**
     * Given 로그인을 하지 않고
     * When 즐겨찾기 생성을 요청하면
     * Then 에러가 발생한다
     */
    @DisplayName("로그인을 하지 않고 즐겨찾기를 생성하면 에러가 발생한다")
    @Test
    void createFavorite_notLogin_exception() {
        // when
        var createResponse = 즐겨찾기_생성_요청(교대역, 강남역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인을 하지 않고
     * When 즐겨찾기 조회를 요청하면
     * Then 에러가 발생한다
     */
    @DisplayName("로그인을 하지 않고 즐겨찾기를 조회하면 에러가 발생한다")
    @Test
    void selectFavorite_notLogin_exception() {
        // given
        즐겨찾기_생성_요청(accessToken, 교대역, 강남역);

        // when
        var selectResponse = 즐겨찾기_조회_요청();

        // then
        assertThat(selectResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인을 하지 않고
     * When 즐겨찾기 삭제를 요청하면
     * Then 에러가 발생한다
     */
    @DisplayName("로그인을 하지 않고 즐겨찾기를 삭제하면 에러가 발생한다")
    @Test
    void deleteFavorite_notLogin_exception() {
        // given
        var createResponse = 즐겨찾기_생성_요청(accessToken, 교대역, 강남역);

        // when
        var deleteResponse = 즐겨찾기_삭제_요청(createResponse);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인을 하고
     * And 존재하지 않는 경로를 생성하고
     * When 즐겨찾기 생성을 요청하면
     * Then 에러가 발생한다
     */
    @DisplayName("존재하지 않는 경로를 즐겨찾기로 생성하면 에러가 발생한다")
    @Test
    void createFavorite_notExistSection_exception() {
        Long sourceId = -999L;

        // when
        var createResponse = 즐겨찾기_생성_요청(accessToken, sourceId, 강남역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 로그인을 하고
     * And 연결되지 않은 경로를 생성하고
     * When 즐겨찾기 생성을 요청하면
     * Then 에러가 발생한다
     */
    @DisplayName("연결되지 않은 경로를 즐겨찾기로 생성하면 에러가 발생한다")
    @Test
    void createFavorite_notConnectSection_exception() {
        // when
        var createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 양재역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Map<String, String> createSectionCreateParams(Long upStationId, Long downStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", 6 + "");
        return params;
    }
}
