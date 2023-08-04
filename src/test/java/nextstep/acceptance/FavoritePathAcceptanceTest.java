package nextstep.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.commonStep.*;
import nextstep.dto.FavoritePathResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 즐겨찾기 관련 기능")
public class FavoritePathAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL1 = "email1@email.com";
    public static final String PASSWORD1 = "password1";
    public static final int AGE1 = 20;

    public static final String EMAIL2 = "email2@email.com";
    public static final String PASSWORD2 = "password2";
    public static final int AGE2 = 30;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 흑석역;
    private Long 교대강남구간거리;
    private Long 강남양재구간거리;

    private Long 이호선;
    private Long 신분당선;


    @BeforeEach
    public void setGivenDate(){
        교대역 =  StationStep.지하철역_생성("교대역").jsonPath().getLong("id");
        강남역 =  StationStep.지하철역_생성("강남역").jsonPath().getLong("id");
        양재역 =  StationStep.지하철역_생성("양재역").jsonPath().getLong("id");
        흑석역 =  StationStep.지하철역_생성("흑석역").jsonPath().getLong("id");

        이호선 =  LineStep.지하철_노선_생성( "2호선", "Green").jsonPath().getLong("id");
        신분당선 =  LineStep.지하철_노선_생성( "신분당선", "Red").jsonPath().getLong("id");

        교대강남구간거리 = 10L;
        강남양재구간거리 = 15L;

        SectionStep.지하철구간_생성(이호선,교대역,강남역,교대강남구간거리);
        SectionStep.지하철구간_생성(신분당선,강남역,양재역,강남양재구간거리);

    }

    /**
     * Given 로그인을 하고
     * When 즐겨찾기를 생성하면
     * Then 생성된 즐겨찾기의 기본키를 받을 수 있다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavoritePath() {

        //given
        String accessToken = FavoritePathStep.회원가입_및_토큰_받기(EMAIL1, PASSWORD1, AGE1);

        //when
        ExtractableResponse<Response> response = FavoritePathStep.즐겨찾기_생성(accessToken, 교대역, 강남역);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

    /**
     * Given 로그인을 하고
     * When 경로를 연결할 수 없는 즐겨찾기를 생성하면
     * Then Bad Request 400 error가 발생한다
     */
    @DisplayName("경로를 생성할 수 없는 즐겨찾기를 생성한다.")
    @Test
    void createInvalidFavoritePath() {

        //given
        String accessToken = FavoritePathStep.회원가입_및_토큰_받기(EMAIL1, PASSWORD1, AGE1);

        //when
        ExtractableResponse<Response> response = FavoritePathStep.즐겨찾기_생성(accessToken, 교대역, 흑석역);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 조회하면
     * Then 생성된 즐겨찾기 목록을 찾을 수 있다.
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavoritePathList() {
        //given
        String accessToken = FavoritePathStep.회원가입_및_토큰_받기(EMAIL1, PASSWORD1, AGE1);

        FavoritePathStep.즐겨찾기_생성(accessToken, 교대역, 강남역);
        FavoritePathStep.즐겨찾기_생성(accessToken, 강남역, 양재역);

        //when
        ExtractableResponse<Response> response = FavoritePathStep.즐겨찾기_목록_조회(accessToken);
        List<FavoritePathResponse> favoritePathResponseList = response.jsonPath().getList("", FavoritePathResponse.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(favoritePathResponseList.get(0).getSource().getId()).isEqualTo(교대역);
        assertThat(favoritePathResponseList.get(0).getTarget().getId()).isEqualTo(강남역);
        assertThat(favoritePathResponseList.get(1).getSource().getId()).isEqualTo(강남역);
        assertThat(favoritePathResponseList.get(1).getTarget().getId()).isEqualTo(양재역);

    }


    /**
     * Given 즐겨찾기를 생성하고
     * When 즐겨찾기를 삭제하면
     * Then 해당 즐겨찾기 정보는 삭제된다
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavoritePath() {
        //given
        String accessToken = FavoritePathStep.회원가입_및_토큰_받기(EMAIL1, PASSWORD1, AGE1);
        String favoritePathId = FavoritePathStep.즐겨찾기_생성(accessToken, 교대역, 강남역).header("Location");

        //when
        ExtractableResponse<Response> response = FavoritePathStep.즐겨찾기_삭제(accessToken, favoritePathId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 권한이 없는 사람이 즐겨찾기를 삭제하면
     * Then UNAUTHORIZED 401 error가 발생한다
     */
    @DisplayName("권한이 없는 사람이 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavoritePathUnauthorized() {
        //given

        String accessTokenOfCreater = FavoritePathStep.회원가입_및_토큰_받기(EMAIL1, PASSWORD1, AGE1);
        String favoritePathId = FavoritePathStep.즐겨찾기_생성(accessTokenOfCreater, 교대역, 강남역).header("Location");

        String accessTokenOfNonCreater = FavoritePathStep.회원가입_및_토큰_받기(EMAIL2, PASSWORD2, AGE2);

        //when
        ExtractableResponse<Response> deleteResponse = FavoritePathStep.즐겨찾기_삭제(accessTokenOfNonCreater, favoritePathId);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }
}
