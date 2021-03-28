package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteVerificationSteps {

    public static void 지하철_즐겨찾기_추가_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 지하철_즐겨찾기_추가_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    public static void 지하철_즐겨찾기_미인증_회원_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 지하철_즐겨찾기_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_즐겨찾기_조회_결과_확인(ExtractableResponse<Response> response, List<StationResponse> expectedStationResponses) {
        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
        List<Long> resultStationResponses = favoriteResponses.stream()
                .flatMap(favoriteResponse -> Stream.of(favoriteResponse.getSource(), favoriteResponse.getTarget()))
                .map(StationResponse::getId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> expectedStationResponseIds = expectedStationResponses.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultStationResponses).containsAll(expectedStationResponseIds);
    }

    public static void 지하철_즐겨찾기_제거_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static Long 지하철_즐겨찾기_생성된_ID(ExtractableResponse<Response> response) {
        String location = response.header("Location");
        return Long.valueOf(location.split("/")[2]);
    }

    public static void 지하철_즐겨찾기_제거_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}
