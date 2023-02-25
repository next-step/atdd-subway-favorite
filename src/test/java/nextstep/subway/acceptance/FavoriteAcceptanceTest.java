package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.SectionAcceptanceTest.createLineCreateParams;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.favorite.ui.response.FavoriteResponses;
import nextstep.member.ui.response.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private Long 신분당선;

    private Long 강남역;
    private Long 양재역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        Map<String, String> lineCreateParams = createLineCreateParams(강남역, 양재역);
        신분당선 = 지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    @Test
    void 즐겨찾기_등록_회원인_경우() {
        // Given 회원 등록
        String token = 회원_생성_및_토큰_요청(EMAIL, PASSWORD);

        // When 즐겨찾기 등록 요청
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(token, 강남역.toString(), 양재역.toString());

        // Then 즐겨찾기 등록됨
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(즐겨찾기_생성_응답.header("Location")).isNotBlank();
    }

    @Test
    void 즐겨찾기_조회_회원인_경우() {
        // Given 회원 등록
        String token = 회원_생성_및_토큰_요청(EMAIL, PASSWORD);
        즐겨찾기_생성_요청(token, 강남역.toString(), 양재역.toString());

        // When 즐겨찾기 조회 요청
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(token);

        // Then 즐겨찾기 조회됨
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        FavoriteResponses response = 즐겨찾기_조회_응답.as(FavoriteResponses.class);
        assertThat(response.getFavorites()).hasSize(1);
        assertThat(response.getFavorites().stream().map(
            f -> f.getSource().getId()
        ).collect(Collectors.toList())).containsExactly(강남역);
        assertThat(response.getFavorites().stream().map(
            f -> f.getTarget().getId()
        ).collect(Collectors.toList())).containsExactly(양재역);
    }

    @Test
    void 즐겨찾기_제거_회원인_경우() {
        // Given 회원 등록
        String token = 회원_생성_및_토큰_요청(EMAIL, PASSWORD);
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(token, 강남역.toString(), 양재역.toString());
        String favoriteId = 즐겨찾기_생성_응답.header("Location").split("/")[2];

        // When 즐겨찾기 제거 요청
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(token, favoriteId);

        // Then 즐겨찾기 제거됨
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 즐겨찾기_등록_회원이_아닌_경우() {
        // Given 즐겨찾기 등록 요청
        String token = "aaa";
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(token, 강남역.toString(), 양재역.toString());

        // When 회원이 아닌 경우

        // Then UnAuthorized 예외 발생
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 즐겨찾기_조회_회원이_아닌_경우() {
        // Given 즐겨찾기 조회 요청
        String token = "aaa";
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회_요청(token);

        // When 회원이 아닌 경우

        // Then UnAuthorized 예외 발생
        assertThat(즐겨찾기_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 즐겨찾기_제거_회원이_아닌_경우() {
        // Given 즐겨찾기 제거 요청
        String token = "aaa";
        String favoriteId = "1";

        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(token, favoriteId);

        // When 회원이 아닌 경우

        // Then UnAuthorized 예외 발생
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private static String 회원_생성_및_토큰_요청(String email, String password) {
        회원_생성_요청(email, password, 20);
        ExtractableResponse<Response> 토큰_응답 = 베어러_인증_로그인_요청(email, password);
        return 토큰_응답.as(TokenResponse.class).getAccessToken();
    }
}
