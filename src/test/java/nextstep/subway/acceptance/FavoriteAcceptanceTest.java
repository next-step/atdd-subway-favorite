package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String INVALID_ACCESS_TOKEN = "invalidAccessToken";

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;

    private String accessToken;
    private String otherAccessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        회원_생성_요청("email@email.com", "password", 20);
        accessToken = 로그인_되어_있음("email@email.com", "password");

        회원_생성_요청("other@email.com", "password", 21);
        otherAccessToken = 로그인_되어_있음("other@email.com", "password");
    }

    /**
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void manageFavorite() {
        final ExtractableResponse<Response> 증겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);
        즐겨찾기_생성됨(증겨찾기_생성_응답);

        final ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(accessToken);
        즐겨찾기_목록_조회_됨(즐겨찾기_목록_조회_응답);

        final ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨_찾기_삭제_요청(accessToken, 증겨찾기_생성_응답.header("Location"));
        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }


    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void createFavorite() {
        // when
        final ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("잘못된 유저 정보로는 즐겨찾기를 생성할 수 없다")
    @Test
    void createFavoriteWithInvalidAccessToken() {
        // when
        final ExtractableResponse<Response> response = 즐겨찾기_생성_요청(INVALID_ACCESS_TOKEN, 강남역, 남부터미널역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("즐겨찾기 목록을 조회한다")
    @Test
    void findAllFavorite() {
        // given
        즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);
        즐겨찾기_생성_요청(accessToken, 양재역, 강남역);
        즐겨찾기_생성_요청(accessToken, 교대역, 양재역);
        즐겨찾기_생성_요청(accessToken, 남부터미널역, 교대역);

        // when
        final ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        assertAll(
                () -> 즐겨찾기_목록_조회_됨(response),
                () -> assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(강남역, 양재역, 교대역, 남부터미널역),
                () -> assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(남부터미널역, 강남역, 양재역, 교대역)
        );
    }

    @DisplayName("잘못된 유저 정보로는 즐겨찾기 목록을 조회할 수 없다")
    @Test
    void findAllFavoriteWithInvalidAccessToken() {
        // given
        즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);
        즐겨찾기_생성_요청(accessToken, 양재역, 강남역);
        즐겨찾기_생성_요청(accessToken, 교대역, 양재역);
        즐겨찾기_생성_요청(accessToken, 남부터미널역, 교대역);

        // when
        final ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(INVALID_ACCESS_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {
        // given
        final ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);

        // when
        final ExtractableResponse<Response> response = 즐겨_찾기_삭제_요청(accessToken, createResponse.header("Location"));

        // then
        즐겨찾기_삭제됨(response);
    }


    @DisplayName("잘못된 유저 정보로는 즐겨찾기를 삭제할 수 없다")
    @Test
    void deleteFavoriteWithInvalidAccessToken() {
        // given
        final ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 남부터미널역);

        // when
        final ExtractableResponse<Response> response = 즐겨_찾기_삭제_요청(INVALID_ACCESS_TOKEN, createResponse.header("Location"));

        // then
        인증에_실패됨(response);
    }

    @DisplayName("다른 사람의 즐겨찾기를 삭제할 수 없다")
    @Test
    void deleteFavoriteByOther() {
        // given
        final ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(otherAccessToken, 강남역, 남부터미널역);

        // when
        final ExtractableResponse<Response> response = 즐겨_찾기_삭제_요청(accessToken, createResponse.header("Location"));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 인증에_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
