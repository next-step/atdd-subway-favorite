package nextstep.subway.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class FavoriteStepExtractor {
    public static class 즐겨찾기_추출기 {
        public static Long 단일_응답의_id_를_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getLong("id");
        }

        public static String 단일_응답의_출발역_이름을_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getString("source.name");
        }

        public static String 단일_응답의_도착역_이름을_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getString("target.name");
        }

        public static List<Long> 즐겨찾기_목록에_포함된_id_를_추출한다(ExtractableResponse<Response> response) {
            return response.jsonPath().getList("id");
        }
    }
}
