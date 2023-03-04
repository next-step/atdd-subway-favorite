package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps extends Steps {

    private static final String URI = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(final String accessToken, final long source, final long target) {
        final Map<String, Object> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return 사용자_인증_요청(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(URI)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기가_정상적으로_추가되었음을_확인(final String accessToken, final ExtractableResponse<Response> response, final long source, final long target) {
        응답_코드_검증(response, HttpStatus.CREATED);
        final ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회함(accessToken);
        assertThat(listResponse.jsonPath().getList("$.findAll{it -> it.source.id ==" + source + " && it.target.id ==" + target + "}").size()).isEqualTo(1);
    }

    public static void 즐겨찾기_추가_요청함(final String accessToken, final long source, final long target) {
        final ExtractableResponse<Response> response = 즐겨찾기_추가_요청(accessToken, source, target);
        응답_코드_검증(response, HttpStatus.CREATED);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String accessToken) {
        return 사용자_인증_요청(accessToken)
                .when().get(URI)
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 즐겨찾기_목록_조회함(final String accessToken) {
        final ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(accessToken);
        응답_코드_검증(response, HttpStatus.OK);
        return response;
    }

    public static void 즐겨찾기_목록_조회가_정상적으로_되었는지_확인(final ExtractableResponse<Response> response, final int size) {
        응답_코드_검증(response, HttpStatus.OK);
        assertThat(response.jsonPath().getList("id").size()).isEqualTo(size);
    }
}
