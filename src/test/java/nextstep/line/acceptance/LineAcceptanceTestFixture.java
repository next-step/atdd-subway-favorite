package nextstep.line.acceptance;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceTestFixture {
    public static final String 신분당선 = "신분당선";
    public static final String 분당선 = "분당선";
    public static final String RED = "bg-red-600";
    public static final String GREEN = "bg-green-600";
    public static final String 분당역 = "분당역";
    public static final String 홍대역 = "홍대역";
    public static final String 강남역 = "강남역";
    public static final String 성수역 = "성수역";
    public static final Long 분당역_ID = createStation(분당역);
    public static final Long 홍대역_ID = createStation(홍대역);
    public static final Long 강남역_ID = createStation(강남역);
    public static final Long 성수역_ID = createStation(성수역);
    public static final Long 생성된적없는_역_ID = -1L;
    public static final Integer DEFAULT_DISTANCE = 10;

    public static final Map<String, Object> 신분당선_PARAM = Map.of(
            "name", 신분당선,
            "color", RED,
            "upStationId", 분당역_ID,
            "downStationId", 홍대역_ID,
            "distance", DEFAULT_DISTANCE
    );

    public static final Map<String, Object> 분당선_PARAM = Map.of(
            "name", 분당선,
            "color", GREEN,
            "upStationId", 분당역_ID,
            "downStationId", 강남역_ID,
            "distance", 4
    );

    public static final Map<String, Object> MODIFY_PARAM = Map.of(
            "name", 분당선,
            "color", GREEN
    );

    public static final Map<String, Object> 홍대역_강남역_구간_PARAM = Map.of(
            "upStationId", 홍대역_ID,
            "downStationId", 강남역_ID,
            "distance", DEFAULT_DISTANCE
    );

    public static final Map<String, Object> 홍대역_서초역_구간_PARAM = Map.of(
            "upStationId", 홍대역_ID,
            "downStationId", 생성된적없는_역_ID,
            "distance", DEFAULT_DISTANCE
    );

    public static final Map<String, Object> 분당역_성수역_구간_PARAM = Map.of(
            "upStationId", 분당역_ID,
            "downStationId", 성수역_ID,
            "distance", 6
    );

    public static final Map<String, Object> 홍대역_분당역_구간_PARAM = Map.of(
            "upStationId", 홍대역_ID,
            "downStationId", 분당역_ID,
            "distance", DEFAULT_DISTANCE
    );

    public static Long createStation(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract()
                .jsonPath().getLong("id");
    }
}
