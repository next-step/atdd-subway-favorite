package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.annotation.AcceptanceTest;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.acceptance.TokenSteps;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.subway.acceptance.fixture.LineFixture;
import nextstep.subway.acceptance.fixture.StationFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.util.RestAssuredUtil.생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest{

    private ExtractableResponse<Response> 교대역;
    private ExtractableResponse<Response> 강남역;
    private ExtractableResponse<Response> 양재역;
    private ExtractableResponse<Response> 남부터미널역;
    private ExtractableResponse<Response> 이호선;
    private ExtractableResponse<Response> 삼호선;

    private String email = "ksyk1205@naver.com";
    private String password = "12341234aa";

    /**
     * given 지하철, 구간을 등록한다.
     */
    @BeforeEach
    void before() {
        교대역 = 생성_요청(
                StationFixture.createStationParams("교대역"),
                "/stations");
        강남역 = 생성_요청(
                StationFixture.createStationParams("강남역"),
                "/stations");
        양재역 = 생성_요청(
                StationFixture.createStationParams("양재역"),
                "/stations");
        남부터미널역 = 생성_요청(
                StationFixture.createStationParams("남부터미널역"),
                "/stations");
        이호선 = 생성_요청(
                LineFixture.createLineParams("2호선", "GREEN", 교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), 10L),
                "/lines");
        삼호선 = 생성_요청(
                LineFixture.createLineParams("3호선", "ORANGE", 양재역.jsonPath().getLong("id"), 남부터미널역.jsonPath().getLong("id"), 2L),
                "/lines");
    }


    /**
     * given 사용자 등록, 토큰을 생성한다.
     * when 즐겨찾기 등록하면
     * then 등록된다.
     */
    @Test
    @DisplayName("즐겨찾기를 등록한다.")
    void 즐겨찾기등록() {
        //given
        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 즐겨찾기_생성요청 = FavoriteSteps.즐겨찾기_생성요청(교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), accessToken);


        //then
        assertThat(즐겨찾기_생성요청.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * when 즐겨찾기를 등록하면
     * then 401에러가 발생한다.
     */
    @Test
    @DisplayName("토큰 없이 즐겨찾기를 등록한다.")
    void 토큰없이_즐겨찾기등록_오류() {
        //when
        ExtractableResponse<Response> 즐겨찾기_생성요청 = FavoriteSteps.즐겨찾기_생성요청(교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), "");

        //then
        assertThat(즐겨찾기_생성요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * given 사용자 등록, 토큰을 생성한다.
     * when 연결되어있지 않은 경로를 즐겨찾기 등록하면
     * then 오류를 반환한다.
     */
    @Test
    @DisplayName("연결되어있지않은 경로는 즐겨찾기 등록시 오류를 반환한다.")
    void 연결되어있지않은경로는_즐겨찾기등록_오류() {
        //given
        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();

        //when
        ExtractableResponse<Response> 즐겨찾기_생성요청 = FavoriteSteps.즐겨찾기_생성요청(교대역.jsonPath().getLong("id"), 양재역.jsonPath().getLong("id"), accessToken);


        //then
        assertThat(즐겨찾기_생성요청.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * given 사용자 등록, 토큰을 생성, 즐겨찾기 등록한다.
     * when 즐겨찾기 조회하면
     * then 즐겨찾기 경로 확인이 가능하다.
     */
    @Test
    @DisplayName("즐겨찾기를 조회한다.")
    void 즐겨찾기조회() {
        //given
        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();
        FavoriteSteps.즐겨찾기_생성요청(교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), accessToken);

        //when
        ExtractableResponse<Response> 즐겨찾기_조회요청 = FavoriteSteps.즐겨찾기_조회요청(accessToken);

        //then
        assertThat(즐겨찾기_조회요청.jsonPath().getList("source").get(0)).isEqualTo(교대역.jsonPath().get());
        assertThat(즐겨찾기_조회요청.jsonPath().getList("target").get(0)).isEqualTo(강남역.jsonPath().get());
    }

    /**
     * given 사용자 등록, 토큰을 생성, 즐겨찾기 등록한다.
     * when 토큰 없이 즐겨찾기 조회하면
     * then 401 오류를 반환한다.
     */
    @Test
    @DisplayName("토큰없이 즐겨찾기를 조회하면 오류를 반환한다.")
    void 토큰없이_즐겨찾기조회() {
        //given
        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();
        FavoriteSteps.즐겨찾기_생성요청(교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), accessToken);

        //when
        ExtractableResponse<Response> 즐겨찾기_조회요청 = FavoriteSteps.즐겨찾기_조회요청("");

        //then
        assertThat(즐겨찾기_조회요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * given 사용자 등록, 토큰을 생성, 즐겨찾기 등록한다.
     * when 즐겨찾기 삭제한다.
     * then 조회했을때 즐겨찾기를 찾을 수 없다.
     */
    @Test
    @DisplayName("즐겨찾기를 삭제한다.")
    void 즐겨찾기삭제() {
        //given
        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> 즐겨찾기_생성요청 = FavoriteSteps.즐겨찾기_생성요청(교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), accessToken);
        Long favoriteId = 즐겨찾기_생성요청.jsonPath().getLong("id");

        //when
        FavoriteSteps.즐겨찾기_삭제요청(favoriteId, accessToken);

        //then
        ExtractableResponse<Response> 즐겨찾기_조회요청 = FavoriteSteps.즐겨찾기_조회요청(accessToken);
        assertThat(즐겨찾기_조회요청.jsonPath().getList("id")).doesNotContain(favoriteId);
    }

    /**
     * given 사용자 등록, 토큰을 생성, 즐겨찾기 등록한다.
     * when 토큰 없이 즐겨찾기 삭제하면
     * then 401 오류를 반환한다.
     */
    @Test
    @DisplayName("토큰없이 즐겨찾기를 삭제하면 오류를 반환한다.")
    void 토큰없이_즐겨찾기삭제() {
        //given
        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> 즐겨찾기_생성요청 = FavoriteSteps.즐겨찾기_생성요청(교대역.jsonPath().getLong("id"), 강남역.jsonPath().getLong("id"), accessToken);
        //when
        ExtractableResponse<Response> 즐겨찾기_삭제요청 = FavoriteSteps.즐겨찾기_삭제요청(즐겨찾기_생성요청.jsonPath().getLong("id"), "123");
        assertThat(즐겨찾기_삭제요청.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * given 사용자 등록, 토큰을 생성한다.
     * when 존재하지 않는 즐겨찾기 삭제하면
     * then 오류를 반환한다.
     */
    @Test
    @DisplayName("존재하지않는 즐겨찾기를 삭제하면 오류를 반환한다.")
    void 존재하지않는_즐겨찾기삭제() {
        //given
        MemberSteps.회원_생성_요청(email, password, 1);
        String accessToken = TokenSteps.토큰_생성요청(email, password).as(TokenResponse.class).getAccessToken();
        //when
        ExtractableResponse<Response> 즐겨찾기_삭제요청 = FavoriteSteps.즐겨찾기_삭제요청(999L, accessToken);
        assertThat(즐겨찾기_삭제요청.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
