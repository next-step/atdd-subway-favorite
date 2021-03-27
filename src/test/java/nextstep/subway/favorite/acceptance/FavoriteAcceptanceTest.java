package nextstep.subway.favorite.acceptance;

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

import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;
    private LineResponse 이호선;
    private LineResponse 신분당선;
    private LineResponse 삼호선;
    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "green", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_등록되어_있음("3호선", "green", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // given
        회원_생성_요청("email@email.com", "password", 20);
        tokenResponse = 로그인_되어_있음("email@email.com", "password");

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(tokenResponse, 교대역, 강남역);

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(tokenResponse);

        // then
        즐겨찾기_조회됨(findResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(tokenResponse, createResponse);

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("즐겨찾기 저장을 실패한다.")
    @Test
    void createFavoriteFail() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(교대역, 강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("즐겨찾기 조회를 실패한다.")
    @Test
    void getFavoriteFail() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("즐겨찾기 삭제를 실패한다.")
    @Test
    void deleteFavoriteFail() {
        // given
        회원_생성_요청("email@email.com", "password", 20);
        tokenResponse = 로그인_되어_있음("email@email.com", "password");
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(tokenResponse, 교대역, 강남역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
