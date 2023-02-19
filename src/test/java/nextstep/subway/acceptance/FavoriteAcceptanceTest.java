package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String ID = "id";
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 10;
    private static final String 로그인하지_않은_토큰 = "test";
    private static final Long 존재하지않음 = 99L;
    private Long 강남역;
    private Long 양재역;
    private String 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong(ID);
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong(ID);
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        토큰 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    @DisplayName("로그인한 사용자는 즐겨찾기를 저장한다.")
    @Test
    void saveFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(토큰, 강남역, 양재역);

        즐겨찾기_생성됨(즐겨찾기_생성_응답, 강남역, 양재역);
    }

    @DisplayName("즐겨찾기 요청 역이 누락되어서 저장에 실패한다.")
    @Test
    void error_inputValue_saveFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(토큰, null, 양재역);

        요청값_누락으로_실패됨(즐겨찾기_생성_응답);
    }

    @DisplayName("로그인하지 않은 사용자는 즐겨찾기 저장에 실패한다.")
    @Test
    void error_noAuth_saveFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인하지_않은_토큰, 강남역, 양재역);

        권한없어서_실패됨(즐겨찾기_생성_응답);
    }

    @DisplayName("등록되지 않은 역은 즐겨찾기 저장에 실패한다.")
    @Test
    void error_noRegisterStation_saveFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(토큰, 존재하지않음, 양재역);

        역이_등록되어_있지_않아서_실패됨(즐겨찾기_생성_응답);
    }

    @DisplayName("로그인한 사용자는 저장된 즐겨찾기를 보여준다.")
    @Test
    void showFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(토큰, 강남역, 양재역);

        final Long 즐겨찾기 = 즐겨찾기_생성_응답.jsonPath().getLong("id");
        final ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(토큰, 즐겨찾기);

        즐겨찾기_조회됨(즐겨찾기_조회_응답, 강남역, 양재역);
    }
    @DisplayName("로그인하지 않은 사용자는 즐겨찾기 조회에 실패한다.")
    @Test
    void error_showFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(토큰, 강남역, 양재역);

        final Long 즐겨찾기 = 즐겨찾기_생성_응답.jsonPath().getLong("id");
        final ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(로그인하지_않은_토큰, 즐겨찾기);

        권한없어서_실패됨(즐겨찾기_조회_응답);
    }

    @DisplayName("존재하지 않는 즐겨찾기는 조회할 수 없다")
    @Test
    void error_noFavorite_showFavorite() {

        즐겨찾기_생성_요청(토큰, 강남역, 양재역);

        final ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(토큰, 존재하지않음);

        즐겨찾기_조회_없음(즐겨찾기_조회_응답);
    }

    @DisplayName("로그인한 사용자는 즐겨찾기 제거의 성공한다.")
    @Test
    void removeFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(토큰, 강남역, 양재역);

        final Long 즐겨찾기 = 즐겨찾기_생성_응답.jsonPath().getLong("id");
        final ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(토큰, 즐겨찾기);

        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }

    @DisplayName("로그인하지 않은 사용자는 즐겨찾기를 제거한다.")
    @Test
    void error_removeFavorite() {

        final ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(토큰, 강남역, 양재역);

        final Long 즐겨찾기 = 즐겨찾기_생성_응답.jsonPath().getLong("id");
        final ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(로그인하지_않은_토큰, 즐겨찾기);

        권한없어서_실패됨(즐겨찾기_삭제_응답);
    }

    @DisplayName("존재하지 않는 즐겨찾기는 제거할 수 없다")
    @Test
    void error_noFavorite_removeFavorite() {

        즐겨찾기_생성_요청(토큰, 강남역, 양재역);

        final ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_삭제_요청(토큰, 존재하지않음);

        즐겨찾기_삭제대상_없음(즐겨찾기_조회_응답);
    }
}
