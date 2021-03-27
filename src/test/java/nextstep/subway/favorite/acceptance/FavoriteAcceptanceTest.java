package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.favorite.common.FavoriteConstant.*;
import static nextstep.subway.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.member.MemberSteps.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능을 관리한다.")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 판교역;
    private StationResponse 모란역;
    private FavoriteRequest 즐겨찾기_요청_1;
    private FavoriteRequest 즐겨찾기_요청_2;
    private TokenResponse loginResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);
        모란역 = 지하철역_등록되어_있음("모란역").as(StationResponse.class);
        즐겨찾기_요청_1 = new FavoriteRequest(강남역.getId(), 광교역.getId());
        즐겨찾기_요청_2 = new FavoriteRequest(판교역.getId(), 모란역.getId());

        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(createResponse);

        // when
        loginResponse = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(즐겨찾기_요청_1, loginResponse);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void searchFavorites() {
        // given
        즐겨찾기_생성_요청(즐겨찾기_요청_1, loginResponse);
        즐겨찾기_생성_요청(즐겨찾기_요청_2, loginResponse);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(loginResponse);

        // then
        즐겨찾기_조회됨(response);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        ExtractableResponse<Response> 생성된_즐겨찾기 = 즐겨찾기_생성_요청(즐겨찾기_요청_1, loginResponse);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(생성된_즐겨찾기, loginResponse);

        // then
        즐겨찾기_삭제됨(response);
        ExtractableResponse<Response> afterResponse = 즐겨찾기_조회_요청(loginResponse);
        즐겨찾기_조회_내용_없음(afterResponse);
    }

    @DisplayName("권한 없는 경우 테스트")
    @Test
    void testNoAuthentication() {
        // given
        즐겨찾기_생성_요청(즐겨찾기_요청_1, loginResponse);
        TokenResponse 대충만든_토큰 = new TokenResponse("123");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(대충만든_토큰);

        // then
        권한없음(response);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".")).isNotEmpty();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_조회_내용_없음(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getList(".")).hasSize(0);
    }

    private void 권한없음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
