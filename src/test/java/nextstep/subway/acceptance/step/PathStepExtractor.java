package nextstep.subway.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class PathStepExtractor {
    public static class 경로_추출기 {
        public static List<String> 경로의_역_이름들을_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getList("stations.name");
        }

        public static Long 경로의_거리를_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getLong("distance");
        }
    }
}
