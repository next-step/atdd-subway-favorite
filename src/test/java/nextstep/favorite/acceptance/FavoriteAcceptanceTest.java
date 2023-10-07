package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.StationSteps;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    Long 역삼역_ID;
    Long 선릉역_ID;

    /**
     * Given 역이 생성되어 있다.
     */
    @BeforeEach
    public void setUp() {
        역삼역_ID = 역_생성_ID_추출(지하철역_생성_요청("역삼역"));
        선릉역_ID = 역_생성_ID_추출(지하철역_생성_요청("선릉역"));
    }

    /**
     * When 즐겨찾기 시작역과 도착역을 등록한다.
     * Then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorites() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(역삼역_ID, 선릉역_ID);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/favorites/1");
    }
}
