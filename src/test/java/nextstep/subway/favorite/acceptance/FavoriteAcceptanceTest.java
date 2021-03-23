package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;

import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_생성됨;
import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_조회됨;
import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.acceptance.FavoriteSteps.즐겨찾기_삭제됨;

@DisplayName("경로 즐겨 찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 남부터미널역;

    @BeforeEach
    public void setUp(){
        super.setUp();
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // given
        final TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        final ExtractableResponse< Response > response = 즐겨찾기_생성_요청(tokenResponse, 교대역, 양재역);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavorite() {
        // given
        final TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        즐겨찾기_생성_요청(tokenResponse, 교대역, 양재역);
        즐겨찾기_생성_요청(tokenResponse, 강남역, 남부터미널역);

        // when
        final ExtractableResponse< Response > response = 즐겨찾기_조회_요청(tokenResponse);

        // then
        즐겨찾기_조회됨(response);
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        final TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
        final ExtractableResponse< Response > createResponse = 즐겨찾기_생성_요청(tokenResponse, 교대역, 양재역);
        즐겨찾기_생성_요청(tokenResponse, 강남역, 남부터미널역);

        // when
        final ExtractableResponse< Response > response = 즐겨찾기_삭제_요청(tokenResponse, createResponse);

        // then
        즐겨찾기_삭제됨(response);
    }
}
