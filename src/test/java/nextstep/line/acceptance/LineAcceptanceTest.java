package nextstep.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static nextstep.line.acceptance.LineAcceptanceTestFixture.*;
import static nextstep.utils.AssertUtil.assertResponseCode;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class LineAcceptanceTest {

    private static final int LINE_CREATION_PARAMS_INDEX = 0;
    private static final int SECTION_PARAMS_INDEX = 1;
    private static final int EXPECTED_STATION_IDS_INDEX = 2;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this.getClass());
    }

    /**
     * Given 새로운 지하철 노선 정보를 입력하고
     * When 관리자가 노선을 생성하면
     * Then 해당 노선이 생성되고 노선 목록에 포함된다
     */
    @DisplayName("노선 생성 요청은, 노선 정보를 입력하고 요청하면 노선 조회시 노선이 목록에 포함된다.")
    @Test
    void createLineTest() {
        // when
        ExtractableResponse<Response> response = createLine(신분당선_PARAM);

        // then
        assertResponseCode(response, HttpStatus.CREATED);
        assertThat(getNames(findLines())).containsExactlyInAnyOrder(신분당선);
    }

    /**
     * Given 여러 개의 지하철 노선이 등록되어 있고,
     * When 관리자가 지하철 노선 목록을 조회하면,
     * Then 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("노선 목록 조회 요청은, 요청하면 전체 노선 목록이 조회된다.")
    @Test
    void findLinesTest() {
        // given
        createLine(신분당선_PARAM);
        createLine(분당선_PARAM);

        // when
        ExtractableResponse<Response> response = findLines();

        // then
        assertResponseCode(response, HttpStatus.OK);
        assertThat(getNames(response)).containsExactlyInAnyOrder(신분당선, 분당선);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("특정 노선 조회 요청은, 특정 노선 조회를 요청하면 해당 노선의 정보가 반환된다.")
    @Test
    void lookUpLineTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = lookUpLine(getId(createdLineResponse));

        // then
        assertResponseCode(response, HttpStatus.OK);
        assertThat(getName(response)).isEqualTo(신분당선);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @DisplayName("노선 수정 요청은, 노선 수정 요청을하면 해당 노선 조회시 수정된 정보가 조회된다.")
    @Test
    void modifyLineTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = modifyLine(getId(createdLineResponse), MODIFY_PARAM);

        // then
        assertResponseCode(response, HttpStatus.OK);
        ExtractableResponse<Response> lookedUpLine = lookUpLine(getId(createdLineResponse));
        assertThat(getName(lookedUpLine)).isEqualTo(분당선);
        assertThat(lookedUpLine.jsonPath().getString("color")).isEqualTo(GREEN);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("노선 삭제 요청은, 노선 삭제를 요청하면 전체 노선을 조회했을 때 해당 노선이 제외된다.")
    @Test
    void deleteLineTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = deleteLine(getId(createdLineResponse));

        // then
        assertResponseCode(response, HttpStatus.NO_CONTENT);
        assertThat(getNames(findLines())).doesNotContain(신분당선);
    }

    /**
     * Given: 특정 구간이 등록되어 있고,
     * When: 구간을 추가하면,
     * Then: 노선 조회시 구간이 등록되어있다.
     */
    @DisplayName("노선 구간 추가 요청은, 특정 노선에 구간 추가 요청을 하면 해당 노선 조회시 등록된 구간이 조회된다.")
    @Test
    void addSectionTest() {
        List<Arguments> fixtures = addSectionFixtures();

        for (Arguments arguments : fixtures){
            // given
            ExtractableResponse<Response> createdLineResponse = createLine((Map<String, Object>) arguments.get()[LINE_CREATION_PARAMS_INDEX]);
            Map<String, Object> sectionParams = (Map<String, Object>) arguments.get()[SECTION_PARAMS_INDEX];
            List<Long> expectedStationIds = (List<Long>) arguments.get()[EXPECTED_STATION_IDS_INDEX];

            // when
            ExtractableResponse<Response> response = addSection(getId(createdLineResponse), sectionParams);

            // then
            assertResponseCode(response, HttpStatus.CREATED);
            List<Long> stationsIds = getStationIds(getId(createdLineResponse));
            assertThat(stationsIds).isEqualTo(expectedStationIds);
        }
    }

    private List<Arguments> addSectionFixtures() {
        return List.of(
            Arguments.of(신분당선_PARAM, 홍대역_강남역_구간_PARAM, List.of(분당역_ID, 홍대역_ID, 강남역_ID)),
            Arguments.of(분당선_PARAM, 홍대역_분당역_구간_PARAM, List.of(홍대역_ID, 분당역_ID, 강남역_ID)),
            Arguments.of(신분당선_PARAM, 분당역_성수역_구간_PARAM, List.of(분당역_ID, 성수역_ID, 홍대역_ID))
        );
    }

    /**
     * When: 존재하지 않는 노선에 구간을 등록하면,
     * Then: 오류를 응답한다.
     */
    @DisplayName("존재하지 않는 노선에 구간 등록 요청을하면 오류가 발생한다.")
    @Test
    void notExistLineExceptionTest() {
        // when
        ExtractableResponse<Response> response = addSection(1L, 홍대역_강남역_구간_PARAM);

        // then
        assertResponseCode(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 존재하지 않는 역이 포함된 구간을 등록하면,
     * Then: 오류를 응답한다.
     */
    @DisplayName("존재하지 않는 역이 포함된 구간을 노선에 등록하면 오류가 발생한다.")
    @Test
    void notExistStationExceptionTest() {
        // given
        var createdLineResponse = createLine(신분당선_PARAM);

        // when
        var response = addSection(getId(createdLineResponse), 홍대역_서초역_구간_PARAM);

        // then
        assertResponseCode(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Given: 특정 노선에 구간이 2개 이상 등록되어 있고,
     * When: 지하철 역의 위치에 상관없이 구간을 제거하면,
     * Then: 노선을 조회했을 때 구간이 제거된다.
     */
    @DisplayName("지하철 구간 제거 요청은, 특정 노선에 지하철 구간 제거 요청을 하면 해당 노선을 조회했을 때 구간이 제거된다.")
    @Test
    void deleteSectionTest() {
        List<Arguments> fixtures = deleteSectionFixtures();

        for (Arguments fixture : fixtures) {
            // given
            var createdLineResponse = createLine(신분당선_PARAM);
            addSection(getId(createdLineResponse), 홍대역_강남역_구간_PARAM);
            Long stationId = (Long) fixture.get()[0];
            List<Long> expectedStationIds = (List<Long>) fixture.get()[1];

            // when
            var response = deleteSection(getId(createdLineResponse), stationId);

            // then
            assertResponseCode(response, HttpStatus.OK);
            List<Long> stationIds = getStationIds(getId(createdLineResponse));
            assertThat(stationIds).isEqualTo(expectedStationIds);
        }
    }

    private static List<Arguments> deleteSectionFixtures() {
        return List.of(
                Arguments.of(분당역_ID, List.of(홍대역_ID, 강남역_ID)),
                Arguments.of(홍대역_ID, List.of(분당역_ID, 강남역_ID)),
                Arguments.of(강남역_ID, List.of(분당역_ID, 홍대역_ID))
        );
    }

    /**
     * Given: 특정 노선에 구간이 1개 등록되어 있고,
     * When: 노선의 하행역 구간을 제거하면,
     * Then: 오류를 응답한다.
     */
    @DisplayName("지하철 노선에 구간이 1개인 경우 구간을 제거하려하면 오류가 발생한다.")
    @Test
    void deleteSectionOfOnlyOneSectionLineExceptionTest() {
        // given
        ExtractableResponse<Response> createdLineResponse = createLine(신분당선_PARAM);

        // when
        ExtractableResponse<Response> response = deleteSection(getId(createdLineResponse), 홍대역_ID);

        // then
        assertResponseCode(response, HttpStatus.BAD_REQUEST);
    }

    private ExtractableResponse<Response> createLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> lookUpLine(Long id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> modifyLine(Long id, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> addSection(Long id, Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(String.format("/lines/%d/sections", id))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteSection(Long id, Long stationId) {
        return RestAssured.given().log().all()
                .when().delete(String.format("/lines/%s/sections?stationId=%s", id, stationId))
                .then().log().all()
                .extract();
    }

    private List<Long> getStationIds(Long lindId) {
        return lookUpLine(lindId).jsonPath()
                .getList("stations.id", Long.class);
    }

    private List<String> getNames(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("name", String.class);
    }

    private static String getName(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getString("name");
    }

    private static long getId(ExtractableResponse<Response> createdLineResponse) {
        return createdLineResponse.jsonPath()
                .getLong("id");
    }
}
