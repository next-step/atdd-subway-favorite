package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.*;
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

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private Long 신논현역;
    private Long 강남역;
    private Long 양재역;
    private String accessToken;

    @BeforeEach
    void setup() {
        신논현역 = 지하철역_생성_요청("신논현역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("로그인 후 즐겨찾기 추가")
    void addFavorite() {
        ExtractableResponse<Response> response = 즐겨찾기_추가(accessToken, 신논현역, 강남역);

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.header("Location")).isNotBlank()
        );

    }

    @Test
    @DisplayName("로그인 없이 즐겨찾기 추가할 경우 에러")
    void addFavoriteWithoutLogin() {
        accessToken = "";
        assertThat(즐겨찾기_추가(accessToken, 신논현역, 강남역).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("로그인 후 즐겨찾기 조회")
    void findAllFavorites() {
        // given
        즐겨찾기_추가(accessToken, 신논현역, 강남역);
        즐겨찾기_추가(accessToken, 강남역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.jsonPath().getList("source.name")).containsOnly("신논현역", "강남역"),
            () -> assertThat(response.jsonPath().getList("target.name")).containsOnly("강남역", "양재역")
        );
    }

    @Test
    @DisplayName("로그인 없이 즐겨찾기 조회할 경우 에러")
    void findAllFavoritesWithoutLogin() {
        accessToken = "";

        // then
        assertThat(즐겨찾기_조회(accessToken).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
