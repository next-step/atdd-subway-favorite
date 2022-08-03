package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_추가_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.PathAcceptanceTest.createSectionCreateParams;
import static nextstep.subway.acceptance.PathAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "masterAdmin";
    private static final String PASSWORD = "password";
    private String 인증_토큰;
    private String 빈_토큰;
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;


    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        인증_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
        빈_토큰 = Strings.EMPTY;
        교대역 = 지하철역_생성_요청(인증_토큰, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(인증_토큰, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(인증_토큰, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(인증_토큰, "남부터미널역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청(인증_토큰, "2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청(인증_토큰, "신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청(인증_토큰, "3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(인증_토큰, 삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
    }

    /**
     * When 즐겨찾기를 추가하면
     * Then 즐겨찾기 목록에 새로운 즐겨찾기가 추가된다
     */
    @Test
    void 즐겨찾기를_추가한다() {
        // when
        즐겨찾기_추가_요청(인증_토큰, 교대역, 양재역);

        // then
        추가_된_즐겨찾기를_목록에서_찾을_수_있다(교대역, 양재역);
    }

    private void 추가_된_즐겨찾기를_목록에서_찾을_수_있다(Long source, Long target) {
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(인증_토큰);
        assertAll(() -> {
            assertThat(즐겨찾기_조회_응답.jsonPath().getList("source.id", Long.class)).containsExactly(source);
            assertThat(즐겨찾기_조회_응답.jsonPath().getList("target.id", Long.class)).containsExactly(target);
        });
    }

    /**
     * Given 즐겨찾기에 새로운 즐겨찾기 추가를 요청하고
     * When 추가 된 즐겨찾기를 제거를 요청하면
     * Then 즐겨찾기가 목록에서 삭제된다
     */

    /**
     * Given 로그인을 하지 않은 사용자가
     * When 즐겨찾기를 추가하면
     * Then 실패한다
     */
    @Test
    void 로그인을_하지_않은_사용자가_즐겨찾기를_추가시_예외를_일으킨다() {
        // when
        var 즐겨찾기_요청_결과 = 즐겨찾기_추가_요청(빈_토큰, 교대역, 양재역);

        // then
        인증_실패(즐겨찾기_요청_결과);
    }

    /**
     * Given 로그인을 하지 않은 사용자가
     * When 즐겨찾기를 조회하면
     * Then 실패한다
     */

    /**
     * Given 로그인을 하지 않은 사용자가
     * When 즐겨찾기를 삭제하면
     * Then 실패한다
     */

    private void 인증_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(401);
    }
}
