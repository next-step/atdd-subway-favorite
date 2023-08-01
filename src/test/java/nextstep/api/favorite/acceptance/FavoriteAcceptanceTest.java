package nextstep.api.favorite.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import static nextstep.api.auth.acceptance.AuthSteps.일반_로그인_요청;
import static nextstep.api.favorite.acceptance.FavoriteSteps.모든_즐겨찾기_조회를_요청한다;
import static nextstep.api.favorite.acceptance.FavoriteSteps.모든_즐겨찾기_조회에_성공한다;
import static nextstep.api.favorite.acceptance.FavoriteSteps.즐겨찾기_생성에_성공한다;
import static nextstep.api.favorite.acceptance.FavoriteSteps.즐겨찾기_생성을_요청한다;
import static nextstep.api.favorite.acceptance.FavoriteSteps.즐겨찾기_제거를_요청한다;
import static nextstep.api.favorite.acceptance.FavoriteSteps.즐겨찾기_제거에_성공한다;
import static nextstep.api.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.api.subway.acceptance.line.LineSteps.지하철노선을_생성한다;
import static nextstep.api.subway.acceptance.station.StationSteps.지하철역을_생성한다;
import static nextstep.utils.AcceptanceHelper.statusCodeShouldBe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import nextstep.api.auth.application.token.dto.TokenResponse;
import nextstep.utils.AcceptanceTest;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 정자역, 판교역, 잠실역;
    private String token;
    private String invalidToken = "invalidToken";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        prepareSubwayGraph();
        prepareMember();
    }

    private void prepareSubwayGraph() {
        정자역 = 지하철역을_생성한다("정자역").getId();
        판교역 = 지하철역을_생성한다("판교역").getId();
        지하철노선을_생성한다(정자역, 판교역, 10).getId();

        잠실역 = 지하철역을_생성한다("잠실역").getId();
    }

    private void prepareMember() {
        final var email = "user@gmail.com";
        final var password = "password";

        회원_생성_요청(email, password, 20);
        token = 일반_로그인_요청(email, password).extract().as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 생성한다")
    @Nested
    class createFavorite {

        @Test
        void success() {
            // when
            즐겨찾기_생성에_성공한다(token, 정자역, 판교역);

            // then
            final var response = 모든_즐겨찾기_조회에_성공한다(token);
            assertThat(response).hasSize(1);
        }

        @Nested
        class fail {

            @Test
            void 로그인한_상태여야_한다() {
                final var response = 즐겨찾기_생성을_요청한다(invalidToken, 정자역, 판교역);
                statusCodeShouldBe(response, HttpStatus.UNAUTHORIZED);
            }

            @Test
            void 존재하는_상행역이어야_한다() {
                final var response = 즐겨찾기_생성을_요청한다(token, 0L, 판교역);
                statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
            }

            @Test
            void 존재하는_하행역이어야_한다() {
                final var response = 즐겨찾기_생성을_요청한다(token, 정자역, 0L);
                statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
            }

            @Test
            void 상행역과_하행역은_연결되어있어야_한다() {
                final var response = 즐겨찾기_생성을_요청한다(token, 잠실역, 판교역);
                statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @DisplayName("즐겨찾기를 조회한다")
    @Nested
    class showLines {

        @Test
        void success() {
            // given
            즐겨찾기_생성에_성공한다(token, 정자역, 판교역);

            // when
            final var response = 모든_즐겨찾기_조회에_성공한다(token);

            // then
            assertThat(response).hasSize(1);
        }

        @Nested
        class fail {

            @Test
            void 로그인한_상태여야_한다() {
                final var response = 모든_즐겨찾기_조회를_요청한다(invalidToken);
                statusCodeShouldBe(response, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @DisplayName("즐겨찾기 삭제에 성공한다")
    @Nested
    class deleteLine {

        @Test
        void success() {
            // given
            즐겨찾기_생성에_성공한다(token, 정자역, 판교역);
            final var favoriteId = 모든_즐겨찾기_조회에_성공한다(token).get(0).getId();

            // when
            즐겨찾기_제거에_성공한다(token, favoriteId);

            // then
            final var response = 모든_즐겨찾기_조회에_성공한다(token);
            assertThat(response).isEmpty();
        }

        @Nested
        class fail {

            @Test
            void 로그인한_상태여야_한다() {
                // given
                즐겨찾기_생성에_성공한다(token, 정자역, 판교역);
                final var favoriteId = 모든_즐겨찾기_조회에_성공한다(token).get(0).getId();

                // when & then
                final var response = 즐겨찾기_제거를_요청한다(invalidToken, favoriteId);
                statusCodeShouldBe(response, HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
