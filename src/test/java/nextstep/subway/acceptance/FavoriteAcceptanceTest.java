package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성함;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 역삼역;
    private String 인증_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성함("강남역");
        역삼역 = 지하철역_생성함("역삼역");
        인증_토큰 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * When 사용자가 즐겨찾기를 추가하면
     * Then 추가한 사용자가 즐겨찾기 목록 조회 시, 추가한 항목을 확인할 수 있습니다.
     */
    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        // when
        final ExtractableResponse<Response> 즐겨찾기_추가_응답 = 즐겨찾기_추가_요청(인증_토큰, 강남역, 역삼역);

        // then
        즐겨찾기가_정상적으로_추가되었음을_확인(인증_토큰, 즐겨찾기_추가_응답, 강남역, 역삼역);
    }

    /**
     * Given 사용자가 즐겨찾기를 2개 추가하고
     * When 추가한 사용자가 즐겨찾기 목록을 조회하면
     * Then 추가한 즐겨찾기 항목 2개를 목록에서 확인할 수 있습니다.
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        // given
        즐겨찾기_추가_요청함(인증_토큰, 강남역, 역삼역);
        즐겨찾기_추가_요청함(인증_토큰, 역삼역, 강남역);

        // when
        final ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(인증_토큰);

        // then
        즐겨찾기_목록_조회가_정상적으로_되었는지_확인(즐겨찾기_목록_조회_응답, 2);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given

        // when

        // then
    }
}
