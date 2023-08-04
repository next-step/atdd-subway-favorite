package nextstep.subway.acceptance.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import nextstep.subway.dto.FavoriteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.common.CommonSteps.*;
import static nextstep.member.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.favorite.FavoriteTestUtils.*;
import static nextstep.subway.acceptance.line.LineTestUtils.*;
import static nextstep.subway.acceptance.line.LineTestUtils.지하철_구간_등록;
import static nextstep.subway.acceptance.station.StationTestUtils.*;
import static org.assertj.core.api.Assertions.*;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * 교대역 --- *2호선* --- 강남역
     * ㅣ                     ㅣ
     * *3호선*              *신분당선*
     * ㅣ                       ㅣ
     * 남부터미널역 --- *3호선* --- 양재역
     * */
    String 교대역_URL;
    String 강남역_URL;
    String 양재역_URL;
    String 남부터미널역_URL;
    String 익명역_URL;
    String 이호선_URL;
    String 신분당선_URL;
    String 삼호선_URL;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역_URL = 지하철역_생성(교대역_정보);
        강남역_URL = 지하철역_생성(강남역_정보);
        양재역_URL = 지하철역_생성(양재역_정보);
        남부터미널역_URL = 지하철역_생성(남부터미널역_정보);
        이호선_URL= 지하철_노선_생성(이호선_생성_요청, 교대역_URL, 강남역_URL, 10);
        신분당선_URL = 지하철_노선_생성(신분당선_생성_요청, 강남역_URL, 양재역_URL, 2);
        삼호선_URL = 지하철_노선_생성(삼호선_생성_요청, 교대역_URL, 남부터미널역_URL, 3);
        지하철_구간_등록(삼호선_URL, 남부터미널역_URL, 양재역_URL, 3);
    }

    @Nested
    @DisplayName("즐겨찾기 성공")
    class favoriteAPISuccess {

        String accessToken;
        String sourceStationId;
        String targetStationId;

        @BeforeEach
        void setUp() {
            // given
            회원_생성_요청(properUser.getEmail(), properUser.getPassword(), properUser.getAge());
            accessToken = 토큰_기반_로그인_요청(properUser.getEmail(), properUser.getPassword()).jsonPath().getString("accessToken");

            sourceStationId = String.valueOf(지하철_아이디_획득(교대역_URL));
            targetStationId = String.valueOf(지하철_아이디_획득(강남역_URL));
        }

        /**
         * Given 로그인 돼있는 유저가
         * When 역A에서 역B로 가는 노선을 즐겨찾기에 등록하면
         * Then 즐겨찾기 생성이 성공하고
         * And 즐겨찾기 조회 시, 등록한 즐겨찾기 내역을 확인할 수 있다.
         */
        @DisplayName("즐겨 찾기 등록 성공")
        @Test
        void createFavorite() {
            // when
            ExtractableResponse<Response> postResponse = 즐겨찾기_등록_요청(accessToken, sourceStationId, targetStationId);

            // then
            생성_요청이_성공한다(postResponse);
            ExtractableResponse<Response> getResponse = 즐겨찾기_조회_요청(accessToken);
            정상_응답을_수신받는다(getResponse);
            즐겨찾기_리스트_응답_개수를_확인한다(getResponse, 1);
        }

        /**
         * Given 로그인 돼있는 유저가
         * And 역A-역B로 가는 노선을 즐겨찾기에 등록 했을 때
         * When 즐겨찾기 조회 요청을 하면
         * Then 조회에 성공하고
         * And 등록한 즐겨찾기 내역들을 확인할 수 있다.
         */
        @DisplayName("즐겨 찾기 조회 성공")
        @Test
        void getFavorite() {
            // given
            ExtractableResponse<Response> postResponse = 즐겨찾기_등록_요청(accessToken, sourceStationId, targetStationId);
            생성_요청이_성공한다(postResponse);

            // when
            ExtractableResponse<Response> getResponse = 즐겨찾기_조회_요청(accessToken);

            // then
            정상_응답을_수신받는다(getResponse);
            즐겨찾기_등록한_정보를_응답받는다(getResponse);
        }

        /**
         * Given 로그인 돼있는 유저가
         * And 역A-역B로 가는 노선을 즐겨찾기에 등록 했을 때
         * When 해당 즐겨 찾기 id로 즐겨찾기 삭제 요청을 하면
         * Then 삭제 요청이 성공하고
         * And 즐겨찾기 조회 시, 내역을 조회할 수 없다.
         */
        @DisplayName("즐겨 찾기 삭제 성공")
        @Test
        void deleteFavorite() {
            // given
            ExtractableResponse<Response> postResponse = 즐겨찾기_등록_요청(accessToken, sourceStationId, targetStationId);
            생성_요청이_성공한다(postResponse);
            String 등록된_즐겨찾기_URL = postResponse.header("Location");

            // when
            ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, 등록된_즐겨찾기_URL);

            // then
            삭제_요청이_성공한다(deleteResponse);
            ExtractableResponse<Response> getResponse = 즐겨찾기_조회_요청(accessToken);
            정상_응답을_수신받는다(getResponse);
            즐겨찾기_리스트_응답_개수를_확인한다(getResponse, 0);
        }

        private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String favoriteLocationUrl) {
            ExtractableResponse<Response> deleteResponse = RestAssured.given().log().all()
                    .auth().preemptive().oauth2(accessToken)
                    .when()
                    .delete(favoriteLocationUrl)
                    .then()
                    .extract();
            return deleteResponse;
        }

        private void 즐겨찾기_리스트_응답_개수를_확인한다(ExtractableResponse<Response> getResponse, int count) {
            List<FavoriteResponse> list = getResponse.jsonPath().getList("$[*]", FavoriteResponse.class);
            assertThat(list).hasSize(count);
        }

        private void 즐겨찾기_등록한_정보를_응답받는다(ExtractableResponse<Response> getResponse) {
            assertThat(getResponse.jsonPath().getString("$[0]['id']")).isEqualTo('1');
            assertThat(getResponse.jsonPath().getString("$[0].source.id")).isEqualTo(sourceStationId);
            assertThat(getResponse.jsonPath().getString("$[0].target.id")).isEqualTo(targetStationId);
            assertThat(getResponse.jsonPath().getList("$[0]..name")).containsExactly(교대역_정보.get("name"), 강남역_정보.get("name"));
        }
    }

    /**
     * Given 로그인 돼있지 않은 유저가
     * When 즐겨 찾기 등록, 조회, 삭제 요청을 하면
     * Then 401 Unauthorized 응답을 받는다.
     */
    @DisplayName("미인증으로 인한 즐겨 찾기 CRD 실패")
    @Test
    void checkAuthentication() {

    }

    /**
     * Given 로그인 돼있는 유저가
     * When 경로 조회 불가한 case로 경로 등록 요청을 하면
     * Then 400 Bad request 응답을 받는다.
     */
    @DisplayName("유효하지 않은 경로로 즐겨찾기 등록 요청")
    @Test
    void createFavoriteFailedByInvalidPath() {

    }
}
