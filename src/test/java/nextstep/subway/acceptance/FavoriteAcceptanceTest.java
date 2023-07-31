package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.깃헙_AccessToken으로_즐겨_찾기_생성한다;
import static nextstep.subway.acceptance.FavoriteSteps.깃헙_로그인하고_AccessToken_받아온다;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨_찾기_생성_검증;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨_찾기_조회;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨_찾기_조회_검증;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨 찾기 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String 교대역_아이디 = "1";
    public static final String 양재역_아이디 = "3";
    public static final String 강남역_아이디 = "2";
    String accessToken;


    @BeforeEach
    public void setUp() {
        지하철역_생성_요청("교대역");
        지하철역_생성_요청("강남역");
        지하철역_생성_요청("양재역");
        accessToken = 깃헙_로그인하고_AccessToken_받아온다();
    }

    /**
     * When 즐겨 찾기를 생성하면
     * Then 응답코드는 201로 받고 Location 헤더에 주소를 보내준다
     */
    @DisplayName("즐겨 찾기 생성")
    @Test
    void createFavorite() {
        // when
        var response = 깃헙_AccessToken으로_즐겨_찾기_생성한다(accessToken, 교대역_아이디, 양재역_아이디);

        // then
        즐겨_찾기_생성_검증(response);
    }

    /**
     * Given 즐겨 찾기도 2개를 생성하고
     * When 즐겨 찾기를 조회하면
     * Then 저장된 즐겨 찾기를 가져올다 스테이션 정보도 가져온다
     */
    @DisplayName("즐겨 찾기 조회")
    @Test
    void getFavorite() {
        // given
        깃헙_AccessToken으로_즐겨_찾기_생성한다(accessToken, 교대역_아이디, 양재역_아이디);
        깃헙_AccessToken으로_즐겨_찾기_생성한다(accessToken, 양재역_아이디, 강남역_아이디);

        // when
        var response = 즐겨_찾기_조회(accessToken);

        // then
        즐겨_찾기_조회_검증(response);
    }
}
