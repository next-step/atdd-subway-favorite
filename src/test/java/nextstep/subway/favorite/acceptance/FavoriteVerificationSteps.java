package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteVerificationSteps {

    public static void 지하철_즐겨찾기_추가_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 지하철_즐겨찾기_개수_확인(ExtractableResponse<Response> response, int count) {
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        assertThat(favoriteResponse.getFavorites).hasSize(count);
    }
}
