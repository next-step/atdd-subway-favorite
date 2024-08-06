package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.LineAssuredTemplate;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.UpdateLineRequest;
import nextstep.subway.station.StationAssuredTemplate;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@Sql(scripts = {"/delete-data.sql", "/create-default.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("지하철 노선 관리 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private static final String UP_STATION = "상행종점역";
    private static final String DOWN_STATION = "하행종점역";

    private static final String LINE_NAME = "신분당선";
    private static final String COLOR = "bg-red-600";
    private static final long DISTANCE = 10;

    private static final long UP_STATION_ID = 1;
    private static final long DOWN_STATION_ID = 2;

    /**
     * Given 노선에 연결할 상행종점역, 하행종점역을 먼저 생성한다.
     * When 지하철 노선을 등록합니다. 이때 상행, 하행 종점역과 같이 등록한다.
     * Then 지하철 노선 목록을 요청할 때 생성한 노선 정보를 확인할 수 있습니다.
     */
    @DisplayName("지하철 노선을 등록하고 목록 조회를 하면 등록한 노선을 볼 수 있습니다.")
    @Test
    void createLine() {
        // given

        // when
        LineRequest lineRequest = new LineRequest(LINE_NAME, COLOR, UP_STATION_ID, DOWN_STATION_ID, DISTANCE);

        ExtractableResponse<Response> result = LineAssuredTemplate.createLine(lineRequest)
                .then()
                .extract();

        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(result.body().jsonPath().getString("name")).isEqualTo(LINE_NAME);
        Assertions.assertThat(result.body().jsonPath().getString("color")).isEqualTo(COLOR);
        Assertions.assertThat(result.body().jsonPath().getList("stations")).hasSize(2)
                .extracting("name")
                .contains(UP_STATION, DOWN_STATION);
    }

    /**
     * Given 노선에 2개의 지하철을 등록한다.
     * When 관리자가 노선 목록을 조회한다.
     * Then 모든 지하철 노선 목록이 반환된다.
     */
    @Test
    @DisplayName("지하철 노선 목록을 조회하면 모든 지하철 노선 목록이 반환된다.")
    void showAllLines() {
        // given
        String newStation = "새로운지하쳘역";
        long newStationId = StationAssuredTemplate.createStationWithId(newStation);

        LineAssuredTemplate.createLine(new LineRequest("신분당선", "bg-red-600", UP_STATION_ID, DOWN_STATION_ID, DISTANCE));
        LineAssuredTemplate.createLine(new LineRequest("2호선", "bg-green-600", UP_STATION_ID, newStationId, DISTANCE));

        // when
        ExtractableResponse<Response> result = LineAssuredTemplate.searchAllLine()
                .then().log().all()
                .extract();

        // then
        List<LineResponse> responseData = result.jsonPath().getList(".", LineResponse.class);
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(responseData).hasSize(2);
        Assertions.assertThat(responseData).extracting("name")
                .contains("신분당선", "2호선");

        Assertions.assertThat(responseData.get(0).getStations()).hasSize(2)
                .extracting("id", "name")
                .containsExactly(
                        Tuple.tuple(UP_STATION_ID, UP_STATION),
                        Tuple.tuple(DOWN_STATION_ID, DOWN_STATION)
                );

        Assertions.assertThat(responseData.get(1).getStations()).hasSize(2)
                .extracting("id", "name")
                .containsExactly(
                        Tuple.tuple(UP_STATION_ID, UP_STATION),
                        Tuple.tuple(newStationId, newStation)
                );
    }

    /**
     * Given 지하철역과 지하철 노선을 등록합니다.
     * When 등록한 지하철 노선을 조회합니다.
     * Then 등록한 지하철 노선 정보를 응답받습니다.
     */
    @Test
    @DisplayName("특정 지하철 노선을 조회합니다.")
    void findLine() {
        // given
        LineRequest lineRequest = new LineRequest(LINE_NAME, COLOR, UP_STATION_ID, DOWN_STATION_ID, DISTANCE);
        long lineId = LineAssuredTemplate.createLine(lineRequest)
                .then().extract().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> result = LineAssuredTemplate.searchOneLine(lineId)
                .then().log().all()
                .extract();

        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(result.jsonPath().getString("name")).isEqualTo(LINE_NAME);
        Assertions.assertThat(result.jsonPath().getString("color")).isEqualTo(COLOR);
        Assertions.assertThat(result.jsonPath().getList("stations")).hasSize(2)
                .extracting("name")
                .containsExactly(UP_STATION, DOWN_STATION);
    }

    /**
     * Given 지하철 역과 지하철 노선을 생성합니다.
     * When 지하철 노선의 이름과 색 수정을 요청합니다.
     * Then 해당 노선을 요청했을 때 수정된 정보를 전달받습니다.
     */
    @Test
    @DisplayName("지하철 노선의 이름과 색 수정 요청을 하면 정상 응답을 받습니다. 이후 수정된 정보로 전달을 받습니다.")
    void updateLine() {
        // given
        LineRequest lineRequest = new LineRequest(LINE_NAME, COLOR, UP_STATION_ID, DOWN_STATION_ID, DISTANCE);
        long lineId = LineAssuredTemplate.createLine(lineRequest)
                .then().extract().jsonPath().getLong("id");

        // when
        String updateLineName = "신분분당선";
        String updateColor = "bg-red-60000";

        UpdateLineRequest updateLineRequest = new UpdateLineRequest(updateLineName, updateColor);
        ExtractableResponse<Response> updateResult = LineAssuredTemplate.updateLine(updateLineRequest, lineId)
                .then().log().all()
                .extract();

        Assertions.assertThat(updateResult.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> findResult = LineAssuredTemplate.searchOneLine(lineId)
                .then().log().all()
                .extract();

        Assertions.assertThat(findResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(findResult.jsonPath().getString("name")).isEqualTo(updateLineName);
        Assertions.assertThat(findResult.jsonPath().getString("color")).isEqualTo(updateColor);
        Assertions.assertThat(findResult.jsonPath().getList("stations")).hasSize(2)
                .extracting("name")
                .contains(UP_STATION, DOWN_STATION);
    }

    /**
     * Given 지하철 역과 지하철 노선을 생성합니다.
     * When 지하철 노선을 삭제합니다.
     * Then 전체 노선 목록을 요청할 때 해당 목록은 보이지 않습니다.
     */
    @Test
    @DisplayName("지하철 노선 삭제 요청을 보내면 정상 응답을 전달받습니다. 이후 해당 노선은 보이지 않습니다.")
    void deleteLine() {
        // given
        LineRequest lineRequest = new LineRequest(LINE_NAME, COLOR, UP_STATION_ID, DOWN_STATION_ID, DISTANCE);
        long lineId = LineAssuredTemplate.createLine(lineRequest)
                .then().extract().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResult = LineAssuredTemplate.deleteOneLine(lineId)
                .then().log().all()
                .extract();

        Assertions.assertThat(deleteResult.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> searchResult = LineAssuredTemplate.searchOneLine(lineId)
                .then().extract();

        Assertions.assertThat(searchResult.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
