package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_등록_한다;
import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.fake.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class FavoriteAcceptanceTest extends AcceptanceTest {

    private GithubResponses user = GithubResponses.사용자1;
    private String accessToken;

    private long 강남역;
    private long 잠실역;

    /**
     * Given 지하철역을 생성, 깃허브 인증 로그인 요청을 하고
     */
    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        accessToken = 깃허브_인증_로그인_요청(user.getCode()).jsonPath().getString("accessToken");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        잠실역 = 지하철역_생성_요청("잠실역").jsonPath().getLong("id");
    }

    /**
     * Given 지하철역을 생성을 하고
     * When 즐겨찾기 등록을 요청하면
     * Then 즐겨찾기 등록을 할 수 없다.
     **/
    @DisplayName("로그인하지_않은경우_즐겨찾기_등록_실패")
    @Test
    void 로그인하지_않은경우_즐겨찾기_등록_실패() {
        final ExtractableResponse<Response> response = 즐겨찾기_등록_한다("notLogin", 강남역, 잠실역);

        로그인하지_않은경우_즐겨찾기_등록_실패한다(response);
    }

    /**
     * Given 지하철역을 생성, 깃허브 인증 로그인 요청을 하고
     * When 즐겨찾기 등록을 요청하면
     * Then 즐겨찾기 등록이 된다.
     **/
    @DisplayName("즐겨찾기_등록")
    @Test
    void 즐겨찾기_등록() {
        final ExtractableResponse<Response> response = 즐겨찾기_등록_한다(accessToken, 강남역, 잠실역);

        즐겨찾기_등록에_성공한다(response, 강남역, 잠실역);
    }

    private void 즐겨찾기_등록에_성공한다(ExtractableResponse<Response> response, Long source, Long target) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.body().jsonPath().getLong("id")).isNotNull(),
            () -> assertThat(response.body().jsonPath().getLong("source.id")).isEqualTo(source),
            () -> assertThat(response.body().jsonPath().getLong("target.id")).isEqualTo(target)
        );
    }

    private void 로그인하지_않은경우_즐겨찾기_등록_실패한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
