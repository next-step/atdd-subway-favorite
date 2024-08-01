package nextstep.subway.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class StationStepExtractor {
    public static class 역_추출기 {
        public static Long 단일_id_를_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getLong("id");
        }

        public static List<String> 모든_역의_이름을_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getList("name", String.class);
        }
    }
}
