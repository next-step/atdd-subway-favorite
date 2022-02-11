package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.StationSteps;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.step.FavoriteSteps.*;

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
     * Given 인증을 한 뒤
     * When 즐겨찾기 생성을 요청하면
     * Then 생성 응답을 받는다
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        // given
        String 토큰 = 로그인_되어_있음(MAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(토큰, 강남역.getId(), 판교역.getId());

        // then
        즐겨찾기_생성_응답됨(response);
    }

    /**
     * When 로그인 없이 즐겨찾기 생성을 요청하면
     * Then 권한 없음을 응답받는다
     */
    @DisplayName("즐겨찾기 생성 - 인증 없이 요청하면 실패한다")
    @Test
    void createFavorite_fail() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청("", 강남역.getId(), 판교역.getId());

        // then
        권한_없음_응답됨(response);
    }

    /**
     * Given 인증을 한 뒤
     * When 없는 역을 즐겨찾기 생성 요청하면
     * Then 잘못된 요청 응답을 받는다
     */
    @DisplayName("즐겨찾기 생성 - 없는 역을 요청하면 실패한다")
    @Test
    void createFavorite_notFound_fail() {
        // given
        String 토큰 = 로그인_되어_있음(MAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(토큰, 강남역.getId(), 1000);

        // then
        잘못된_요청_응답됨(response);
    }

    /**
     * Given 인증을 한 뒤
     * When 즐겨찾기 조회을 요청하면
     * Then 성공 응답을 받는다
     */
    @DisplayName("즐겨찾기 조회")
    @Test
    void findFavorite() {
        // given
        String 토큰 = 로그인_되어_있음(MAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(토큰);

        // then
        즐겨찾기_조회_응답됨(response);
    }

    /**
     * When 로그인 없이 즐겨찾기 조회을 요청하면
     * Then 권한 없음을 응답받는다
     */
    @DisplayName("즐겨찾기 조회 - 인증 없이 조회하면 실패한다")
    @Test
    void findFavorite_fail() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청("");

        // then
        권한_없음_응답됨(response);
    }

    /**
     * Given 인증을 한 뒤
     * When 즐겨찾기 삭제를 요청하면
     * Then 성공 응답을 받는다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        String 토큰 = 로그인_되어_있음(MAIL, PASSWORD);
        List<FavoriteResponse> 조회 = 즐겨찾기_생성_조회(토큰, 강남역.getId(), 판교역.getId());
        Long 즐겨찾기ID = 조회.get(0).getId();

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(토큰, 즐겨찾기ID);

        // then
        즐겨찾기_삭제_응답됨(response);
    }

    /**
     * When 인증 없이 즐겨찾기 삭제를 요청하면
     * Then 권한 없음을 응답받는다
     */
    @DisplayName("즐겨찾기 삭제 - 인증 없이 요청하면 실패")
    @Test
    void deleteFavorite_authorize_fail() {
        // given
        String 토큰 = "";

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(토큰, 1L);

        // then
        권한_없음_응답됨(response);
    }

    /**
     * Given 인증을 한 뒤
     * Given 즐겨 찾기를 생성하고
     * When 없는 즐겨찾기 id를 삭제 요청하면
     * Then 잘못된 요청 응답을 받는다
     */
    @DisplayName("즐겨찾기 삭제 - 존재하는 즐겨찾기만 삭제 가능하다")
    @Test
    void deleteFavorite_notfound_fail() {
        // given
        String 토큰 = 로그인_되어_있음(MAIL, PASSWORD);
        즐겨찾기_생성_조회(토큰, 강남역.getId(), 판교역.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(토큰, 10000L);

        // then
        잘못된_요청_응답됨(response);
    }

    /**
     * Scenario: 즐겨찾기를 관리
     * Given 인증 요청
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기 관리")
    @Test
    void manage_favorite() {
        // given
        String 토큰 = 로그인_되어_있음(MAIL, PASSWORD);
        // when
        ExtractableResponse<Response> 생성응답 = 즐겨찾기_생성_요청(토큰, 강남역.getId(), 판교역.getId());
        // then
        즐겨찾기_생성_응답됨(생성응답);

        // when
        ExtractableResponse<Response> 조회응답 = 즐겨찾기_조회_요청(토큰);
        // then
        즐겨찾기_조회_응답됨(조회응답);

        // given
        List<FavoriteResponse> 조회 = 즐겨찾기_생성_조회(토큰, 강남역.getId(), 판교역.getId());
        Long 즐겨찾기ID = 조회.get(0).getId();
        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(토큰, 즐겨찾기ID);
        // then
        즐겨찾기_삭제_응답됨(response);
    }

}
