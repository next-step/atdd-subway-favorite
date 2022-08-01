package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "masterAdmin";
    private static final String PASSWORD = "password";
    private String 인증_토큰;
    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;

    /**
     * Given 지하철역과 노선 생성을 요청 하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        인증_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        교대역 = 지하철역_생성_요청(인증_토큰, "교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청(인증_토큰, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(인증_토큰, "양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청(인증_토큰, "남부터미널역").jsonPath().getLong("id");
    }

    /**
     * When 즐겨찾기를 추가하면
     * Then 즐겨찾기 목록에 새로운 즐겨찾기가 추가된다
     */

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
}
