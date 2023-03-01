package nextstep.subway.acceptance.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.MemberSteps;
import nextstep.subway.acceptance.StationSteps;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    private long 신논현역;
    private long 강남역;
    private long 양재역;

    private String email;
    private String password;

    @BeforeEach
    public void setUp() {
        신논현역 = getStationId(지하철역_생성_요청("신논현역"));
        강남역 = getStationId(지하철역_생성_요청("강남역"));
        양재역 = getStationId(지하철역_생성_요청("양재역"));
        MemberSteps.회원_생성_요청(email, password, 20);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        // given
        ExtractableResponse<Response> tokenResponse = MemberSteps.베어러_인증_로그인_요청(email, password);
        String accessToken = tokenResponse.jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성(String.valueOf(신논현역), String.valueOf(양재역), accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void getFavorites() {
        // given
        ExtractableResponse<Response> tokenResponse = MemberSteps.베어러_인증_로그인_요청(email, password);
        String accessToken = tokenResponse.jsonPath().getString("accessToken");

        FavoriteSteps.즐겨찾기_생성(String.valueOf(신논현역), String.valueOf(양재역), accessToken);
        FavoriteSteps.즐겨찾기_생성(String.valueOf(강남역), String.valueOf(양재역), accessToken);

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_조회(accessToken);

        // then
        즐겨찾기_조회_검증(response);
    }

    private static void 즐겨찾기_조회_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("source.name", String.class))
                .contains("신논현역", "강남역");
        assertThat(response.jsonPath().getList("target.name", String.class))
                .contains("양재역", "양재역");
    }

    private long getStationId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
