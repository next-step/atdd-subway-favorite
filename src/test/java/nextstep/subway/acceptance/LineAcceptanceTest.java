package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.DatabaseSetupTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.AcceptanceTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class LineAcceptanceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long 신사역Id;
    private Long 논현역Id;
    private Long 강남역Id;
    Map<String, Object> 신분당선_요청_매개변수;

    @BeforeEach
    void setUp() {
        LineAndStationSetup lineAndStationSetup = new LineAndStationSetup(jdbcTemplate);
        lineAndStationSetup.setUpDatabase();

        신사역Id = 역_생성("신사역").jsonPath().getLong("id");
        논현역Id = 역_생성("논현역").jsonPath().getLong("id");
        강남역Id = 역_생성("강남역").jsonPath().getLong("id");
        신분당선_요청_매개변수 = 노선_생성_매개변수("신분당선", "bg-red-600", 신사역Id, 논현역Id, 10L);

    }

    @Test
    @DisplayName("지하철 노선을 생성한다")
    void createLine() {
        // when
        ExtractableResponse<Response> response = 노선_생성_Extract(신분당선_요청_매개변수);

        // then
        String name = response.jsonPath().getString("name");
        String color = response.jsonPath().getString("color");
        List<Map<String, Object>> stations = response.jsonPath().getList("stations");

        assertThat(name).isEqualTo("신분당선");
        assertThat(color).isEqualTo("bg-red-600");
        assertThat(stations).hasSize(2);
        Map<String, Object> firstStation = stations.get(0);
        Map<String, Object> secondStation = stations.get(1);
        assertThat(Long.valueOf(String.valueOf(firstStation.get("id")))).isEqualTo(신사역Id);
        assertThat(firstStation.get("name")).isEqualTo("신사역");
        assertThat(Long.valueOf(String.valueOf(secondStation.get("id")))).isEqualTo(논현역Id);
        assertThat(secondStation.get("name")).isEqualTo("논현역");
    }

    @Test
    @DisplayName("존재하지 않는 상행역을 포함한 노선을 생성하면 에러가 발생한다.")
    void 존재하지_않는_상행역을_포함한_노선_생성() {
        //given
        Map<String, Object> 분당선_요청_매개변수 = 노선_생성_매개변수("분당선", "bg-green-600", 99L, 신사역Id, 7L);
        // when
        ExtractableResponse<Response> response = 노선_생성_Extract(분당선_요청_매개변수);

        // then
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("존재하지 않는 하행역을 포함한 노선을 생성하면 에러가 발생한다.")
    void 존재하지_않는_하행역을_포함한_노선_생성() {
        //given
        Map<String, Object> 분당선_요청_매개변수 = 노선_생성_매개변수("분당선", "bg-green-600", 신사역Id, 99L, 7L);
        // when
        ExtractableResponse<Response> response = 노선_생성_Extract(분당선_요청_매개변수);

        // then
        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void viewLineList() {
        // given
        노선_생성_Extract(신분당선_요청_매개변수);

        Map<String, Object> 분당선_요청_매개변수 = 노선_생성_매개변수("분당선", "bg-green-600", 신사역Id, 강남역Id, 7L);

        노선_생성_Extract(분당선_요청_매개변수);

        // when
        List<Map<String, Object>> response = getLineListExtract().jsonPath().getList("$");

        // then
        assertThat(response).hasSize(2);

        assertThat(response.get(0).get("name")).isEqualTo("신분당선");
        assertThat(response.get(0).get("color")).isEqualTo("bg-red-600");
        List<Map<String, Object>> stations1 = (List<Map<String, Object>>) response.get(0).get("stations");
        assertThat(stations1.size()).isEqualTo(2);
        assertThat(Long.valueOf(String.valueOf(stations1.get(0).get("id")))).isEqualTo(신사역Id);
        assertThat(Long.valueOf(String.valueOf(stations1.get(1).get("id")))).isEqualTo(논현역Id);

        assertThat(response.get(1).get("name")).isEqualTo("분당선");
        assertThat(response.get(1).get("color")).isEqualTo("bg-green-600");
        List<Map<String, Object>> stations2 = (List<Map<String, Object>>) response.get(1).get("stations");
        assertThat(stations2.size()).isEqualTo(2);
        assertThat(Long.valueOf(String.valueOf(stations2.get(0).get("id")))).isEqualTo(신사역Id);
        assertThat(Long.valueOf(String.valueOf(stations2.get(1).get("id")))).isEqualTo(강남역Id);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void viewLine() {
        // given
        long lineId = 노선_생성_Extract(신분당선_요청_매개변수).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 노선_조회_Extract(lineId);

        // then
        Long responseLineId = response.jsonPath().getLong("id");
        String responseLineName = response.jsonPath().getString("name");
        String responseLineColor = response.jsonPath().getString("color");
        List<Map<String, Object>> stations = response.jsonPath().getList("stations");

        assertThat(responseLineId).isEqualTo(lineId);
        assertThat(responseLineName).isEqualTo("신분당선");
        assertThat(responseLineColor).isEqualTo("bg-red-600");
        assertThat(stations).hasSize(2);
        assertThat(Long.valueOf(String.valueOf(stations.get(0).get("id")))).isEqualTo(1);
        assertThat(Long.valueOf(String.valueOf(stations.get(1).get("id")))).isEqualTo(2);
    }

    @Test
    @DisplayName("존재하지 않는 노선을 조회하면 에러가 발생한다.")
    void 존재하지_않는_노선을_조회() {
        // when
        ExtractableResponse<Response> response = 노선_조회_Extract(9999L);

        // then
        assertThat(response.statusCode()).isEqualTo(404);
    }


    @Test
    @DisplayName("지하철 노선을 수정한다.")
    void updateLine() {
        // given
        long lineId = 노선_생성_Extract(신분당선_요청_매개변수).jsonPath().getLong("id");

        Map<String, Object> 노선_수정_매개변수 = new HashMap<>();
        노선_수정_매개변수.put("name", "다른분당선");
        노선_수정_매개변수.put("color", "bg-red-700");

        // when
        ExtractableResponse<Response> patchResponse = patchLineExtract(노선_수정_매개변수, lineId);

        ExtractableResponse<Response> viewResponse = 노선_조회_Extract(lineId);

        // then
        assertThat(patchResponse.statusCode()).isEqualTo(200);

        assertThat(viewResponse.jsonPath().getString("name")).isEqualTo("다른분당선");
        assertThat(viewResponse.jsonPath().getString("color")).isEqualTo("bg-red-700");
    }

    @Test
    @DisplayName("존재하지 않는 노선을 수정하면 에러가 발생한다.")
    void 존재하지_않는_노선_수정() {
        //given
        Map<String, Object> 노선_수정_매개변수 = new HashMap<>();
        노선_수정_매개변수.put("name", "다른분당선");
        노선_수정_매개변수.put("color", "bg-red-700");

        // when
        ExtractableResponse<Response> patchResponse = patchLineExtract(노선_수정_매개변수, 999L);

        // then
        assertThat(patchResponse.statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void DeleteLine() {
        // given
        long lineId = 노선_생성_Extract(신분당선_요청_매개변수).jsonPath().getLong("id");
        // when
        ExtractableResponse<Response> deleteResponse = deleteLineExtract(lineId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(204);
        assertThat(노선_조회_Extract(lineId).statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("존재하지 않는 노선을 삭제하면 에러가 발생한다.")
    void 존재하지_않는_노선_삭제() {
        // when
        ExtractableResponse<Response> deleteResponse = deleteLineExtract(999L);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(404);
    }

    private ExtractableResponse<Response> getLineListExtract() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteLineExtract(long lineId) {
        return RestAssured.given().log().all()
                .when().delete("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> patchLineExtract(Map<String, Object> putParams, long lineId) {
        return RestAssured.given().log().all()
                .body(putParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    private class LineAndStationSetup extends DatabaseSetupTemplate {

        public LineAndStationSetup(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate);
        }

        @Override
        protected void truncateTables() {
            jdbcTemplate.execute("TRUNCATE TABLE line");
            jdbcTemplate.execute("TRUNCATE TABLE station");
        }

        @Override
        protected void resetAutoIncrement() {
            jdbcTemplate.execute("ALTER TABLE station ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE line ALTER COLUMN id RESTART WITH 1");
        }

        @Override
        protected void insertInitialData() {
        }
    }
}
