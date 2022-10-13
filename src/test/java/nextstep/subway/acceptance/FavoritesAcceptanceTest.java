package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.support.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.support.FavoritesSteps.미로그인_즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.support.FavoritesSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.support.FavoritesSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.support.FavoritesSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.support.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 기능")
public class FavoritesAcceptanceTest extends AcceptanceTest {

    private Long 강남역;
    private Long 양재역;

    /**
     * Given 지하철역을 생성하고
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청(관리자, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(관리자, "양재역").jsonPath().getLong("id");
    }

    @DisplayName("즐겨찾기 생성")
    @Nested
    class CreateFavorite{
        /**
         * When 서로 다른 두개의 역에 대해서 즐겨찾기를 등록하면
         * Then 즐겨찾기가 등록된다.
         */
        @Test
        void success(){
            // when
            ExtractableResponse<Response> response = 즐겨찾기_생성_요청(관리자, 강남역, 양재역);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        /**
         * When 유효하지 않은 사용자가 즐겨찾기를 등록하면
         * Then 등록에 실패한다.
         */
        @Test
        void unauthorized(){
            // when
            final String notAdmin = "9999";
            ExtractableResponse<Response> response = 즐겨찾기_생성_요청(notAdmin, 강남역, 양재역);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.jsonPath().getLong("code")).isEqualTo(1002);
        }

        /**
         * When 로그인 하지 않은 사용자가 즐겨찾기 등록하면
         * Then 등록에 실패한다.
         */
        @Test
        void unauthorized_notlogin(){
            // when
            ExtractableResponse<Response> response = 미로그인_즐겨찾기_생성_요청(강남역, 양재역);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.jsonPath().getLong("code")).isEqualTo(1002);
        }

        /**
         * When 일반 회원이 즐겨찾기를 등록하면
         * Then 등록에 실패한다.
         */
        @Test
        void unauthorized_when_member(){
            // when
            ExtractableResponse<Response> response = 즐겨찾기_생성_요청(일반회원, 강남역, 양재역);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.jsonPath().getLong("code")).isEqualTo(1002);
        }
    }

    @DisplayName("즐겨찾기 조회")
    @Nested
    class GetFavorite{
        /**
         * Given 서로 다른 두개의 역에 대해서 즐겨찾기를 등록한다.
         * When 즐겨 찾기를 조회하면
         * Then 즐겨찾기 정보를 얻을 수 있다.
         */
        @Test
        void success(){
            // given
            즐겨찾기_생성_요청(관리자, 강남역, 양재역);

            // when
            ExtractableResponse<Response> response = 즐겨찾기_조회_요청(관리자);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.jsonPath().getLong("[0].id")).isNotNull();
            assertThat(response.jsonPath().getString("[0].source.name")).contains("강남역");
            assertThat(response.jsonPath().getString("[0].target.name")).contains("양재역");
        }

        /**
         * When 유효하지 않은 사용자가 즐겨찾기를 조회하면
         * Then 조회에 실패한다.
         */
        @Test
        void unauthorized(){
            // when
            final String notAdmin = "9999";
            ExtractableResponse<Response> response = 즐겨찾기_조회_요청(notAdmin);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.jsonPath().getLong("code")).isEqualTo(1002);
        }
    }

    @DisplayName("즐겨찾기 삭제")
    @Nested
    class DeleteFavorite{
        /**
         * Given 서로 다른 두개의 역에 대해서 즐겨찾기를 등록한다.
         * When 즐겨찾기를 삭제하면
         * Then 즐겨찾기 삭제가 된다.
         */
        @Test
        void success(){
            // given
            즐겨찾기_생성_요청(관리자, 강남역, 양재역);
            Long 등록된_즐겨찾기 = 즐겨찾기_조회_요청(관리자).jsonPath().getLong("[0].id");

            // when
            ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(관리자, 등록된_즐겨찾기);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        /**
         * Given 서로 다른 두개의 역에 대해서 즐겨찾기를 등록한다.
         * When 유효하지 않은 사용자가 즐겨찾기를 조회하면
         * Then 조회에 실패한다.
         */
        @Test
        void unauthorized(){
            // given
            즐겨찾기_생성_요청(관리자, 강남역, 양재역);
            Long 등록된_즐겨찾기 = 즐겨찾기_조회_요청(관리자).jsonPath().getLong("[0].id");

            // when
            final String 유효하지않은_사용자 = "9999";
            ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(유효하지않은_사용자, 등록된_즐겨찾기);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.jsonPath().getLong("code")).isEqualTo(1002);
        }
    }
}
