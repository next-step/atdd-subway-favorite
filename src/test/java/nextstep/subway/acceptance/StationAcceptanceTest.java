package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 관리자가 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("관리자가 지하철역을 생성한다.")
    @Test
    void createStationByAdmin() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청("강남역", ADMIN_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final List<String> stationNames = 지하철역_목록_요청함();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * When 일반 사용자가 지하철역을 생성하면
     * Then 생성할 권한이 없다는 오류가 발생한다.
     */
    @DisplayName("일반 사용자는 지하철역을 생성할 수 없다.")
    @Test
    void createStationByMember() {
        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청("강남역", MEMBER_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStationsByAdmin() {
        // given
        지하철역_생성_요청("강남역", ADMIN_TOKEN);
        지하철역_생성_요청("역삼역", ADMIN_TOKEN);

        // when
        final ExtractableResponse<Response> 지하철역_목록_응답 = 지하철역_목록_요청();

        // then
        List<StationResponse> stations = 지하철역_목록_응답.jsonPath().getList(".", StationResponse.class);
        assertThat(stations).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 관리자가 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("관리자가 지하철역을 제거한다.")
    @Test
    void deleteStationByAdmin() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청("강남역", ADMIN_TOKEN);

        // when
        String location = createResponse.header("location");
        지하철역_삭제_요청(location, ADMIN_TOKEN);

        // then
        final List<String> stationNames = 지하철역_목록_요청함();
        assertThat(stationNames).doesNotContain("강남역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 일반 사용자가 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("일반 사용자는 지하철역을 제거할 수 없다.")
    @Test
    void deleteStationByMember() {
        // given
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청("강남역", ADMIN_TOKEN);

        // when
        String location = createResponse.header("location");
        final ExtractableResponse<Response> response = 지하철역_삭제_요청(location, MEMBER_TOKEN);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}