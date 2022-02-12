package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 강남역, 역삼역, 선릉역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청("역삼역").jsonPath().getLong("id");
        선릉역 = 지하철역_생성_요청("선릉역").jsonPath().getLong("id");
        Long 이호선 = 지하철_노선_생성_요청(createLineCreateParams(강남역, 역삼역)).jsonPath().getLong("id");
        지하철_노선에_지하철_구간_생성_요청(이호선, createSectionCreateParams(역삼역, 선릉역));
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(이호선);
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 역삼역, 선릉역);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> getResponse = 내_회원_정보_조회_요청(accessToken);
        회원_정보_조회됨(getResponse, EMAIL, AGE);
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역, 선릉역);
        즐겨찾기_생성됨(createResponse);

        ExtractableResponse<Response> getResponse = 즐겨찾기_조회_요청(accessToken);
        즐겨찾기_조회됨(getResponse, 강남역, 선릉역);

        Long 즐겨찾기 = getResponse.jsonPath().getLong("id");
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, 즐겨찾기);
        즐겨찾기_삭제됨(deleteResponse);
    }

}
