package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.AuthAcceptanceTest.깃허브_인증_로그인_요청_파라미터_생성;
import static nextstep.subway.acceptance.AuthSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.FavoriteSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final Long SOURCE_ID = 1L;
    private static final Long TARGET_ID = 2L;

    /**
     * Given : 깃허브 로그인을 하고
     * When  : 구간 즐겨찾기를 하면
     * Then  : 즐겨찾기가 등록된다
     */
    @DisplayName("즐거찾기 등록")
    @Test
    void favoriteCreate() {
        //given
        ExtractableResponse<Response> 깃허브_로그인_응답 = 깃허브_인증_로그인_요청(깃허브_인증_로그인_요청_파라미터_생성());

        //when
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(깃허브_로그인_응답, SOURCE_ID, TARGET_ID);

        //then
        expectHttpStatus(response, HttpStatus.CREATED);
    }

    /**
     * Given : 깃허브 로그인을 하고
     * and   : 즐겨찾기 등록을 하고
     * When  : 즐겨찾기 조회를 하면
     * Then  : 즐겨찾기 목록이 조회된다
     */
    @DisplayName("즐겨 찾기 조회")
    @Test
    void favoriteList() {
        //given
        ExtractableResponse<Response> 깃허브_로그인_응답 = 깃허브_인증_로그인_요청(깃허브_인증_로그인_요청_파라미터_생성());
        즐겨찾기_등록_요청(깃허브_로그인_응답, SOURCE_ID, TARGET_ID);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(깃허브_로그인_응답);

        expectHttpStatus(response, HttpStatus.OK);
        assertThat(response.jsonPath().getLong("source.id")).isEqualTo(SOURCE_ID);
        assertThat(response.jsonPath().getLong("target.id")).isEqualTo(TARGET_ID);
    }

    /**
     * Given : 깃허브 로그인을 하고
     * and   : 즐겨찾기 등록을 하고
     * When  : 즐겨찾기 삭제를 하면
     * Then  : 즐겨찾기가 삭제된다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void favoriteDelete() {
        //given
        ExtractableResponse<Response> 깃허브_로그인_응답 = 깃허브_인증_로그인_요청(깃허브_인증_로그인_요청_파라미터_생성());
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(깃허브_로그인_응답, SOURCE_ID, TARGET_ID);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(깃허브_로그인_응답, 즐겨찾기_등록_응답);
        expectHttpStatus(response, HttpStatus.NO_CONTENT);
    }

    private static void expectHttpStatus(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
