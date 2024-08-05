package nextstep.subway.acceptance.common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;

public class SubwayUtils {

    public static Long responseToId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    public static String responseToName(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("name");
    }

    public static List<String> responseToNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }

    public static String responseToLocation(ExtractableResponse<Response> response) {
        return response.header("Location");
    }
}
