package nextstep.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static nextstep.path.acceptance.PathAcceptanceTestFixture.*;
import static nextstep.utils.AssertUtil.assertResponseCode;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 관련 인수 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PathAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this.getClass());
    }

    /**
     * Given: 연결된 구간을 가진 노선들이 등록되어있고,
     * When: 출발역과 도착역을 입력하여 최단거리 경로를 조회하면,
     * Then: 출발역에서 도착역까지 죄단거리의 경로와 거리의 길이가 조회된다.
     */
    @DisplayName("최단거리 경로 조회 요청은, 출발역과 도착역을 입력하여 최단거리 경로를 조회하면 죄단거리의 경로와 거리의 길이가 조회된다.")
    @Test
    void findShortestPathTest() {
        // given
        createLine(신분당선_PARAM);
        createLine(분당선_PARAM);
        createLine(경의선_PARAM);
        createLine(중앙선_PARAM);

        // when
        ExtractableResponse<Response> response = findShortestPaths(분당역_ID, 성수역_ID);

        // then
        assertResponseCode(response, HttpStatus.OK);
        assertThat(getStationIds(response)).containsExactly(분당역_ID, 강남역_ID, 성수역_ID);
        assertThat(getDistance(response)).isEqualTo(5);
    }

    private ExtractableResponse<Response> createLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> findShortestPaths(Long source, Long target) {
        return RestAssured.given().log().all()
                .when().get(String.format("/paths?source=%d&target=%d", source, target))
                .then().log().all()
                .extract();
    }

    private List<Long> getStationIds(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("stations.id", Long.class);
    }

    private static int getDistance(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getInt("distance");
    }
}
