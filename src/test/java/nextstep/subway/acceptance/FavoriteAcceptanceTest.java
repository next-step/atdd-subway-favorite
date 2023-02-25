package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final Long SOURCE_ID = 0L;
    private static final Long TARGET_ID = 1L;

    /**
     * Given : jwt 로그인을 하고
     * When  : 구간 즐겨찾기를 하면
     * Then  : 즐겨찾기가 등록된다
     */
    @DisplayName("즐거찾기 등록")
    @Test
    void favoriteCreate() {
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(SOURCE_ID, TARGET_ID);
        expectHttpStatus(response, HttpStatus.CREATED);
    }



    /**
     * Given : jwt 로그인을 하고
     * and   : 즐겨찾기 등록을 하고
     * When  : 즐겨찾기 조회를 하면
     * Then  : 즐겨찾기 목록이 조회된다
     */
    @DisplayName("증겨 찾기 조회")
    @Test
    void favoriteList() {
    }

    /**
     * Given : jwt 로그인을 하고
     * and   : 즐겨찾기 등록을 하고
     * When  : 즐겨찾기 삭제를 하면
     * Then  : 즐겨찾기가 삭제된다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void favoriteDelete() {
    }

    private static void expectHttpStatus(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
