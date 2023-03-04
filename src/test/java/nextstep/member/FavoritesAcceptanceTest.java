package nextstep.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static nextstep.member.FavoritesSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoritesAcceptanceTest extends AcceptanceTest {

    Long source = 3L;
    Long target = 1L;

    @DisplayName("즐겨찾기를 추가 할 수 있다.")
    @Test
    void favoriteAdd() {
        ExtractableResponse<Response> response = 즐겨찾기_추가(source, target);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();

    }

    @DisplayName("즐겨찾기를 조회할 수 있다.")
    @Test
    void favoritesFind() {
        ExtractableResponse<Response> response = 즐겨찾기_조회();
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(source);
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(target);
    }

    @DisplayName("즐겨찾기를 삭제할 수 있다.")
    @Test
    void favoritesDelete() {
        ExtractableResponse<Response> response = 즐겨찾기_삭제(1L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }


}
