package nextstep.subway.acceptance;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.member.acceptance.TokenSteps.로그인_요청;
import static nextstep.subway.acceptance.FavoriteSteps.비회원_즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.비회원_즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.비회원_즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.auth.token.TokenResponse;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 양재역;

    private String 회원_이메일 = "emai@abc.com";
    private String 회원_패스워드 = "password";

    @BeforeEach
    public void setUp() {
        교대역 = 지하철역_생성_요청("교대역").as(StationResponse.class).getId();
        양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class).getId();
        지하철_노선_생성_요청(노선_생성_요청_파라미터_생성(교대역, 양재역));

        회원_생성_요청(회원_이메일, 회원_패스워드, 20);
    }


    /**
     * Given: 로그인 토큰을 발급받는다.
     * When: 즐겨찾기를 생성한다.
     * Then: 성공(201 Created) 응답을 받는다.
     * And: Location URL로 즐겨찾기를 조회한다.
     * And: 즐겨찾기 목록을 검증한다.
     */
    @Test
    @DisplayName("회원이 즐겨찾기를 생성한다.")
    void 회원이_즐겨찾기를_생성한다() {
        // Given
        String 로그인_토큰 = 로그인_요청(회원_이메일, 회원_패스워드).as(TokenResponse.class).getAccessToken();

        // When
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인_토큰, new FavoriteRequest(교대역, 양재역));

        // Then
        HTTP_응답_상태코드_검증(즐겨찾기_생성_응답.statusCode(), HttpStatus.CREATED);

        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(로그인_토큰, 즐겨찾기_생성_응답.header("Location"));
        즐겨찾기_조회_검증(즐겨찾기_조회_응답, new SourceTarget(교대역, 양재역));
    }

    /**
     * When: 즐겨찾기를 생성한다.
     * Then: 실패(401 Unathorized) 응답을 받는다.
     */
    @Test
    @DisplayName("비회원이 즐겨찾기를 생성한다.")
    void 비회원이_즐겨찾기를_생성한다() {
        // Given
        // When
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 비회원_즐겨찾기_생성_요청(new FavoriteRequest(교대역, 양재역));

        // Then
        HTTP_응답_상태코드_검증(즐겨찾기_생성_응답.statusCode(), HttpStatus.UNAUTHORIZED);
    }


    /**
     * Given: 로그인 토큰을 발급받는다.
     * And: 즐겨찾기를 생성한다.
     * When: 즐겨찾기를 조회한다.
     * Then: 성공(200 OK) 응답을 받는다.
     * And: 즐겨찾기 목록을 검증한다.
     */
    @Test
    @DisplayName("회원이 즐겨찾기를 조회한다.")
    void 회원이_즐겨찾기를_조회한다() {
        // Given
        String 로그인_토큰 = 로그인_요청(회원_이메일, 회원_패스워드).as(TokenResponse.class).getAccessToken();
        즐겨찾기_생성_요청(로그인_토큰, new FavoriteRequest(교대역, 양재역));

        // When
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(로그인_토큰);

        // Then
        즐겨찾기_목록_조회_검증(즐겨찾기_목록_조회_응답, List.of(new SourceTarget(교대역, 양재역)));
    }


    /**
     * When: 즐겨찾기를 조회한다.
     * Then: 실패(401 Unauthorized) 응답을 받는다.
     */
    @Test
    @DisplayName("비회원이 즐겨찾기를 조회한다.")
    void 비회원이_즐겨찾기를_조회한다() {
        // Given
        // When
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 비회원_즐겨찾기_목록_조회_요청();

        // Then
        HTTP_응답_상태코드_검증(즐겨찾기_목록_조회_응답.statusCode(), HttpStatus.UNAUTHORIZED);
    }


    /**
     * Given: 로그인 토큰을 발급받는다.
     * And: 즐겨찾기를 생성한다.
     * And: Location URL로 즐겨찾기를 삭제한다.
     * Then: 성공(204 No Content) 응답을 받는다.
     */
    @Test
    @DisplayName("회원이 즐겨찾기를 삭제한다.")
    void 회원이_즐겨찾기를_삭제한다() {
        // Given
        String 로그인_토큰 = 로그인_요청(회원_이메일, 회원_패스워드).as(TokenResponse.class).getAccessToken();
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(로그인_토큰, new FavoriteRequest(교대역, 양재역));

        // When
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(로그인_토큰, 즐겨찾기_생성_응답.header("Location"));

        // Then
        HTTP_응답_상태코드_검증(즐겨찾기_삭제_응답.statusCode(), HttpStatus.NO_CONTENT);
    }


    /**
     * When: 즐겨찾기를 삭제한다.
     * Then: 실패(401 Unauthorized) 응답을 받는다.
     */
    @Test
    @DisplayName("비회원이 즐겨찾기를 삭제한다.")
    void 비회원이_즐겨찾기를_삭제한다() {
        // Given
        // When
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 비회원_즐겨찾기_삭제_요청("/favorites/1");

        // Then
        HTTP_응답_상태코드_검증(즐겨찾기_삭제_응답.statusCode(), HttpStatus.UNAUTHORIZED);
    }


    private Map<String, String> 노선_생성_요청_파라미터_생성(Long upStationId, Long downStationId) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "신분당선");
        lineCreateParams.put("color", "bg-orange-600");
        lineCreateParams.put("upStationId", upStationId + "");
        lineCreateParams.put("downStationId", downStationId + "");
        lineCreateParams.put("distance", 10 + "");
        return lineCreateParams;
    }

    private void 즐겨찾기_조회_검증(ExtractableResponse<Response> response, SourceTarget sourceTarget) {
        HTTP_응답_상태코드_검증(response.statusCode(), HttpStatus.OK);

        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);

        assertThat(favoriteResponse.getSource().getId()).isEqualTo(sourceTarget.source);
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(sourceTarget.target);
    }

    private void 즐겨찾기_목록_조회_검증(ExtractableResponse<Response> response, List<SourceTarget> sourceTargets) {
        HTTP_응답_상태코드_검증(response.statusCode(), HttpStatus.OK);

        List<FavoriteResponse> favoriteResponseList = response.jsonPath()
            .getList("", FavoriteResponse.class);

        int totalAssertionSourceTarget = sourceTargets.size();
        int matchSourceTarget = 0;
        for (FavoriteResponse favoriteResponse : favoriteResponseList) {
            for (SourceTarget sourceTarget : sourceTargets) {
                if (sourceTarget.source.equals(favoriteResponse.getSource().getId())
                && sourceTarget.target.equals(favoriteResponse.getTarget().getId())) {
                    matchSourceTarget++;
                }
            }
        }
        assertThat(matchSourceTarget).isEqualTo(totalAssertionSourceTarget);
    }

    void HTTP_응답_상태코드_검증(int actualStatus, HttpStatus expectedStatus) {
        assertThat(actualStatus).isEqualTo(expectedStatus.value());
    }

    static class SourceTarget {
        Long source;
        Long target;

        public SourceTarget(Long source, Long target) {
            this.source = source;
            this.target = target;
        }
    }

}
