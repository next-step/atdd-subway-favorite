package nextstep.subway.station.acceptance;

import static nextstep.subway.station.acceptance.StationSteps.모든_역_조회_요청_후_역이름_리스트_반환;
import static nextstep.subway.station.acceptance.StationSteps.역_생성_요청;
import static nextstep.subway.station.acceptance.StationSteps.역_삭제_요청;
import static nextstep.subway.station.acceptance.StationSteps.역_생성_요청_후_id_반환;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.Response;
import java.util.List;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철역을 생성한다
     * When 지하철역 전체 목록을 조회한다
     * Then 생성된 지하철 역들의 개수와 이름을 확인할 수 있다
     */
    @DisplayName("지하철 역을 2개 생성하고 등록된 역들을 조회한다.")
    @Test
    void test() {
        // given
        역_생성_요청("건대입구역");
        역_생성_요청("어린이대공원역");

        // when
        List<String> stationNames = 모든_역_조회_요청_후_역이름_리스트_반환();

        // then
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsAnyOf(
                "건대입구역", "어린이대공원역"
        );
    }

    /**
     * Given 지하철역을 생성한다
     * When 생성한 지하철 역을 제거한다
     * Then 204(noContent) 응답코드를 확인한다.
     */
    @DisplayName("생성한 역을 삭제하면 204 코드를 반환한다.")
    @Test
    void test2() {
        // given
        Long stationId = 역_생성_요청_후_id_반환("건대입구역");
        String id = String.valueOf(stationId);

        // when
        Response response = 역_삭제_요청(id);

        // then
        assertThat(response.getStatusCode()).isEqualTo(204);
    }

    /**
     * Given 지하철역을 생성한다
     * When 생성된 지하철 역이 아닌, 임의의 역을 제거한다
     * Then 204(noContent) 응답코드가 아니고 500 에러 코드임을 확인한다
     */
    @DisplayName("생성한 역이 아닌 역을 삭제하면 204 코드를 반환하지 않는다.")
    @Test
    void test3() {
        // given
        Long stationId = 역_생성_요청_후_id_반환("건대입구역");
        String id = String.valueOf(stationId + 1);

        // when
        Response response = 역_삭제_요청(id);

        // then
        assertThat(response.getStatusCode()).isNotEqualTo(204);
        assertThat(response.getStatusCode()).isEqualTo(500);
    }
}