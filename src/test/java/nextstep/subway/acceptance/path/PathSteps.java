package nextstep.subway.acceptance.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.line.LineSteps;
import org.springframework.http.MediaType;

import java.util.Map;

public class PathSteps {

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", source, target)
                .then().log().all().extract();
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance) {
        Map<String, String> lineCreateParams = Map.of(
                "name", name,
                "color", color,
                "upStationId", String.valueOf(upStation),
                "downStationId", String.valueOf(downStation),
                "distance", String.valueOf(distance)
        );
        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }
}
