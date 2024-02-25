package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.exception.ExceptionResponse;
import nextstep.subway.domain.request.LineRequest;
import nextstep.subway.domain.response.LineResponse;
import nextstep.subway.domain.response.StationResponse;
import nextstep.subway.utils.StationTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.exception.ExceptionMessage.*;
import static nextstep.subway.utils.LineTestUtil.getLine;
import static nextstep.subway.utils.LineTestUtil.지하철_노선_생성;
import static nextstep.subway.utils.SectionTestUtil.deleteSection;
import static nextstep.subway.utils.SectionTestUtil.지하철_구간_추가;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest extends AcceptanceTest {

    long stationId1, stationId2, stationId3, lineId;
    int distance;

    @BeforeEach
    public void setUp() {
        super.setUp();
        stationId1 = StationTestUtil.지하철역_생성("A").jsonPath().getLong("id");
        stationId2 = StationTestUtil.지하철역_생성("B").jsonPath().getLong("id");
        stationId3 = StationTestUtil.지하철역_생성("C").jsonPath().getLong("id");

        distance = 10;
        lineId = 지하철_노선_생성(new LineRequest("2호선", "green", stationId1, stationId2, distance)).jsonPath().getLong("id");
    }

    /**
     * 구간 등록 기능
     * 지하철 노선에 구간을 등록하는 기능을 구현
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void addSectionTest() {
        //given

        //when
        ExtractableResponse<Response> response = 지하철_구간_추가(lineId, stationId2, stationId3, 10);

        // ERROR
        LineResponse lineResponse = response.as(LineResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(lineResponse.getId()).isEqualTo(lineId),
                () -> assertThat(lineResponse.getStations().stream().map(StationResponse::getId)).contains(stationId2, stationId3)
        );
    }

    /**
     * 새로운 구간의 상행역과 하행역은 같을 수 없다.
     */
    @DisplayName("새로운 구간의 상행역과 하행역은 같을 수 없다.")
    @Test
    void addSectionExceptionTest3() {
        //given

        //when
        String exceptionMessage = 지하철_구간_추가(lineId, stationId2, stationId2, 13).as(ExceptionResponse.class).getMessage();

        //then
        assertThat(exceptionMessage).isEqualTo(NEW_SECTION_VALIDATION_EXCEPTION.getMessage());
    }

    /**
     * 구간 제거 기능
     * 지하철 노선에 구간을 제거하는 기능 구현
     */
    @DisplayName("구간 제거 기능")
    @Test
    void deleteSectionTest() {
        //given
        지하철_구간_추가(lineId, stationId2, stationId3, 11);

        //when
        deleteSection(lineId, stationId3);

        //then
        //section 수 = 1
        LineResponse lineResponse = getLine(lineId).as(LineResponse.class);
        assertThat(lineResponse.getStations()).hasSize(2);
    }

    /**
     * 구간 제거 실패 테스트
     * 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
     */
    @DisplayName("지하철 노선에 구간이 1개인 경우 역을 삭제할 수 없다.")
    @Test
    void deleteSectionExceptionTest2() {
        //given

        //when
        String message = deleteSection(lineId, stationId2).as(ExceptionResponse.class).getMessage();

        //then
        assertThat(message).isEqualTo(DELETE_ONLY_ONE_SECTION_EXCEPTION.getMessage());
    }

    /**
     * Given 지하철 노선에 구간 추가하고
     * When 추가한 지하철 구간을 조회하면
     * Then 등록한 지하철 노선의 구간 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 구간 조회")
    @Test
    void showSection() {
        //given
        지하철_구간_추가(lineId, stationId2, stationId3, 11);

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/{lineId}/sections/{sectionId}", lineId, 1L)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        //then
        long id = response.jsonPath().getLong("id");
        long upStationId = response.jsonPath().getLong("upStation.id");
        long downStationId = response.jsonPath().getLong("downStation.id");
        int sectionDistance = response.jsonPath().getInt("distance");

        assertAll(
                () -> assertThat(id).isEqualTo(1L),
                () -> assertThat(upStationId).isEqualTo(stationId1),
                () -> assertThat(downStationId).isEqualTo(stationId2),
                () -> assertThat(sectionDistance).isEqualTo(distance)
        );
    }

    /**
     * 노선에 역 추가시 노선 가운데 추가 할 수 있다.
     **/
    @DisplayName("노선 가운데 역 추가")
    @Test
    void insertSection() {
        // given
        // 노선 A-B

        // when
        // A-C를 추가하면
        LineResponse response = 지하철_구간_추가(lineId, stationId1, stationId3, 4).as(LineResponse.class);

        // then
        // A-C-B 가 된다.
        assertAll(
                () -> assertThat(response.getStations()).hasSize(3),
                () -> assertThat(response.getStations().stream().map(StationResponse::getName)).contains("A", "B", "C"),
                () -> assertThat(response.getDistance()).isEqualTo(10),
                () -> assertThat(response.findSectionByUpStationName("A").getDistance()).isEqualTo(4),
                () -> assertThat(response.findSectionByUpStationName("C").getDistance()).isEqualTo(6)
        );
    }

    /**
     * 노선에 역 추가시 노선 처음에 추가 할 수 있다.
     **/
    @DisplayName("노선 처음에 역 추가")
    @Test
    void insertSectionToFirst() {
        // given
        // 노선 A-B

        // when
        // C-A를 추가하면
        LineResponse response = 지하철_구간_추가(lineId, stationId3, stationId1, 4).as(LineResponse.class);

        // then
        // C-A-B 가 된다.
        assertAll(
                () -> assertThat(response.getStations()).hasSize(3),
                () -> assertThat(response.getStations().stream().map(StationResponse::getName)).contains("A", "B", "C")
        );
    }

    /**
     * 이미 등록되어있는 구간은 노선에 등록될 수 없다.
     */
    @DisplayName("이미 등록되어있는 구간은 노선에 등록될 수 없다.")
    @Test
    void addRegisteredStation() {
        // given
        // 노선 A-B-C
        지하철_구간_추가(lineId, stationId2, stationId3, 4).as(LineResponse.class);

        // when
        // C-A를 추가하면
        ExtractableResponse<Response> response = 지하철_구간_추가(lineId, stationId3, stationId1, 4);

        // then
        // 에러 발생
        String message = response.as(ExceptionResponse.class).getMessage();
        assertThat(message).isEqualTo(ALREADY_REGISTERED_SECTION_EXCEPTION.getMessage());
    }

    /**
     * 노선의 중간에 추가할 경우
     * 추가 구간의 거리가 기존 노선의 거리보다 크다면 추가할 수 없다.
     */
    @DisplayName("기존 구간보다 거리가 큰 구간은 추가할 수 없다.")
    @Test
    void addLongerDistanceSection() {
        // given
        // 노선 A-B, 거리 10

        // when
        // A-C, 거리 11 을 추가하면
        ExtractableResponse<Response> response = 지하철_구간_추가(lineId, stationId1, stationId3, 11);

        // then
        // 에러 발생
        String message = response.as(ExceptionResponse.class).getMessage();
        assertThat(message).isEqualTo(LONGER_DISTANCE_SECTION_EXCEPTION.getMessage());
    }

    /**
     * 노선에 등록된 역 제거 시 해당 역이 노선 가운데 있어도 제거할 수 있다.
     * - 중간역이 제거될 경우 재배치를 함
     * - 노선에 A - B - C 역이 연결되어 있을 때 B역을 제거할 경우 A - C로 재배치 됨. 거리는 두 구간의 거리의 합으로 정함
     */
    @DisplayName("노선 중간의 역 제거")
    @Test
    void removeStationInMiddleOfLine() {
        //given
        // A-B-C (A-B : 10, B-C : 10)
        지하철_구간_추가(lineId, stationId2, stationId3, 10);

        //when
        // B 역 제거
        deleteSection(lineId, stationId2);
        LineResponse lineResponse = getLine(lineId).as(LineResponse.class);

        //then
        // A-C
        assertAll(
                () -> assertThat(lineResponse.getSections()).hasSize(1),
                () -> assertThat(lineResponse.getStations().stream().map(StationResponse::getName)).contains("A","C"),
                () -> assertThat(lineResponse.getSections().get(0).getDistance()).isEqualTo(20)
        );
    }

    /**
     * 노선에 등록된 역 제거 시 해당 역이 상행 종점역이어도 제거할 수 있다.
     * - 종점이 제거될 경우 다음으로 오던 역이 종점이 됨
     */
    @DisplayName("노선의 상행 종점역 제거")
    @Test
    void removeStationTopOfLine() {
        // given
        // A-B-C
        // A-B-C (A-B : 10, B-C : 10)
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", stationId2);
        params.put("downStationId", stationId3);
        params.put("distance", 10);
        지하철_구간_추가(lineId, stationId2, stationId3, 10);

        // when
        // A 제거
        deleteSection(lineId, stationId1);
        LineResponse lineResponse = getLine(lineId).as(LineResponse.class);

        // then
        // B-C , distance : 10
        assertAll(
                () -> assertThat(lineResponse.getSections()).hasSize(1),
                () -> assertThat(lineResponse.getStations().stream().map(StationResponse::getName)).contains("B","C"),
                () -> assertThat(lineResponse.getSections().get(0).getDistance()).isEqualTo(10)
        );
    }

    /**
     * 노선의 하행 종점역 제거
     * - 종점이 제거될 경우 다음으로 오던 역이 종점이 됨
     */
    @DisplayName("노선의 하행 종점역 제거")
    @Test
    void removeStationTailOfLine() {
        // given
        // A-B-C
        // A-B-C (A-B : 10, B-C : 10)
        지하철_구간_추가(lineId, stationId2, stationId3, 10);

        // when
        // C 제거
        deleteSection(lineId, stationId3);
        LineResponse lineResponse = getLine(lineId).as(LineResponse.class);

        // then
        // B-C , distance : 10
        assertAll(
                () -> assertThat(lineResponse.getSections()).hasSize(1),
                () -> assertThat(lineResponse.getStations().stream().map(StationResponse::getName)).contains("A","B"),
                () -> assertThat(lineResponse.getSections().get(0).getDistance()).isEqualTo(10)
        );
    }
}
