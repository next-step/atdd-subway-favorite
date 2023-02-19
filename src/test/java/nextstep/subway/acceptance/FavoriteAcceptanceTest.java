package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "member@email.com";
    private static final String PASSWORD = "password";

    private Long 수서역;
    private Long 복정역;
    private String accessToken;

    /**
     * 수서역 -----<분당선>----- 복정역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        수서역 = 지하철역_생성_요청("수서역").jsonPath().getLong("id");
        복정역 = 지하철역_생성_요청("복정역").jsonPath().getLong("id");

        지하철_노선_생성_요청("분당선", "yellow", 수서역, 복정역, 20);

        accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * When 출발역과 도착역으로 즐겨찾기 구간 생성을 요청하면
     * Then 해당 구간이 즐겨찾기 구간으로 생성된다.
     */
    @DisplayName("즐겨찾기 구간을 생성한다.")
    @Test
    void createFavoriteSection() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_구간_생성_요청(accessToken, 수서역, 복정역);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.header("Location")).isEqualTo("/favorites/1")
        );
    }

    /**
     * When 액세스 토큰 없이 출발역과 도착역으로 즐겨찾기 구간 생성을 요청하면
     * Then 해당 구간이 즐겨찾기 구간으로 생성되지 않는다.
     */
    @DisplayName("즐겨찾기 구간 생성 요청 시, 액세스 토큰이 없으면 즐겨찾기 구간이 생성되지 않는다.")
    @Test
    void createFavoriteSectionWithoutAccessToken() {
        // when
        ExtractableResponse<Response> response = 인증_없이_즐겨찾기_구간_생성_요청(수서역, 복정역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 새로운 즐겨찾기 구간 추가를 요청하고
     * When 유효한 인증 토큰과 함께 즐겨찾기 구간 조회를 요청하면
     * Then 즐겨찾기 구간 목록을 응답받을 수 있다.
     */
    @DisplayName("즐겨찾기 구간 목록을 조회한다.")
    @Test
    void findFavoriteSections() {
        // given
        즐겨찾기_구간_생성_요청(accessToken, 수서역, 복정역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_구간_목록_조회_요청(accessToken);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(수서역),
            () -> assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(복정역)
        );
    }
}
