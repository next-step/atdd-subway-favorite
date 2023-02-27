package nextstep.member.acceptance;

import static nextstep.member.acceptance.AuthAcceptanceSteps.베어러_인증_로그인_요청;
import static nextstep.member.acceptance.FavoriteAcceptanceSteps.연결되지_않은_역으로_즐겨찾기를_등록하면_예외_처리한다;
import static nextstep.member.acceptance.FavoriteAcceptanceSteps.존재하지_않은_역으로_즐겨찾기를_등록하면_예외_처리한다;
import static nextstep.member.acceptance.FavoriteAcceptanceSteps.즐겨찾기_등록_검증;
import static nextstep.member.acceptance.FavoriteAcceptanceSteps.즐겨찾기_등록_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.PathAcceptanceSteps.createSectionCreateParams;
import static nextstep.subway.acceptance.PathAcceptanceSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;

import nextstep.utils.AcceptanceTest;
import nextstep.utils.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 정자역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    @Autowired
    private DataLoader dataLoader;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재              정자역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        dataLoader.loadData();

        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        남부터미널역 = 지하철역_생성_요청("남부터미널역").jsonPath().getLong("id");
        정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");

        이호선 = 지하철_노선_생성_요청("2호선", "green", 교대역, 강남역, 10);
        신분당선 = 지하철_노선_생성_요청("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = 지하철_노선_생성_요청("3호선", "orange", 교대역, 남부터미널역, 2);

        지하철_노선에_지하철_구간_생성_요청(삼호선, createSectionCreateParams(남부터미널역, 양재역, 3));
    }

    @DisplayName("즐겨찾기 등록 관련 기능")
    @Nested
    class RegisterFavoriteTest {
        /**
         * Given 베어러 인증 로그인 후
         * When 즐겨찾기를 등록하면
         * Then 등록된 즐겨찾기를 찾을 수 있다.
         */
        @DisplayName("즐겨찾기를 등록하면 등록된 즐겨잧기를 찾을 수 있다.")
        @Test
        void registerFavorite() {
            // given
            String accessToken = 베어러_인증_토큰();

            // when
            var response = 즐겨찾기_등록_요청(accessToken, 강남역, 양재역);

            // then
            즐겨찾기_등록_검증(accessToken, response, 1);
        }

        /**
         * Given 베어러 인증 로그인 후
         * When 연결되지 않은 역으로 즐겨찾기를 등록하면
         * Then 예외 처리한다.
         */
        @DisplayName("연결되지 않은 역으로 즐겨찾기를 등록하면 예외 처리한다.")
        @Test
        void registerFavoriteNotLinkedStations() {
            // given
            String accessToken = 베어러_인증_토큰();

            // when
            var response = 즐겨찾기_등록_요청(accessToken, 강남역, 양재역);

            // then
            연결되지_않은_역으로_즐겨찾기를_등록하면_예외_처리한다(response);
        }

        /**
         * Given 베어러 인증 로그인 후
         * When 존재하지 않는 역으로 즐겨찾기를 등록하면
         * Then 예외 처리한다.
         */
        @DisplayName("존재하지 않는 역으로 즐겨찾기를 등록하면 예외 처리한다.")
        @Test
        void registerFavoriteNotExistsSourceStation() {
            // given
            long 존재하지_않는_역 = Long.MAX_VALUE;
            String accessToken = 베어러_인증_토큰();

            // when
            var response = 즐겨찾기_등록_요청(accessToken, 강남역, 존재하지_않는_역);

            // then
            존재하지_않은_역으로_즐겨찾기를_등록하면_예외_처리한다(response);
        }
    }

    @DisplayName("즐겨찾기 조회 관련 기능")
    @Nested
    class ShowFavoriteTest {

        /**
         * Given 베어러 인증 로그인 후
         * And 즐겨찾기를 등록하고
         * When 즐겨찾기 목록을 조회하면
         * Then 등록 된 즐겨찾기를 찾을 수 있다.
         */
        @Test
        void showFavorite() {

        }
    }

    @DisplayName("즐겨찾기 삭제 관련 기능")
    @Nested
    class DeleteFavoriteTest {
        /**
         * Given 베어러 인증 로그인 후
         * And 즐겨찾기를 등록하고
         * When 즐겨찾기를 삭제하면
         * Then 해당 즐겨찾기는 삭제된다.
         */
        @Test
        void deleteFavorite() {

        }

        /**
         * Given 베어러 인증 로그인 후
         * And 즐겨찾기를 등록하고
         * When 자신의 즐겨찾기 목록에 등록되지 않은 식별자로 삭제하면
         * Then 예외 처리한다.
         */
        @Test
        void deleteFavoriteNotExistsId() {

        }
    }

    @DisplayName("즐겨찾기 인증 관련 기능")
    @Nested
    class AuthFavoriteTest {
        /**
         * When 유효하지 않은 토큰으로 즐겨찾기 등록을 요청하면
         * Then 예외 처리한다.
         * When 유효하지 않은 토큰으로 즐겨찾기 조회를 요청하면
         * Then 예외 처리한다.
         * When 유효하지 않은 토큰으로 즐겨찾기 삭제를 요청하면
         * Then 예외 처리한다.
         */
        @Test
        void favoriteInvalidToken() {

        }

        /**
         * When 토큰 없이 즐겨찾기 등록을 요청하면
         * Then 예외 처리한다.
         * When 토큰 없이 즐겨찾기 조회를 요청하면
         * Then 예외 처리한다.
         * When 토큰 없이 즐겨찾기 삭제를 요청하면
         * Then 예외 처리한다.
         */
        @Test
        void favoriteWithoutToken() {

        }
    }

    private static String 베어러_인증_토큰() {
        return 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }
}
