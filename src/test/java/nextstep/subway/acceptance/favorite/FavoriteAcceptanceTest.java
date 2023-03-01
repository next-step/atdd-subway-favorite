package nextstep.subway.acceptance.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.StationSteps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.*;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기 생성")
    @Test
    void test() {
        // given
        long 신논현역 = getStationId(지하철역_생성_요청("신논현역"));
        long 강남역 = getStationId(지하철역_생성_요청("강남역"));
        long 양재역 = getStationId(지하철역_생성_요청("양재역"));

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성(String.valueOf(신논현역), String.valueOf(양재역));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private long getStationId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
