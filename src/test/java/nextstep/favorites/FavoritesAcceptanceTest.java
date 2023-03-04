package nextstep.favorites;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.favorites.application.dto.FavoriteResponse;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static nextstep.favorites.FavoritesSteps.*;
import static nextstep.member.LoginSteps.베어러_인증_로그인_요청;
import static nextstep.member.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoritesAcceptanceTest extends AcceptanceTest {

    Long 강남역;
    Long 양재역;

    String EMAIL = "email@email.com";
    String PASSWORD = "password";
    int AGE = 20;

    String 인증정보;




    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        회원_생성_요청(EMAIL, PASSWORD, AGE);

         인증정보 = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    @DisplayName("즐겨찾기를 추가 할 수 있다.")
    @Test
    void favoriteAdd() {
        ExtractableResponse<Response> response = 즐겨찾기_추가(인증정보, 강남역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();

    }

    @DisplayName("즐겨찾기를 조회할 수 있다.")
    @Test
    void favoritesFind() {
        ExtractableResponse<Response> response = 즐겨찾기_조회(인증정보);
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(강남역);
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(양재역);
    }

    @DisplayName("즐겨찾기를 삭제할 수 있다.")
    @Test
    void favoritesDelete() {
        ExtractableResponse<Response> response = 즐겨찾기_삭제(1L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }


}
