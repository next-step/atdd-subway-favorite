package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private Long 강남역;
    private Long 역삼역;
    private Long 삼성역;
    private static final String 강남역_이름 = "강남역";
    private static final String 삼성역_이름 = "삼성역";
    private static final String 역삼역_이름 = "역삼역";

    @BeforeEach
    void makeStation() {

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        역삼역 = 지하철역_생성_요청("역삼역").jsonPath().getLong("id");
        삼성역 = 지하철역_생성_요청("삼성역").jsonPath().getLong("id");
    }

    @Nested
    @DisplayName("로그인")
    class login {

        private String accessToken;

        @BeforeEach
        void login() {
            //given 로그인 한다.
            accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        }

        @Test
        @DisplayName("즐겨찾기 추가")
        void addFavorite() {
            //when 즐겨찾기를 추가한다.
            ExtractableResponse<Response> createResponse = 즐겨찾기_추가(accessToken, String.valueOf(강남역), String.valueOf(역삼역));
            //then 추가한 즐겨찾기가 조회된다.
            ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

            assertAll(
                    () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                    () -> assertThat(response.jsonPath().getList("source.name", String.class)).containsOnly(강남역_이름),
                    () -> assertThat(response.jsonPath().getList("target.name", String.class)).containsOnly(역삼역_이름)
            );
        }

        @Test
        @DisplayName("즐겨찾기 조회")
        void getFavorites() {
            //when 즐겨찾기를 추가한다.
            즐겨찾기_추가(accessToken, String.valueOf(강남역), String.valueOf(역삼역));
            즐겨찾기_추가(accessToken, String.valueOf(강남역), String.valueOf(삼성역));
            //then 추가한 즐겨찾기가 조회된다.
            ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(response.jsonPath()
                            .getList("source.name", String.class)).containsOnly(강남역_이름, 강남역_이름),
                    () -> assertThat(response.jsonPath()
                            .getList("target.name", String.class)).containsOnly(역삼역_이름, 삼성역_이름)
            );
        }

        @Test
        @DisplayName("즐겨찾기 삭제")
        void deleteFavorite() {
            //given 즐겨찾기를 추가한다.
            즐겨찾기_추가(accessToken, String.valueOf(강남역), String.valueOf(역삼역));
            ExtractableResponse<Response> addResponse = 즐겨찾기_추가(accessToken, String.valueOf(강남역), String.valueOf(삼성역));
            //when 즐겨찾기를 삭제한다.
            ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제(accessToken, addResponse.header("Location"));
            //then 추가한 즐겨찾기가 조회된다.
            ExtractableResponse<Response> findResponse = 즐겨찾기_조회(accessToken);

            assertAll(
                    () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                    () -> assertThat(findResponse.jsonPath()
                            .getList("source.name", String.class)).containsOnly(강남역_이름),
                    () -> assertThat(findResponse.jsonPath()
                            .getList("target.name", String.class)).containsOnly(역삼역_이름)
            );
        }
    }

    @Nested
    @DisplayName("로그인 안함")
    class notLogin {

        //given 로그인을 하지 않는다.
        private String accessToken = "0000";

        @Test
        @DisplayName("즐겨찾기 추가")
        void addFavorite() {
            //when 즐겨찾기를 추가한다.
            ExtractableResponse<Response> response = 즐겨찾기_추가(accessToken, String.valueOf(강남역), String.valueOf(역삼역));
            //then UNAUTHORIZED 응답을 받는다.
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        @DisplayName("즐겨찾기 조회")
        void getFavorites() {
            //when 즐겨찾기를 추가한다.
            ExtractableResponse<Response> response = 즐겨찾기_추가(accessToken, String.valueOf(강남역), String.valueOf(역삼역));
            //then  401 UNAUTHORIZED 응답을 받는다.
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        @DisplayName("즐겨찾기 삭제")
        void deleteFavorite() {
            //when 즐겨찾기를 삭제한다.
            ExtractableResponse<Response> response = 즐겨찾기_삭제(accessToken, "/favorites/1");
            //then  401 UNAUTHORIZED 응답을 받는다.
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
