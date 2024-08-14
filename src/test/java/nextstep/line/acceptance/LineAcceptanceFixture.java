package nextstep.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
public class LineAcceptanceFixture extends AcceptanceTest {

    protected Long 신사역;
    protected Long 강남역;
    protected Long 청량리;
    protected Long 서울숲 ;
    protected Long 논현역;
    protected Long 양재역;

    protected Long 신분당선;
    protected Long 분당선;


    @BeforeEach
    void init() {
        신사역 = requestCreateStation("신사역")
                .jsonPath()
                .getObject("id", Long.class);

        강남역 = requestCreateStation("강남역")
                .jsonPath()
                .getObject("id", Long.class);

        청량리 = requestCreateStation("청량리")
                .jsonPath()
                .getObject("id", Long.class);
        서울숲 = requestCreateStation("서울숲")
                .jsonPath()
                .getObject("id", Long.class);

        논현역 = requestCreateStation("논현역")
                .jsonPath()
                .getObject("id", Long.class);

        양재역 = requestCreateStation("양재역")
                .jsonPath()
                .getObject("id", Long.class);

        신분당선 = requestCreateLine(신분당선_생성(신사역, 논현역)).jsonPath().getObject("id", Long.class);
        분당선 = requestCreateLine(분당선_생성(청량리, 서울숲)).jsonPath().getObject("id", Long.class);
    }

    public static Map<String, Object> 신분당선_생성(Long upStationId, Long downStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 7);
        return params;
    }

    public static Map<String, Object> 분당선_생성(Long upStationId, Long downStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "분당선");
        params.put("color", "bg-red-500");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 25);
        return params;
    }

    private static Map<String, String> createStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    private static ExtractableResponse<Response> requestCreateStation(String stationName) {
        return RestAssured.given().log().all()
                .body(createStationParams(stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> requestCreateLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}


