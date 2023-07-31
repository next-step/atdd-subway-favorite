package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.권한_없음_결과_검증;
import static nextstep.subway.acceptance.FavoriteSteps.깃헙_AccessToken으로_즐겨_찾기_생성한다;
import static nextstep.subway.acceptance.FavoriteSteps.깃헙_로그인하고_AccessToken_받아온다;
import static nextstep.subway.acceptance.FavoriteSteps.잘못된_요청_결과_검증;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨_찾기_삭제_성공_검증;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨_찾기_삭제한다;
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
    public static final int 테스트_즐겨찾기_아이디 = 1;
    public static final int 존재하지_않는_즐겨찾기_아이디 = 1000000;
    public static final String 사용자1_CODE = "aofijeowifjaoief";
    public static final String 사용자2_CODE = "fau3nfin93dmn";
    public static final String 존재하지_않는_지하철역_아이디 = "10000";
    String 사용자1_ACCESS_TOKEN;


    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        지하철역_생성_요청("교대역");
        지하철역_생성_요청("강남역");
        지하철역_생성_요청("양재역");
        사용자1_ACCESS_TOKEN = 깃헙_로그인하고_AccessToken_받아온다(사용자1_CODE);
    }

    /**
     * When 즐겨 찾기를 생성하면
     * Then 응답코드는 201로 받고 Location 헤더에 주소를 보내준다
     */
    @DisplayName("즐겨 찾기 생성한다")
    @Test
    void createFavorite() {
        // when
        var response = 깃헙_AccessToken으로_즐겨_찾기_생성한다(사용자1_ACCESS_TOKEN, 교대역_아이디, 양재역_아이디);

        // then
        즐겨_찾기_생성_검증(response);
    }

    @DisplayName("즐겨 찾기 생성요청시 존재하지 않는 지하철역 아이디가 포함되면 즐겨 찾기가 생성되지 않는다")
    @Test
    void createFavoriteThatContainsNotExistsStation() {
        // when
        var response = 깃헙_AccessToken으로_즐겨_찾기_생성한다(사용자1_ACCESS_TOKEN, 존재하지_않는_지하철역_아이디, 양재역_아이디);

        // then
        잘못된_요청_결과_검증(response);
    }

    /**
     * Given 즐겨 찾기도 2개를 생성하고
     * When 즐겨 찾기를 조회하면
     * Then 저장된 즐겨 찾기를 가져올다 스테이션 정보도 가져온다
     */
    @DisplayName("즐겨 찾기를 조회한다")
    @Test
    void getFavorite() {
        // given
        깃헙_AccessToken으로_즐겨_찾기_생성한다(사용자1_ACCESS_TOKEN, 교대역_아이디, 양재역_아이디);
        깃헙_AccessToken으로_즐겨_찾기_생성한다(사용자1_ACCESS_TOKEN, 양재역_아이디, 강남역_아이디);

        // when
        var response = 즐겨_찾기_조회(사용자1_ACCESS_TOKEN);

        // then
        즐겨_찾기_조회_검증(response);
    }

    /**
     * Given 즐겨 찾기를 생성하고
     * When 해당 즐겨 찾기를 삭제하면
     * Then 204 코드를 응답한다
     */
    @DisplayName("로그인 되고 권한이 있는 즐겨 찾기를 삭제요청하면 삭제된다")
    @Test
    void deleteFavorite() {
        // given
        깃헙_AccessToken으로_즐겨_찾기_생성한다(사용자1_ACCESS_TOKEN, 교대역_아이디, 양재역_아이디);

        // when
        var response = 즐겨_찾기_삭제한다(사용자1_ACCESS_TOKEN, 테스트_즐겨찾기_아이디);

        // then
        즐겨_찾기_삭제_성공_검증(response);
    }

    @DisplayName("로그인 하지 않고 즐겨 찾기를 삭제요청시 삭제 실패한다")
    @Test
    void deleteFavoriteWithoutLogin() {
        // given
        깃헙_AccessToken으로_즐겨_찾기_생성한다(사용자1_ACCESS_TOKEN, 교대역_아이디, 양재역_아이디);

        // when
        var response = 즐겨_찾기_삭제한다("", 테스트_즐겨찾기_아이디);

        // then
        권한_없음_결과_검증(response);
    }

    @DisplayName("내 것이 아닌 즐겨 찾기를 삭제요청시 삭제 실패한다")
    @Test
    void deleteNotMyFavorite() {
        // given
        깃헙_AccessToken으로_즐겨_찾기_생성한다(사용자1_ACCESS_TOKEN, 교대역_아이디, 양재역_아이디);
        String 사용자2_ACCESS_TOKEN = 깃헙_로그인하고_AccessToken_받아온다(사용자2_CODE);

        // when
        var response = 즐겨_찾기_삭제한다(사용자2_ACCESS_TOKEN, 테스트_즐겨찾기_아이디);

        // then
        권한_없음_결과_검증(response);
    }

    @DisplayName("존재하지 않는 즐겨 찾기를 삭제요청시 삭제 실패한다")
    @Test
    void deleteNotExistsFavorite() {
        // given
        깃헙_AccessToken으로_즐겨_찾기_생성한다(사용자1_ACCESS_TOKEN, 교대역_아이디, 양재역_아이디);

        // when
        var response = 즐겨_찾기_삭제한다(사용자1_ACCESS_TOKEN, 존재하지_않는_즐겨찾기_아이디);

        // then
        잘못된_요청_결과_검증(response);
    }
}
