package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.StationRequest;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.handler.subway.StationHandler.지하철_역_요청;

public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 역을 생성하면
     * Then 지하철 역이 생성된다.
     */
    @DisplayName("지하철 역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> 교대역 = 지하철_역_요청(new StationRequest("교대역"));

        // then
        Assertions.assertThat(교대역.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * When 3개의 지하철 역을 생성하면
     * Then 3개의 지하철 역이 생성된다.
     */
    @DisplayName("3개의 지하철 역을 생성한다.")
    @Test
    void createThreeStation() {
        // when
        ExtractableResponse<Response> 교대역 = 지하철_역_요청(new StationRequest("교대역"));
        ExtractableResponse<Response> 강남역 = 지하철_역_요청(new StationRequest("강남역"));
        ExtractableResponse<Response> 양재역 = 지하철_역_요청(new StationRequest("양재역"));

        // then
        Assertions.assertThat(교대역.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(강남역.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(양재역.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}