package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("즐겨찾기 관리")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    Long 이호선;
    Long 방배역;
    Long 서초역;
    Long 교대역;
    String 회원ID;
    String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();
        이호선 = 지하철_노선_생성_요청("이호선", "green").jsonPath().getLong("id");
        방배역 = 지하철역_생성_요청("방배역").jsonPath().getLong("id");
        서초역 = 지하철역_생성_요청("서초역").jsonPath().getLong("id");
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(이호선, 방배역, 서초역);
        지하철_노선에_지하철_구간_생성_요청(이호선, 서초역, 교대역);
        회원ID = 회원_생성_요청(EMAIL, PASSWORD, AGE).header("Location").split("/")[2];
        accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기 생성/조회/삭제 관리")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 방배역, 서초역);

        // then
        생성_확인(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);

        // then
        조회_확인(findResponse, 방배역, 서초역);

        // given
        Long id = findResponse.jsonPath().getList("id", Long.class).get(0);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, id);

        // then
        삭제_확인(deleteResponse);
    }

    @DisplayName("로그인하지 않을 경우 즐겨찾기 생성시 401를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "wrong_token"})
    void return401whenUnauthorizedWithCreate(String wrongToken) {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(wrongToken, 방배역, 서초역);

        // then
        권한_없음(response);
    }

    @DisplayName("로그인하지 않을 경우 즐겨찾기 조회시 401를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "wrong_token"})
    void return401whenUnauthorizedWithFind(String wrongToken) {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 방배역, 서초역);

        // then
        생성_확인(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(wrongToken);

        // then
        권한_없음(findResponse);
    }

    @DisplayName("로그인하지 않을 경우 즐겨찾기 삭제시 401를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "wrong_token"})
    void return401whenUnauthorizedWithDelete(String wrongToken) {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 방배역, 서초역);

        // then
        생성_확인(createResponse);

        // given
        Long id = Long.valueOf(createResponse.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(wrongToken, id);

        // then
        권한_없음(response);
    }
}
