package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청한다;
import static nextstep.subway.utils.RestAssuredCRUD.응답결과가_BAD_REQUEST;
import static nextstep.subway.utils.RestAssuredCRUD.응답결과가_CREATED;
import static nextstep.subway.utils.RestAssuredCRUD.응답결과가_NO_CONTENT;
import static nextstep.subway.utils.RestAssuredCRUD.응답결과가_OK;
import static nextstep.subway.utils.RestAssuredCRUD.응답결과가_UNAUTHORIZED;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.dto.FavoriteRequest;
import nextstep.favorite.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.utils.RestAssuredCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 기능 인수테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private String accessToken;

    private ExtractableResponse<Response> response;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남강역;
    private StationResponse 대교역;
    private StationResponse 널미터부남역;
    private StationResponse 남부터미널역;
    private Long 없는Id = Long.MAX_VALUE;

    /**
     * 교대역    --- *2호선* ---     강남역 --- *2호선* ---   대교역
     * |            30                |         11
     * |                         *신분당선* 17
     * |                              |
     * |                            남강역
     * |                              |
     * *3호선* 43                 *신분당선* 10
     * |                              |
     * 남부터미널역  --- *3호선* ---   양재   --- *3호선* --- 널미터부남역
     *                  2                       21
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        given_노선이_만들어져_있다: {
            강남역 = 지하철역_생성_요청한다(StationRequest.of("강남역")).as(StationResponse.class);
            양재역 = 지하철역_생성_요청한다(StationRequest.of("양재역")).as(StationResponse.class);
            교대역 = 지하철역_생성_요청한다(StationRequest.of("교대역")).as(StationResponse.class);
            남강역 = 지하철역_생성_요청한다(StationRequest.of("남강역")).as(StationResponse.class);
            대교역 = 지하철역_생성_요청한다(StationRequest.of("대교역")).as(StationResponse.class);
            널미터부남역 = 지하철역_생성_요청한다(StationRequest.of("널미터부남역")).as(StationResponse.class);
            남부터미널역 = 지하철역_생성_요청한다(StationRequest.of("남부터미널역")).as(StationResponse.class);

            신분당선 = 지하철_노선_생성_요청(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 27)).as(
                LineResponse.class);
            이호선 = 지하철_노선_생성_요청(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30)).as(LineResponse.class);
            삼호선 = 지하철_노선_생성_요청(LineRequest.of("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

            지하철_노선에_지하철_구간_생성_요청(삼호선.getId(), SectionRequest.of(교대역.getId(), 남부터미널역.getId(), 43));
            지하철_노선에_지하철_구간_생성_요청(신분당선.getId(), SectionRequest.of(강남역.getId(), 남강역.getId(), 17));
            지하철_노선에_지하철_구간_생성_요청(이호선.getId(), SectionRequest.of(강남역.getId(), 대교역.getId(), 11));
            지하철_노선에_지하철_구간_생성_요청(삼호선.getId(), SectionRequest.of(양재역.getId(), 널미터부남역.getId(), 21));
        }

        and_회원으로_로그인_되어있다: {
            회원_생성_요청(EMAIL, PASSWORD, AGE);
            accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        }
    }

    @DisplayName("즐겨찾기 관련 기능을 확인한다")
    @Test
    void 즐겨찾기_통합인수테스트() {
        response = 즐겨찾기를_생성한다(FavoriteRequest.of(널미터부남역.getId(), 대교역.getId()));
        응답결과가_CREATED(response);
        response = 즐겨찾기를_생성한다(FavoriteRequest.of(남강역.getId(), 남부터미널역.getId()));
        응답결과가_CREATED(response);
        Long secondFavoriteId = response.as(FavoriteResponse.class).getId();

        response = 즐겨찾기_목록을_조회한다();
        즐겨찾기_목록이_조회된다(response);

        response = 즐겨찾기_삭제한다(secondFavoriteId);
        즐겨찾기_목록이_삭제된다(response);

        response = 즐겨찾기_목록을_조회한다();
        즐겨찾기_목록이_조회된다(response);
    }

    @DisplayName("로그인 없이 즐겨찾기를 생성하면 실패한다.")
    @Test
    void 즐겨찾기_생성_예외테스트_1() {
        로그인이_안되어있다();

        response = 즐겨찾기를_생성한다(FavoriteRequest.of(강남역.getId(), 남부터미널역.getId()));

        응답결과가_UNAUTHORIZED(response);
    }

    @DisplayName("없는 역으로 즐겨찾기를 생성하면 실패한다.")
    @Test
    void 즐겨찾기_생성_예외테스트_2() {
        response = 즐겨찾기를_생성한다(FavoriteRequest.of(없는Id, 남부터미널역.getId()));

        응답결과가_BAD_REQUEST(response);
    }

    @DisplayName("로그인 없이 즐겨찾기를 조회하면 실패한다.")
    @Test
    void 즐겨찾기_조회_예외테스트_1() {
        로그인이_안되어있다();

        response = 즐겨찾기_목록을_조회한다();

        응답결과가_UNAUTHORIZED(response);
    }

    @DisplayName("로그인 없이 즐겨찾기를 삭제하면 실패한다.")
    @Test
    void 즐겨찾기_삭제_예외테스트_1() {
        로그인이_안되어있다();

        response = 즐겨찾기_삭제한다(1L);

        응답결과가_UNAUTHORIZED(response);
    }

    @DisplayName("없는 즐겨찾기를 삭제하면 실패한다.")
    @Test
    void 즐겨찾기_삭제_예외테스트_2() {
        response = 즐겨찾기_삭제한다(없는Id);

        응답결과가_UNAUTHORIZED(response);
    }

    private ExtractableResponse<Response> 즐겨찾기를_생성한다(FavoriteRequest request) {
        return RestAssuredCRUD.postRequestWithOAuth("/favorites", request, accessToken);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록을_조회한다() {
        return RestAssuredCRUD.getWithOAuth("/favorites", accessToken);
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제한다(Long id) {
        return RestAssuredCRUD.deleteWithOAuth("/favorites/"+id, accessToken);
    }

    private void 즐겨찾기_목록이_조회된다(ExtractableResponse<Response> response) {
        응답결과가_OK(response);
    }

    private void 즐겨찾기_목록이_삭제된다(ExtractableResponse<Response> response) {
        응답결과가_NO_CONTENT(response);
    }

    private void 로그인이_안되어있다() {
        accessToken = "no";
    }
}
