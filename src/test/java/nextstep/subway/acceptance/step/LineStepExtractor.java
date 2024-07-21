package nextstep.subway.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class LineStepExtractor {
    public static class 노선_추출기 {
        public static Long 단일_id_를_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getLong("id");
        }

        public static String 단일_노선명을_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getString("name");
        }

        public static String 단일_색상을_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getString("color");
        }

        public static List<String> 단일_노선에_포함된_역_이름을_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getList("stations.name");
        }

        public static List<String> 모든_노선명_목록을_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getList("name");
        }

        public static List<String> 모든_노선에_포함된_역_이름을_추출한다(ExtractableResponse<Response> response, String lineName) {
            String path = "find { it.name == '" + lineName + "' }.stations.name";
            return response.jsonPath().getList(path);
        }
    }
}
