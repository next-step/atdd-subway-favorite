package nextstep.favorite.acceptance;

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
import static nextstep.subway.acceptance.steps.LineSteps.지하철_노선_생성;
import static nextstep.subway.acceptance.steps.StationSteps.지하철역_생성_응답;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("즐겨찾기 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 교대역,강남역, 양재역;
    private static final int DEFAULT_DISTANCE = 10;
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private static final String NO_PERMISSION="noPermission";


    @BeforeEach
    void set() {

        교대역 = 지하철역_생성_응답("교대역");
        강남역 = 지하철역_생성_응답("강남역");
        양재역 = 지하철역_생성_응답("양재역");

        지하철_노선_생성("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), DEFAULT_DISTANCE);
        지하철_노선_생성("2호선", "bg-green-600", 강남역.getId(), 교대역.getId(), DEFAULT_DISTANCE);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        var accessToken = 회원_로그인_토큰_요청(EMAIL, PASSWORD);
        var response = 즐겨찾기_생성_요청(accessToken, 교대역.getId(), 양재역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("즐겨찾기 생성에 실패한다 - 로그인되지 않은 경우 에러를 던진다.")
    @Test
    void createFavoriteExceptionWhenWithoutPermission() {
        var response = 즐겨찾기_생성_요청(NO_PERMISSION, 교대역.getId(), 양재역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {
        var accessToken = 회원_로그인_토큰_요청(EMAIL, PASSWORD);
        var createResponse = 즐겨찾기_생성_요청(accessToken, 교대역.getId(), 양재역.getId());
        var favorite = createResponse.as(FavoriteResponse.class);

        var response = 즐겨찾기_조회_요청(accessToken);
        var findFavorites = response.jsonPath().getList("", FavoriteResponse.class);

        var expected = List.of(favorite);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findFavorites).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("즐겨찾기 조회에 실패한다 - 로그인되지 않은 경우 에러를 던진다.")
    @Test
    void findFavoriteExceptionWhenWithoutPermission() {
        var accessToken = 회원_로그인_토큰_요청(EMAIL, PASSWORD);
        var createResponse = 즐겨찾기_생성_요청(accessToken, 교대역.getId(), 양재역.getId());
        createResponse.as(FavoriteResponse.class);

        var response = 즐겨찾기_조회_요청(NO_PERMISSION);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorites() {
        var accessToken = 회원_로그인_토큰_요청(EMAIL, PASSWORD);
        var favorite = 즐겨찾기_생성_응답(accessToken, 교대역.getId(), 양재역.getId());

        var response = 즐겨찾기_삭제_요청(accessToken, favorite.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @DisplayName("즐겨찾기 삭제에 실패한다 - 로그인되지 않은 경우 에러를 던진다.")
    @Test
    void deleteFavoriteExceptionWhenWithoutPermission() {
        var accessToken = 회원_로그인_토큰_요청(EMAIL, PASSWORD);
        var favorite = 즐겨찾기_생성_응답(accessToken, 교대역.getId(), 양재역.getId());

        var response = 즐겨찾기_삭제_요청(NO_PERMISSION, favorite.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
