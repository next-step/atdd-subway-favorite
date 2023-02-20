package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.subway.fixtures.MemberFixtures.*;
import static nextstep.subway.fixtures.StationFixtures.강남;
import static nextstep.subway.fixtures.StationFixtures.판교;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setUpStations() {
        지하철역_생성_요청("강남");
        지하철역_생성_요청("양재");
        지하철역_생성_요청("판교");
    }

    // Given 로그인 요청하고
    // When 출발역과 도착역을 즐겨찾기에 추가하면
    // Then 즐겨찾기가 생성된다.
    @DisplayName("출발역과 도착역을 즐겨찾기에 추가한다")
    @Test
    void 출발역과_도착역을_즐겨찾기에_추가한다() {
        // given
        String accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 강남, 판교);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isEqualTo("/favorites/1")
        );
    }

    @DisplayName("즐겨찾기 생성 권한이 없는경우 Unauthorized으로 응답한다")
    @Test
    void 즐겨찾기_생성_권한이_없는경우_Unauthorized으로_응답한다() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(INVALID_ACCESS_TOKEN, 강남, 판교);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
