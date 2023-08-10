package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.response.StationResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.favorite.acceptance.steps.FavoriteSteps.*;
import static nextstep.member.acceptance.MemberSteps.회원_로그인_토큰_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.steps.StationSteps.지하철역_생성_응답;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 교대역, 양재역;
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    @BeforeEach
    void set() {

        교대역 = 지하철역_생성_응답("교대역");
        양재역 = 지하철역_생성_응답("양재역");

        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        var accessToken = 회원_로그인_토큰_요청(EMAIL, PASSWORD);
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 교대역.getId(), 양재역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {
        var accessToken = 회원_로그인_토큰_요청(EMAIL, PASSWORD);
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 교대역.getId(), 양재역.getId());
        FavoriteResponse favorite = createResponse.as(FavoriteResponse.class);

        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);
        List<FavoriteResponse> findFavorites = response.jsonPath().getList("", FavoriteResponse.class);

        List<FavoriteResponse> expected = List.of(favorite);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findFavorites).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorites() {
        var accessToken = 회원_로그인_토큰_요청(EMAIL, PASSWORD);
        FavoriteResponse favorite = 즐겨찾기_생성_응답(accessToken, 교대역.getId(), 양재역.getId());

        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(accessToken, favorite.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
