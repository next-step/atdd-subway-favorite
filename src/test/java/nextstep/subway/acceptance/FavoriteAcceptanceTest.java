package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.FavoriteSteps;
import nextstep.subway.acceptance.step.StationSteps;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.step.FavoriteSteps.권한_없음_응답됨;
import static nextstep.subway.acceptance.step.FavoriteSteps.즐겨찾기_생성_응답됨;

@DisplayName("즐겨 찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private static String MAIL = "mail";
    private static String PASSWORD = "password";
    private static int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 판교역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationSteps.지하철역_생성_조회("강남역");
        판교역 = StationSteps.지하철역_생성_조회("판교역");
        MemberSteps.회원_생성_요청(MAIL, PASSWORD, AGE);
    }

    /**
     * When 로그인 없이 즐겨찾기 생성을 요청하면
     * Then 권한 없음을 응답받는다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createFavorite() {
        // given
        String 토큰 = 로그인_되어_있음(MAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청(토큰, 강남역.getId(), 판교역.getId());

        // then
        즐겨찾기_생성_응답됨(response);
    }

    /**
     * When 로그인 없이 즐겨찾기 생성을 요청하면
     * Then 권한 없음을 응답받는다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createFavorite_fail() {
        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성_요청("", 강남역.getId(), 판교역.getId());

        // then
        권한_없음_응답됨(response);
    }

}
