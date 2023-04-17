package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_추가_요청;
import static nextstep.subway.acceptance.MemberSteps.깃헙_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 양재역;
    private Long 판교역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();
        GithubResponses user = GithubResponses.사용자1;
        accessToken = 깃헙_로그인_요청(user.getCode()).jsonPath().getString("accessToken");

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        판교역 = 지하철역_생성_요청("판교역").jsonPath().getLong("id");
    }

    /**
     * Given
     *      1) 지하철 역을 생성한다.
     *      2) 깃허브 로그인을 한다.
     *
     * When 즐겨찾기에 추가할 지하철역 2개(출발역, 도착역)를 넣어 즐겨찾기를 생성한다.
     *
     * Then 즐겨찾기를 조회할 수 있다.
     *
     */
    @Test
    @DisplayName("즐겨찾기를 추가에 성공한다.")
    void addFavorite() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_추가_요청(accessToken, 강남역, 판교역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 유효하지 않은 accessToken이 생성된다.
     *
     * When 즐겨찾기에 추가 API를 호출한다.
     *
     * Then 401 error 발생한다.
     *
     */
    @EmptySource
    @ValueSource(strings = {"abc", "123"})
    @ParameterizedTest
    @DisplayName("즐겨찾기를 추가시 잘못된 엑세스 토큰으로 API를 호출하면 401 ERROR가 발생한다.")
    void addFavoriteByInvalidUser(String invalidToken) {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_추가_요청(invalidToken, 강남역, 판교역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
