package nextstep.core.station.step;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.station.application.dto.StationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static nextstep.common.utils.HttpResponseUtils.getCreatedLocationId;

public class StationSteps {

    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    public static final String COLOR_KEY = "color";


    public static void 지하철_역_생성_요청(List<StationRequest> requests) {
        requests.forEach(StationSteps::지하철_역_생성_요청);
    }

    public static Long 지하철_역_생성(StationRequest request) {
        return getCreatedLocationId(지하철_역_생성_요청(request));
    }

    public static ExtractableResponse<Response> 지하철_역_생성_요청(StationRequest request) {
        return given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회() {
        return given()
                .when()
                .get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static Map<String, Long> convertStationResponses(JsonPath jsonPath) {
        List<Long> ids = jsonPath.getList(ID_KEY, Long.class);
        List<String> names = jsonPath.getList(NAME_KEY, String.class);

        return IntStream.range(0, ids.size())
                .boxed()
                .collect(Collectors.toMap(names::get, ids::get));
    }

    public static List<Long> convertStationIds(JsonPath jsonPath) {
        return jsonPath.getList("stations.id", Long.class);
    }
}