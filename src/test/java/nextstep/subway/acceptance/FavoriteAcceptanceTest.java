package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.로그인_안하고_즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_저장_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private String 인증_토큰;

    private Long 강남역;
    private Long 양재역;

    @BeforeEach
    void setup() {
        super.setUp();
        인증_토큰 = 로그인_되어_있음("admin@email.com", "password");

        강남역 = 지하철역_생성_요청(인증_토큰, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(인증_토큰, "양재역").jsonPath().getLong("id");
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void saveFavorite() {
        즐겨찾기_저장_요청(인증_토큰, 강남역, 양재역);

        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(인증_토큰);

        저장이_되었는지_값_검증(인증_토큰, 강남역, 양재역);
    }

    private void 저장이_되었는지_값_검증(String token, Long source, Long target) {
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(token);

        assertThat(response.jsonPath().getList("source.id")).containsExactly(source.intValue());
        assertThat(response.jsonPath().getList("target.id")).containsExactly(target.intValue());
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() {
        ExtractableResponse<Response> 즐겨찾기_생성 = 즐겨찾기_저장_요청(인증_토큰, 강남역, 양재역);
        즐겨찾기_삭제_요청(인증_토큰, 즐겨찾기_생성);

        삭제후_값이_없는지_검증(인증_토큰);
    }

    /**
     * Given 로그인 후, 인증 토큰을 받습니다.
     * When 즐겨찾기 저장을 합니다.
     * Then 즐격찾기 조회하여, 저장이 되었는지 확인합니다.
     *
     * When 즐겨찾기 삭제합니다.
     * Then 즐겨찾기 조회하여, 삭제되었는지 확인합니다.
     *
     */
    @Test
    @DisplayName("로그인한 상테에서 즐겨찾기 관리하기")
    void manageFavorite() {
        ExtractableResponse<Response> 저장 = 즐겨찾기_저장_요청(인증_토큰, 강남역, 양재역);
        저장이_되었는지_값_검증(인증_토큰, 강남역, 양재역);

        즐겨찾기_삭제_요청(인증_토큰, 저장);

        삭제후_값이_없는지_검증(인증_토큰);
    }


    private void 삭제후_값이_없는지_검증(String token) {
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(token);
        assertThat(response.jsonPath().getList("")).hasSize(0);
    }

    @Test
    @DisplayName("유효하지 않는 토큰일 경우 401에러를 반환합니다.")
    void authException() {
        ExtractableResponse<Response> response = 로그인_안하고_즐겨찾기_조회_요청();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
