package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given 역을 미리 추가해놓고
     * When 즐겨찾기를 추가하면
     * Then 즐겨찾기 조회 시 추가된 즐겨찾기 역들을 확인할 수 있다
     */
    @DisplayName("즐겨찾기 추가")
    @Test
    void createFavorite() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(교대역, 양재역, ACCESS_TOKEN);

        //then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        assertThat(getResponse.jsonPath().getList("source.name", String.class).get(0)).isEqualTo("교대역");
        assertThat(getResponse.jsonPath().getList("target.name", String.class).get(0)).isEqualTo("양재역");
    }

    /**
     * Given 역을 미리 추가해놓고
     * When 즐겨찾기를 추가 시 인증되지 않으면
     * Then 에러가 발생한다
     */
    @DisplayName("즐겨찾기 추가 시 미 인증 에러")
    @Test
    void createFavoriteAuthenticateException() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");

        //when
        final String weirdToken = "abcd";
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(교대역, 양재역, weirdToken);

        //then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }


    /**
     * Given 역을 미리 추가해놓고
     * When 권한이 없는 계정으로 즐겨찾기를 추가 시
     * Then 에러가 발생한다
     */
    @DisplayName("즐겨찾기 추가 시 권한이 없을 때")
    @Test
    void createFavoriteUnauthorizedException() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");

        //when
        String accessToken = 로그인_되어_있음("member@email.com", "password");
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(교대역, 양재역, accessToken);

        //then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }


    /**
     * Given 역과 즐겨찾기를 추가해놓고
     * When 즐겨찾기를 조회하면
     * Then 추가된 즐겨찾기 역들을 확인할 수 있다
     */
    @DisplayName("즐겨찾기 조회")
    @Test
    void showFavorites() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");


        즐겨찾기_생성_요청(교대역, 양재역, ACCESS_TOKEN);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(response.jsonPath().getList("source.name", String.class).get(0)).isEqualTo("교대역");
        assertThat(response.jsonPath().getList("target.name", String.class).get(0)).isEqualTo("양재역");
    }

    /**
     * Given 역과 즐겨찾기를 추가해놓고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기 조회 시 삭제됨을 확인할 수 있다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");


        Long 즐겨찾기 = 즐겨찾기_생성_요청(교대역, 양재역, ACCESS_TOKEN).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> deletedResponse = 즐겨찾기_목록_삭제_요청(즐겨찾기, ACCESS_TOKEN);

        //then
        assertThat(deletedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> getResponse = 즐겨찾기_목록_조회_요청();
        assertThat(getResponse.jsonPath().getList("")).isEmpty();
    }


    /**
     * Given 역과 즐겨찾기를 추가해놓고
     * When 즐겨찾기를 삭제 시 인증되지 않으면
     * Then 에러가 발생한다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavoriteAuthenticationException() {
        //given
        long 교대역 = 지하철역_생성_요청("교대역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 남부터미널역 = 지하철역_생성_요청("남부터미널역", ACCESS_TOKEN).jsonPath().getLong("id");
        long 양재역 = 지하철역_생성_요청("양재역", ACCESS_TOKEN).jsonPath().getLong("id");


        Long 즐겨찾기 = 즐겨찾기_생성_요청(교대역, 양재역, ACCESS_TOKEN).jsonPath().getLong("id");

        //when
        final String weirdToken = "abcd";
        ExtractableResponse<Response> deletedResponse = 즐겨찾기_목록_삭제_요청(즐겨찾기, weirdToken);

        //then
        assertThat(deletedResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
