package nextstep.subway.acceptance;

import io.jsonwebtoken.lang.Strings;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.FavoriteSteps.지하철_즐겨찾기_등록;
import static nextstep.subway.acceptance.FavoriteSteps.지하철_즐겨찾기_삭제;
import static nextstep.subway.acceptance.FavoriteSteps.지하철_즐겨찾기_조회;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private String 인증토큰;


    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        인증토큰 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * when 지하철 즐겨찾기를 등록한다.
     * then 정상적으로 지하철역이 생성됨을 확인한다.
     */
    @DisplayName("지하철 즐겨찾기 생성")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_등록(인증토큰, 교대역, 남부터미널역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * given 지하철 즐겨찾기를 여러개 등록한다.
     * when 지하철 즐겨찾기를 조회한다.
     * then 등록한 즐겨찾기 리스트인지 확인한다.
     */
    @DisplayName("지하철 즐겨찾기 리스트 조회")
    @Test
    void showFavorite() {
        //given
        지하철_즐겨찾기_등록(인증토큰, 교대역, 남부터미널역);
        지하철_즐겨찾기_등록(인증토큰, 강남역, 양재역);

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_조회(인증토큰);

        // then
        assertAll(() -> assertThat(response.body().as(List.class).size()).isEqualTo(2),
                () -> assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(교대역, 강남역),
                () -> assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(남부터미널역, 양재역));
    }

    /**
     * given 지하철 즐겨찾기를 여러개 등록한다.
     * when 지하철 즐겨찾기를 삭제한다.
     * then 등록한 즐겨찾기 리스트인지 확인한다.
     */
    @DisplayName("지하철 즐겨찾기 리스트 삭제")
    @Test
    void deleteFavorite() {
        //given
        지하철_즐겨찾기_등록(인증토큰, 교대역, 남부터미널역);
        지하철_즐겨찾기_등록(인증토큰, 강남역, 양재역);

        // when
        Long 삭제즐겨찾기 = 지하철_즐겨찾기_조회(인증토큰).jsonPath().getList("id", Long.class).get(1);
        지하철_즐겨찾기_삭제(인증토큰, 삭제즐겨찾기);

        // then
        assertThat(지하철_즐겨찾기_조회(인증토큰).jsonPath().getList("id", Long.class)).doesNotContain(삭제즐겨찾기);
    }

    /**
     * when 지하철 즐겨찾기 생성, 조회, 삭제를 요청한다.
     * then 401권한 없음 에러가 발생한다.
     */
//    @DisplayName("지하철 즐겨찾기 권한체크 실패")
//    @Test
//    void ExceptionFavorite() {
//        String 유효하지않는인증 = "00000GciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2Nzc0MTg3NzAsImV4cCI6MTY3NzQyMjM3MCwicm9sZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfTUVNQkVSIl19.BZD7rp2WsfSj4Lexkwgl9fs30f171tbceikLlmF9y6E";
//        var response = 지하철_즐겨찾기_등록(유효하지않는인증,교대역, 강남역);
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
//}
}
