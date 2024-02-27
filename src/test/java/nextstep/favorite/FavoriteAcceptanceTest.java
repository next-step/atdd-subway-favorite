package nextstep.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.line.LineSteps;
import nextstep.station.StationSteps;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.member.acceptance.AuthSteps.토큰_생성;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 이호선;
    private Long 신분당선;
    private String accessToken;

    /**
     * Given 지하철 역과 노선을 생성하고, 회원가입을 하고, 로그인한 사용자가
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역 = 역_생성("교대역");
        강남역 = 역_생성("강남역");
        양재역 = 역_생성("양재역");
        이호선 = 노선_생성("2호선", "green", 교대역, 강남역, 10L);
        신분당선 = 노선_생성("신분당선", "red", 강남역, 양재역, 10L);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = 토큰_생성(EMAIL, PASSWORD);
    }

    /**
     * When 토큰을 가지고 즐겨찾기를 생성하면
     * Then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static Long 역_생성(String name) {
        return StationSteps.createStation(name).jsonPath().getLong("id");
    }

    private static Long 노선_생성(String name, String color, Long upStation, Long downStation, Long distance) {
        return LineSteps.createLine(name, color, upStation, downStation, distance).jsonPath().getLong("id");
    }

}