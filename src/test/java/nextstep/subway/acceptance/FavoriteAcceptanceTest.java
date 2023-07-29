package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.marker.AcceptanceTest;
import nextstep.utils.AcceptanceTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@AcceptanceTest
public class FavoriteAcceptanceTest extends FavoriteAcceptanceTestHelper {


    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 이호선;
    private Long 삼호선;

    private String 토큰;
    private String 다른_토큰;

    private String 회원;
    private String 다른_회원;


    /**
     * 교대역    --- *2호선* ---   강남역
     * <p>
     * <p>
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        교대역 = AcceptanceTestUtils.getId(createStation("교대역"));
        강남역 = AcceptanceTestUtils.getId(createStation("강남역"));
        양재역 = AcceptanceTestUtils.getId(createStation("양재역"));
        남부터미널역 = AcceptanceTestUtils.getId(createStation("남부터미널역"));

        이호선 = AcceptanceTestUtils.getId(createLines("2호선", "green", 교대역, 강남역, 10L));
        삼호선 = AcceptanceTestUtils.getId(createLines("3호선", "orange", 남부터미널역, 양재역, 2L));

        회원 = AcceptanceTestUtils.getResource(AcceptanceTestUtils.getLocation(createMember("email", "password", 20))).extract().jsonPath().getString("email");
        다른_회원 = AcceptanceTestUtils.getResource(AcceptanceTestUtils.getLocation(createMember("other-email", "password", 20))).extract().jsonPath().getString("email");

        토큰 = getToken("email", "password").extract().jsonPath().getString("accessToken");
        다른_토큰 = getToken("other-email", "password").extract().jsonPath().getString("accessToken");
    }

    @Nested
    class Success {

        /**
         * 즐겨찾기 생성
         * Given 지하철 노선을 생성하고
         * And 토큰을 헤더에 담아
         * When 즐겨찾기를 생성하면
         * Then 다시 조회 했을 때 즐겨찾기 정보가 응답된다.
         */
        @Test
        void 즐겨찾기_생성() {
            //when
            ValidatableResponse favoriteCreatedResponse = createFavorite(교대역, 강남역, 토큰);

            //then
            AcceptanceTestUtils.verifyResponseStatus(favoriteCreatedResponse, HttpStatus.CREATED);

            ValidatableResponse foundFavoriteResponse = getFavorite(AcceptanceTestUtils.getId(favoriteCreatedResponse), 토큰);
            verifyFoundFavorite(foundFavoriteResponse, "교대역", "강남역");
        }

        /**
         * 즐겨찾기 목록 조회
         * Given 지하철 노선을 생성하고
         * And 즐겨찾기를 생성하고
         * And 토큰을 헤더에 담아
         * When 즐겨찾기를 조회하면
         * Then 즐겨찾기 목록 정보가 응답된다.
         */
        @Test
        void 즐겨찾기_목록_조회() {
            // given
            ValidatableResponse favoriteCreatedResponse = createFavorite(교대역, 강남역, 토큰);
            AcceptanceTestUtils.verifyResponseStatus(favoriteCreatedResponse, HttpStatus.CREATED);

            //when
            ValidatableResponse foundFavoriteResponse = getFavorites(토큰);

            //then
            verifyFoundFavorites(foundFavoriteResponse, "교대역", "강남역");
        }

        /**
         * 즐겨찾기 조회
         * Given 지하철 노선을 생성하고
         * And 즐겨찾기를 생성하고
         * And 토큰을 헤더에 담아
         * When 즐겨찾기를 조회하면
         * Then 즐겨찾기 정보가 응답된다.
         */
        @Test
        void 즐겨찾기_조회() {
            // given
            ValidatableResponse favoriteCreatedResponse = createFavorite(교대역, 강남역, 토큰);

            //when
            ValidatableResponse foundFavoriteResponse = getFavorite(AcceptanceTestUtils.getId(favoriteCreatedResponse), 토큰);

            //then
            verifyFoundFavorite(foundFavoriteResponse, "교대역", "강남역");
        }

        /**
         * 즐겨찾기 삭제
         * Given 지하철 노선을 생성하고
         * And 즐겨찾기를 생성하고
         * And 토큰을 헤더에 담아
         * When 즐겨찾기를 삭제하면
         * Then 다시 조회 했을 때 즐겨찾기 정보를 찾지 못한다.
         */
        @Test
        void 즐겨찾기_삭제() {
            // given
            ValidatableResponse favoriteCreatedResponse = createFavorite(교대역, 강남역, 토큰);

            //when
            ValidatableResponse deleteFavoriteResponse = deleteFavorite(AcceptanceTestUtils.getId(favoriteCreatedResponse), 토큰);
            AcceptanceTestUtils.verifyResponseStatus(deleteFavoriteResponse, HttpStatus.NO_CONTENT);

            //then
            ValidatableResponse foundFavoriteResponse = getFavorite(AcceptanceTestUtils.getId(favoriteCreatedResponse), 토큰);
            AcceptanceTestUtils.verifyResponseStatus(foundFavoriteResponse, HttpStatus.NOT_FOUND);
        }
    }


    @Nested
    class Fail {

        /**
         * 즐겨찾기 조회 실패
         * Given 지하철 노선을 생성하고
         * And 즐겨찾기를 생성하고
         * When 자신의 즐겨찾기가 아닌 것을 조회하면
         * Then 권한이 없어 실패한다.
         */
        @Test
        void 즐겨찾기를_등록한_회원이_아니면_응답을_받지_못한다() {
            //given
            ValidatableResponse favoriteCreatedResponse = createFavorite(교대역, 강남역, 토큰);

            //when
            ValidatableResponse foundFavoriteResponse = getFavorite(AcceptanceTestUtils.getId(favoriteCreatedResponse), 다른_토큰);

            //then
            AcceptanceTestUtils.verifyResponseStatus(foundFavoriteResponse, HttpStatus.FORBIDDEN);
        }

        /**
         * 없는 역 즐겨찾기 등록 실패
         * And 토큰을 헤더에 담아
         * When 없는 지하철 역을 등록하려 하면
         * Then 등록에 실패한다.
         */
        @Test
        void 등록된_역이_아니면_등록에_실패한다() {
            //when
            long unRegisteredStationId = 100L;
            ValidatableResponse favoriteCreatedResponse = createFavorite(unRegisteredStationId, 강남역, 토큰);

            //then
            AcceptanceTestUtils.verifyResponseStatus(favoriteCreatedResponse, HttpStatus.NOT_FOUND);
        }

        /**
         * 연결되지 않은 노선 즐겨찾기 등록 실패
         * Given 지하철 노선을 등록하고
         * And 토큰을 헤더에 담아
         * When 연결되지 않은 노선의 역들을 즐겨찾기로 등록하려 하면
         * Then 등록에 실패한다.
         */
        @Test
        void 연결되지_않은_경로이면_등록에_실패한다() {
            //when
            ValidatableResponse favoriteCreatedResponse = createFavorite(남부터미널역, 강남역, 토큰);

            //then
            AcceptanceTestUtils.verifyResponseStatus(favoriteCreatedResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
