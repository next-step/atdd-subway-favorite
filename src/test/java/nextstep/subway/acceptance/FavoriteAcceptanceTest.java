package nextstep.subway.acceptance;

import static nextstep.auth.token.acceptance.TokenSteps.로그인_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.상태코드_400_응답;
import static nextstep.subway.acceptance.FavoriteSteps.상태코드_401_응답;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록에_즐겨찾기가_존재하지_않는다;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록에_즐겨찾기가_존재한다;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_추가;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_추가_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.SectionAcceptanceTest.createLineCreateParams;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.utils.Constants.EMAIL;
import static nextstep.subway.utils.Constants.PASSWORD;
import static nextstep.subway.utils.Constants.UNKNOWN_TOKEN;

import java.util.Map;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 기능 인수테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 양재역;

    @BeforeEach
    void init() {
        super.setUp();
        회원_생성_요청(EMAIL, PASSWORD, 20);

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
        지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    /*
    Given 즐겨찾기를 추가하면
    When 즐겨찾기 목록을 조회했을 때
    Then 즐겨찾기 목록에 추가한 즐겨찾기가 조회된다
     */
    @DisplayName("즐겨찾기를 추가하고 조회한다")
    @Test
    void favoriteAddAndFind() {
        var 토큰 = 로그인_요청(EMAIL, PASSWORD);
        즐겨찾기_추가(토큰, 강남역, 양재역);

        var 즐겨찾기 = 즐겨찾기_조회(토큰);

        즐겨찾기_목록에_즐겨찾기가_존재한다(즐겨찾기, 강남역, 양재역);
    }

    /*
    Given and When 연결되지 않은 역을 즐겨찾기에 추가하면
    Then           400 Bad Request를 응답받는다
     */
    @DisplayName("연결되지 않은 경로를 즐겨찾기에 추가해 실패한다")
    @Test
    void favoriteAdd_fail_unconnectedRoute() {
        var 판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
        var 토큰 = 로그인_요청(EMAIL, PASSWORD);
        var 상태코드 = 즐겨찾기_추가_요청(토큰, 강남역, 판교역).statusCode();

        상태코드_400_응답(상태코드);
    }

    /*
    Given and When 존재하지 않는 역을 즐겨찾기에 추가하면
    Then           400 Bad Request를 응답받는다
     */
    @DisplayName("존재하지 않는 역을 즐겨찾기에 추가해 실패한다")
    @Test
    void favoriteAdd_fail_notExistStation() {
        var 포틀랜드역 = Long.MAX_VALUE;
        var 토큰 = 로그인_요청(EMAIL, PASSWORD);
        var 상태코드 = 즐겨찾기_추가_요청(토큰, 강남역, 포틀랜드역).statusCode();

        상태코드_400_응답(상태코드);
    }

    /*
    Given 권한이 없는 유저가
    When 즐겨찾기를 추가하면
    Then 401 Unauthorized를 응답받는다
     */
    @DisplayName("권한이 없는 유저가 즐겨찾기를 추가에 실패한다")
    @Test
    void favoriteAdd_fail_withoutAuth() {
        var 즐겨찾기_추가_상태코드 = 즐겨찾기_추가_요청(UNKNOWN_TOKEN, 강남역, 양재역).statusCode();

        상태코드_401_응답(즐겨찾기_추가_상태코드);
    }

    /*
    Given 권한이 없는 유저가
    When 즐겨찾기를 조회하면
    Then 401 Unauthorized를 응답받는다
    * */
    @DisplayName("권한이 없는 유저가 즐겨찾기 조회에 실패한다")
    @Test
    void favoriteFindWithoutAuth() {
        var 즐겨찾기_조회_상태코드 = 즐겨찾기_조회_요청(UNKNOWN_TOKEN).statusCode();

        상태코드_401_응답(즐겨찾기_조회_상태코드);
    }

    /*
    Given 즐겨찾기 목록이 있고
    When 즐겨찾기를 삭제하면
    Then 즐겨찾기 목록에서 삭제한 즐겨찾기가 조회되지 않는다
     */
    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void favoriteDelete() {
        var 토큰 = 로그인_요청(EMAIL, PASSWORD);
        var 강남역_양재역_즐겨찾기 = 즐겨찾기_추가(토큰, 강남역, 양재역);

        즐겨찾기_삭제(토큰, 강남역_양재역_즐겨찾기);

        var 즐겨찾기 = 즐겨찾기_조회(토큰);
        즐겨찾기_목록에_즐겨찾기가_존재하지_않는다(즐겨찾기, 강남역_양재역_즐겨찾기);
    }

    /*
    Given 즐겨찾기를 추가한 후
    And 권한이 없는 유저가
    When 즐겨찾기를 삭제하면
    Then 401 Unauthorized를 응답받는다
    * */
    @DisplayName("권한이 없는 유저가 즐겨찾기를 삭제한다")
    @Test
    void favoriteDeleteWithoutAuth() {
        var 토큰 = 로그인_요청(EMAIL, PASSWORD);
        회원_생성_요청("email2@email.com", "password2", 25);
        var 토큰2 = 로그인_요청("email2@email.com", "password2");
        var 강남역_신논현역_즐겨찾기 = 즐겨찾기_추가(토큰, 강남역, 양재역);

        int 즐겨찾기_삭제_상태코드 = 즐겨찾기_삭제_요청(토큰2, 강남역_신논현역_즐겨찾기).statusCode();

        상태코드_401_응답(즐겨찾기_삭제_상태코드);
    }

    /*
    When 없는 즐겨찾기 id에 대해 즐겨찾기를 삭제 요청하면
    Then 400 Bad Request를 응답받는다
    * */
    @DisplayName("없는 즐겨찾기 id에 대해 즐겨찾기를 삭제한다")
    @Test
    void favoriteDeleteWithInvalidId() {
        var 토큰 = 로그인_요청(EMAIL, PASSWORD);
        var 즐겨찾기_삭제_상태코드 = 즐겨찾기_삭제_요청(토큰, Long.MAX_VALUE).statusCode();

        상태코드_400_응답(즐겨찾기_삭제_상태코드);
    }
}