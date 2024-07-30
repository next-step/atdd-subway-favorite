package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.line.LineAssuredTemplate;
import nextstep.subway.line.SectionAssuredTemplate;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineStationsResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.StationAssuredTemplate;
import nextstep.subway.station.StationFixtures;
import nextstep.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 노선 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private long 강남역_id;
    private long 역삼역_id;
    private long 선릉역_id;
    private long 이호선_id;

    @BeforeEach
    void setUp() {
        this.강남역_id = StationAssuredTemplate.createStation(StationFixtures.FIRST_UP_STATION.getName())
                .then().extract().jsonPath().getLong("id");
        this.역삼역_id = StationAssuredTemplate.createStation(StationFixtures.FIRST_DOWN_STATION.getName())
                .then().extract().jsonPath().getLong("id");
        this.선릉역_id =  StationAssuredTemplate.createStation(StationFixtures.SECOND_UP_STATION.getName())
                .then().extract().jsonPath().getLong("id");
        this.이호선_id = LineAssuredTemplate.createLine(new LineRequest("신분당선", "red", 강남역_id, 역삼역_id, 10L))
                .then().extract().jsonPath().getLong("id");
    }

    /**
     * Given 노선에 구간이 2개 등록되어 있습니다.(A-B, B-C)
     * When 추가하려는 구간에 A, B, C 역이 없는 경우
     * Then 에러 메시지를 응답받습니다.
     */
    @DisplayName("역을 새로 추가할 때 upStation 혹은 downStation 이 기존 구간에 없으면 에러 응답을 받습니다.")
    @Test
    void noExistStation() {
        // given
        SectionAssuredTemplate.addSection(이호선_id, new SectionRequest(역삼역_id, 선릉역_id, 20L));

        long newUpStation = StationAssuredTemplate.createStation(StationFixtures.THIRD_UP_STATION.getName())
                .then().extract().jsonPath().getLong("id");

        long newDownStation = StationAssuredTemplate.createStation(StationFixtures.THIRD_DOWN_STATION.getName())
                .then().extract().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> result = SectionAssuredTemplate.addSection(이호선_id, new SectionRequest(newUpStation, newDownStation, 30L))
                .then().extract();

        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(result.body().asString()).isEqualTo(SubwayErrorMessage.CANNOT_ADD_STATION.getMessage());
    }

    /**
     * Given 노선에 구간이 하나 등록되어 있습니다.
     * When 해당 노선에 신규 구간을 추가합니다.
     * Then 신규 구간의 상행역이 기존 구간의 하행역과 같지 않아 에러 응답을 보냅니다.
     */
    @DisplayName("새로운 구간 등록 시 새 구간의 상행역이 기존 구간의 하행역과 같지 않다면 에러 응답을 보냅니다.")
    @Test
    void invalidSection() {
        // given
        long newUpStationId = StationAssuredTemplate.createStation(StationFixtures.SECOND_UP_STATION.getName())
                .then().extract().jsonPath().getLong("id");
        long newDownStationId = StationAssuredTemplate.createStation(StationFixtures.SECOND_DOWN_STATION.getName())
                .then().extract().jsonPath().getLong("id");

        // when
        SectionRequest sectionRequest = new SectionRequest(newUpStationId, newDownStationId, 10L);
        ExtractableResponse<Response> result = SectionAssuredTemplate.addSection(이호선_id, sectionRequest)
                .then().log().all().extract();

        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(result.body().asString()).isEqualTo(SubwayErrorMessage.CANNOT_ADD_STATION.getMessage());
    }

    /**
     * Given 노선에 구간이 하나 등록되어 있습니다.
     * When 해당 노선에 신규 구간을 추가합니다.
     * Then 신규 구간의 하행역이 기존에 구역에 존재하기 때문에 에러 응답을 보냅니다.
     */
    @DisplayName("새로운 구간 등록 시 새 구간의 하행역이 기존 구간에 존재하는 역이라면 에러 응답을 보냅니다.")
    @Test
    void existDownStation() {
        // given

        // when
        SectionRequest sectionRequest = new SectionRequest(역삼역_id, 강남역_id, 10L);
        ExtractableResponse<Response> result = SectionAssuredTemplate.addSection(이호선_id, sectionRequest)
                .then().log().all().extract();

        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(result.body().asString()).isEqualTo(SubwayErrorMessage.CANNOT_ADD_STATION.getMessage());
    }

    /**
     * Given 노선에 구간이 하나 등록되어 있습니다.
     * When 해당 노선에 신규 구간을 추가합니다.
     * Then 노선 조회 시 추가된 신규 구간이 정상적으로 응답으로 보입니다.
     */
    // TODO: 7/17/24 좀 더 다양한 케이스에서 성공하는지 확인해야함, 특히 응답의 순서에 주의할 필요 있음
    @DisplayName("새로운 구간을 등록합니다.")
    @Test
    void addSection() {
        // given
        SectionAssuredTemplate.addSection(이호선_id, new SectionRequest(역삼역_id, 선릉역_id, 10L));
        long newDownStationId = StationAssuredTemplate.createStation(StationFixtures.SECOND_DOWN_STATION.getName())
                .then().extract().jsonPath().getLong("id");

        long newUpStationId = StationAssuredTemplate.createStation(StationFixtures.THIRD_UP_STATION.getName())
                .then().extract().jsonPath().getLong("id");

        // when
        // 1. upStation 기준으로 구간 추가
        SectionAssuredTemplate.addSection(이호선_id, new SectionRequest(역삼역_id, newDownStationId, 3L));

        // 2. downStation 기준으로 구간 추가
        SectionAssuredTemplate.addSection(이호선_id, new SectionRequest(newUpStationId, 강남역_id, 20L));

        // then
        ExtractableResponse<Response> result = LineAssuredTemplate.searchOneLine(이호선_id)
                .then().log().all().extract();

        Assertions.assertThat(result.jsonPath().getList("stations", LineStationsResponse.class)).hasSize(5)
                .extracting("name")
                .contains(
                        StationFixtures.THIRD_UP_STATION.getName(),
                        StationFixtures.FIRST_UP_STATION.getName(),
                        StationFixtures.FIRST_DOWN_STATION.getName(),
                        StationFixtures.SECOND_DOWN_STATION.getName(),
                        StationFixtures.SECOND_UP_STATION.getName()
                );
    }

    /**
     * Given 노선에 구간이 한 개 등록되어 있습니다.
     * When 노선에 해당 구간을 제거합니다.
     * Then 구간이 한 개 이므로 제거할 수 없다는 응답을 전달받습니다.
     */
    @Test
    @DisplayName("구간이 한 개인 경우 구간을 제거할 수 없습니다.")
    void hasOneSection() {
        // given
        // when
        ExtractableResponse<Response> result = SectionAssuredTemplate.deleteSection(이호선_id, 역삼역_id)
                .then().log().all().extract();

        // then
        Assertions.assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(result.body().asString()).isEqualTo(SubwayErrorMessage.CANNOT_DELETE_SECTION.getMessage());
    }

    /**
     * Given 노선에 구간이 2개 등록되어 있습니다.
     * When 노선의 중간 역에 대해 삭제 요청을 보냅니다.
     * Then 노선 정보를 요청하면 중간 역이 정상 삭제된 것을 확인할 수 있습니다.
     */
    @Test
    @DisplayName("2개의 구간에서 중간 역을 삭제할 수 있습니다. 이후 조회 시 삭제한 역은 조회되지 않습니다.")
    void notDownStation() {
        // given
        long newDownStationId = StationAssuredTemplate.createStation(StationFixtures.SECOND_DOWN_STATION.getName())
                .then().extract().jsonPath().getLong("id");

        SectionRequest sectionRequest = new SectionRequest(역삼역_id, newDownStationId, 10L);
        SectionAssuredTemplate.addSection(이호선_id, sectionRequest);

        // when
        SectionAssuredTemplate.deleteSection(이호선_id, 역삼역_id)
                .then().log().all().extract();

        // then
        ExtractableResponse<Response> lineResult = LineAssuredTemplate.searchOneLine(이호선_id)
                .then().log().all().extract();

        Assertions.assertThat(lineResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(lineResult.jsonPath().getList("stations", LineStationsResponse.class)).hasSize(2)
                .extracting("id", "name")
                .containsExactly(
                        Tuple.tuple(강남역_id, StationFixtures.FIRST_UP_STATION.getName()),
                        Tuple.tuple(newDownStationId, StationFixtures.SECOND_DOWN_STATION.getName())
                );
    }

    /**
     * Given 노선에 구간이 2개 등록되어 있습니다.
     * When 가장 마지막 하행 종점역에 대한 삭제를 진행합니다.
     * Then 노선 정보를 요청하면 마지막 하행 종점역이 정상 삭제된 것을 확인할 수 있습니다.
     */
    @Test
    @DisplayName("정상적으로 하행 종점역 삭제가 진행됩니다.")
    void deleteStation() {
        // given
        long newDownStationId = StationAssuredTemplate.createStation(StationFixtures.SECOND_DOWN_STATION.getName())
                .then().extract().jsonPath().getLong("id");

        SectionRequest sectionRequest = new SectionRequest(역삼역_id, newDownStationId, 10L);
        SectionAssuredTemplate.addSection(이호선_id, sectionRequest);

        // when
        ExtractableResponse<Response> deleteResult = SectionAssuredTemplate.deleteSection(이호선_id, newDownStationId)
                .then().log().all().extract();

        Assertions.assertThat(deleteResult.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> lineResult = LineAssuredTemplate.searchOneLine(이호선_id)
                .then().log().all().extract();

        Assertions.assertThat(lineResult.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(lineResult.jsonPath().getList("stations", LineStationsResponse.class)).hasSize(2)
                .extracting("id", "name")
                .containsExactly(
                        Tuple.tuple(강남역_id, StationFixtures.FIRST_UP_STATION.getName()),
                        Tuple.tuple(역삼역_id, StationFixtures.FIRST_DOWN_STATION.getName())
                );
    }
}
