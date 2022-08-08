package nextstep.subway.acceptance;

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
    private String 다른아이디_인증토큰;
    private String 로그인_안된_토큰;

    private Long 강남역;
    private Long 양재역;

    @BeforeEach
    void setup() {
        super.setUp();
        인증_토큰 = 로그인_되어_있음("admin@email.com", "password");
        다른아이디_인증토큰 = 로그인_되어_있음("member@email.com", "password");
        로그인_안된_토큰 = 로그인_되어_있음("user@email.com", "password");

        강남역 = 지하철역_생성_요청(인증_토큰, "강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청(인증_토큰, "양재역").jsonPath().getLong("id");
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void saveFavorite() {
        즐겨찾기_저장_요청(인증_토큰, 강남역, 양재역);

        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(인증_토큰);

        저장이_되었는지_값_검증(response, 강남역, 양재역);
    }

    private void 저장이_되었는지_값_검증(ExtractableResponse<Response> response, Long source, Long target) {
        assertThat(response.jsonPath().getList("source.id")).containsExactly(강남역.intValue());
        assertThat(response.jsonPath().getList("target.id")).containsExactly(양재역.intValue());
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() {
        즐겨찾기_저장_요청(인증_토큰, 강남역, 양재역);
        즐겨찾기_삭제_요청(인증_토큰, 1L);

        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(인증_토큰);

        삭제후_값이_없는지_검증(response);
    }

    private void 삭제후_값이_없는지_검증(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getList("")).hasSize(0);
    }

    @Test
    @DisplayName("인증토큰이 로그인된 상태가 아닐때 403에러를 반환합니다.")
    void authException() {
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(로그인_안된_토큰);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
