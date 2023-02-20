package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@ExtendWith(MockitoExtension.class)
class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;

    @BeforeEach
    void makeStation() {
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 2L);
        삼성역 = new Station("삼성역");
        ReflectionTestUtils.setField(삼성역, "id", 3L);
    }

    @Nested
    @DisplayName("로그인")
    class login {

        private String accessToken;

        @BeforeAll
        void login() {
            //given 로그인 한다.
            accessToken = 베어러_인증_로그인_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        }

        @Test
        @DisplayName("즐겨찾기 추가")
        void addFavorite() {
            //when 즐겨찾기를 추가한다.
            즐겨찾기_추가(accessToken, String.valueOf(강남역.getId()), String.valueOf(역삼역.getId()));
            //then 추가한 즐겨찾기가 조회된다.
            ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

            assertAll(
                    () -> assertThat(response.jsonPath().getString("source.name")).isEqualTo("강남역"),
                    () -> assertThat(response.jsonPath().getString("target.name")).isEqualTo("역삼역"),
                    () -> assertThat(response.jsonPath().getList("name")).containsOnly("강남역", "역삼역")
            );
        }

        @Test
        @DisplayName("즐겨찾기 조회")
        void getFavorites() {
            //when 즐겨찾기를 추가한다.
            즐겨찾기_추가(accessToken, String.valueOf(강남역.getId()), String.valueOf(역삼역.getId()));
            즐겨찾기_추가(accessToken, String.valueOf(강남역.getId()), String.valueOf(삼성역.getId()));
            //then 추가한 즐겨찾기가 조회된다.
            ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

            assertAll(
                    () -> assertThat(response.jsonPath()
                            .getList("source.name", String.class)).containsOnly("강남역", "강남역"),
                    () -> assertThat(response.jsonPath()
                            .getList("target.name", String.class)).containsOnly("역삼역", "삼성역")
            );
        }

        @Test
        @DisplayName("즐겨찾기 삭제")
        void deleteFavorite() {
            //given 즐겨찾기를 추가한다.
            즐겨찾기_추가(accessToken, String.valueOf(강남역.getId()), String.valueOf(역삼역.getId()));
            ExtractableResponse<Response> addResponse = 즐겨찾기_추가(accessToken, String.valueOf(강남역.getId()), String.valueOf(삼성역.getId()));
            //when 즐겨찾기를 삭제한다.
            String location = addResponse.header("Location");
            즐겨찾기_삭제(accessToken, location);
            //then 추가한 즐겨찾기가 조회된다.
            ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

            assertAll(
                    () -> assertThat(response.jsonPath()
                            .getList("source.name", String.class)).containsOnly("강남역"),
                    () -> assertThat(response.jsonPath()
                            .getList("target.name", String.class)).containsOnly("역삼역")
            );
        }
    }

    @Nested
    @DisplayName("로그인 안함")
    class notLogin {

        //given 로그인을 하지 않는다.
        private String accessToken;

        @Test
        @DisplayName("즐겨찾기 추가")
        void addFavorite() {
            //when 즐겨찾기를 추가한다.
            ExtractableResponse<Response> response = 즐겨찾기_추가(accessToken, String.valueOf(강남역.getId()), String.valueOf(역삼역.getId()));
            //then UNAUTHORIZED 응답을 받는다.
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        @DisplayName("즐겨찾기 조회")
        void getFavorites() {
            //when 즐겨찾기를 추가한다.
            ExtractableResponse<Response> response = 즐겨찾기_추가(accessToken, String.valueOf(강남역.getId()), String.valueOf(역삼역.getId()));
            //then  401 UNAUTHORIZED 응답을 받는다.
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        @DisplayName("즐겨찾기 삭제")
        void deleteFavorite() {
            //when 즐겨찾기를 삭제한다.
            ExtractableResponse<Response> response = 즐겨찾기_삭제(accessToken, "1");
            //then  401 UNAUTHORIZED 응답을 받는다.
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
