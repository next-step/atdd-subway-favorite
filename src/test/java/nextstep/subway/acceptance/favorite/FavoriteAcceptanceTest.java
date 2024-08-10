package nextstep.subway.acceptance.favorite;

import static nextstep.auth.acceptance.AuthSteps.로그인_후_토큰_반환;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.common.SubwayUtils.responseToIds;
import static nextstep.subway.acceptance.common.SubwayUtils.responseToLocation;
import static nextstep.subway.acceptance.favorite.FavoriteUtils.AGE;
import static nextstep.subway.acceptance.favorite.FavoriteUtils.EMAIL;
import static nextstep.subway.acceptance.favorite.FavoriteUtils.PASSWORD;
import static nextstep.subway.acceptance.favorite.FavoriteUtils.즐겨찾기_목록조회;
import static nextstep.subway.acceptance.favorite.FavoriteUtils.즐겨찾기_삭제;
import static nextstep.subway.acceptance.favorite.FavoriteUtils.즐겨찾기_생성;
import static nextstep.subway.acceptance.line.LineUtils.삼호선;
import static nextstep.subway.acceptance.line.LineUtils.신분당선;
import static nextstep.subway.acceptance.line.LineUtils.이호선;
import static nextstep.subway.acceptance.line.LineUtils.지하철노선_생성_후_ID_반환;
import static nextstep.subway.acceptance.line.SectionUtils.지하철구간_생성;
import static nextstep.subway.acceptance.station.StationUtils.강남역;
import static nextstep.subway.acceptance.station.StationUtils.교대역;
import static nextstep.subway.acceptance.station.StationUtils.남부터미널역;
import static nextstep.subway.acceptance.station.StationUtils.양재역;
import static nextstep.subway.acceptance.station.StationUtils.지하철역_생성_후_id_추출;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역_id;
    private Long 강남역_id;
    private Long 양재역_id;
    private Long 남부터미널역_id;

    private Long 이호선_id;
    private Long 신분당선_id;
    private Long 삼호선_id;

    private String 로그인_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역_id = 지하철역_생성_후_id_추출(교대역);
        강남역_id = 지하철역_생성_후_id_추출(강남역);
        양재역_id = 지하철역_생성_후_id_추출(양재역);
        남부터미널역_id = 지하철역_생성_후_id_추출(남부터미널역);

        이호선_id = 지하철노선_생성_후_ID_반환(이호선, "green", 교대역_id, 강남역_id, 10L);
        신분당선_id = 지하철노선_생성_후_ID_반환(신분당선, "bg-red-600", 강남역_id, 양재역_id, 10L);
        삼호선_id = 지하철노선_생성_후_ID_반환(삼호선, "orange", 교대역_id, 남부터미널역_id, 2L);

        지하철구간_생성(삼호선_id, 남부터미널역_id, 양재역_id, 3L);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        로그인_토큰 = 로그인_후_토큰_반환(EMAIL, PASSWORD);
    }

    /**
     * Given 지하철 경로가 등록되어 있고
     * When 경로를 즐겨찾기에 추가하면
     * Then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(교대역_id, 양재역_id, 로그인_토큰);

        //then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(즐겨찾기_생성_응답.header("Location")).isNotNull();
    }

    /**
     * Given 중복된 즐겨찾기가 존재할 때
     * When 동일한 즐겨찾기를 생성하려고 하면
     * Then 중복 예외가 발생한다.
     */
    @DisplayName("중복된 즐겨찾기를 생성하려고 하면 예외를 발생시킨다")
    @Test
    void createDuplicateFavorite() {
        // given
        즐겨찾기_생성(교대역_id, 양재역_id, 로그인_토큰);

        // when
        ExtractableResponse<Response> 중복_즐겨찾기_생성_응답 = 즐겨찾기_생성(교대역_id, 양재역_id, 로그인_토큰);

        // then
        assertThat(중복_즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 존재하지 않는 경로로
     * When 즐겨찾기를 생성하면
     * Then 경로가 존재하지 않음 예외가 발생한다.
     */
    @DisplayName("존재하지 않는 경로로 즐겨찾기를 생성하면 실패한다.")
    @Test
    void createFavoriteWithNonExistentPath() {
        // given
        Long 존재하지_않는_역_id = 999L;

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(교대역_id, 존재하지_않는_역_id, 로그인_토큰);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 여러개의 즐겨찾기가 등록되어 있고
     * When 즐겨찾기 목록을 조회하면
     * Then 모든 즐겨찾기 목록이 조회된다.
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void showFavorites() {
        //given
        즐겨찾기_생성(교대역_id, 양재역_id, 로그인_토큰);
        즐겨찾기_생성(교대역_id, 남부터미널역_id, 로그인_토큰);

        //when
        List<Long> 즐겨찾기_목록 = responseToIds(즐겨찾기_목록조회(로그인_토큰));

        //then
        assertThat(즐겨찾기_목록.size()).isEqualTo(2);
    }

    /**
     * Given 특정 즐겨찾기가 등록되어 있고
     * When 해당 즐겨찾기를 삭제하면
     * Then 해당 즐겨찾기가 삭제되고 즐겨찾기 목록에서 제외된다.
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        //given
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(교대역_id, 양재역_id, 로그인_토큰);

        //when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제(responseToLocation(즐겨찾기_생성_응답), 로그인_토큰);

        //then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        List<Long> 즐겨찾기_목록 = responseToIds(즐겨찾기_목록조회(로그인_토큰));
        assertThat(즐겨찾기_목록.size()).isEqualTo(0);
    }

    /**
     * Given 다른 사용자의 즐겨찾기가 존재할 때
     * When 해당 즐겨찾기를 삭제하려고 하면
     * Then 권한 없음 예외가 발생한다.
     */
    @DisplayName("다른 사용자의 즐겨찾기를 삭제하려고 하면 실패한다.")
    @Test
    void deleteFavoriteOfOtherUser() {
        // given
        String otherUserEmail = "other@example.com";
        String otherUserPassword = "password";

        회원_생성_요청(otherUserEmail, otherUserPassword, AGE);
        String 다른_사용자_토큰 = 로그인_후_토큰_반환(otherUserEmail, otherUserPassword);

        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(교대역_id, 양재역_id, 다른_사용자_토큰);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제(responseToLocation(즐겨찾기_생성_응답), 로그인_토큰);

        // then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Given 즐겨찾기 데이터가 존재하지 않을 때
     * When 해당 즐겨찾기를 삭제하려고 하면
     * Then 존재하지 않는 데이터 예외가 발생한다.
     */
    @DisplayName("삭제하려는 데이터가 존재하지 않는 경우 예외를 발생시킨다")
    @Test
    void deleteNonExistentFavorite() {
        // given
        String 존재하지_않는_즐겨찾기_location = "/favorites/999";

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제(존재하지_않는_즐겨찾기_location, 로그인_토큰);

        // then
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
