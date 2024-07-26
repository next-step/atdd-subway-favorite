package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.dto.StationResponse;
import nextstep.subway.utils.DatabaseSetupTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.acceptance.AcceptanceTestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class PathAcceptanceTest {
    private Long 교대역Id;
    private Long 강남역Id;
    private Long 양재역Id;
    private Long 남부터미널역Id;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        TestSetup testSetup = new TestSetup(jdbcTemplate);
        testSetup.setUpDatabase();
        교대역Id = 역_생성("교대역").jsonPath().getLong("id");
        강남역Id = 역_생성("강남역").jsonPath().getLong("id");
        양재역Id = 역_생성("양재역").jsonPath().getLong("id");
        남부터미널역Id = 역_생성("남부터미널역").jsonPath().getLong("id");
    }

    /**
     * Given 3개의 노선이 등록돼있고, (교대-강남 [10], 강남-양재 [10], 교대-남부터미널 [2], 남부터미널-양재 [3])
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * When 출발역(교대역)과 도착역(양재역) 경로를 조회 하면
     * Then 최단거리의 경로를 조회한다. (교대역 - 남부터미널역 - 양재역 [5])
     */
    @Test
    @DisplayName("출발역과 도착역의 최단경로를 조회한다.")
    void 최단_경로_조회() {
        //given
        노선_생성_Extract(노선_생성_매개변수("2호선", "bg-green-600", 교대역Id, 강남역Id, 10L));
        노선_생성_Extract(노선_생성_매개변수("신분당선", "bg-gre-600", 강남역Id, 양재역Id, 10L));
        ExtractableResponse<Response> 삼호선_생성_응답 = 노선_생성_Extract(노선_생성_매개변수("3호선", "bg-green-600", 교대역Id, 남부터미널역Id, 2L));
        long 삼호선Id = 삼호선_생성_응답.jsonPath().getLong("id");
        노선에_새로운_구간_추가_Extract(구간_생성_매개변수(남부터미널역Id, 양재역Id, 3L), 삼호선Id);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .queryParam("source", 교대역Id)
                .queryParam("target", 양재역Id)
                .when().get("/paths")
                .then().log().all()
                .extract();

        // then
        List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);
        long distance = response.jsonPath().getLong("distance");

        assertThat(stations).hasSize(3);
        assertThat(stations.get(0).getId()).isEqualTo(교대역Id);
        assertThat(stations.get(1).getId()).isEqualTo(남부터미널역Id);
        assertThat(stations.get(2).getId()).isEqualTo(양재역Id);
        assertThat(distance).isEqualTo(5L);
    }


    private class TestSetup extends DatabaseSetupTemplate {

        public TestSetup(JdbcTemplate jdbcTemplate) {
            super(jdbcTemplate);
        }

        @Override
        protected void truncateTables() {
            jdbcTemplate.execute("TRUNCATE TABLE line");
            jdbcTemplate.execute("TRUNCATE TABLE station");
            jdbcTemplate.execute("TRUNCATE TABLE section");
        }

        @Override
        protected void resetAutoIncrement() {
            jdbcTemplate.execute("ALTER TABLE station ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE line ALTER COLUMN id RESTART WITH 1");
            jdbcTemplate.execute("ALTER TABLE section ALTER COLUMN id RESTART WITH 1");
        }

        @Override
        protected void insertInitialData() {
        }
    }
}
