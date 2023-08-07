package nextstep.subway.acceptance.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AcceptanceTest;
import nextstep.subway.dto.FavoriteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.common.CommonSteps.*;
import static nextstep.member.acceptance.MemberSteps.*;
import static nextstep.subway.acceptance.favorite.FavoriteTestUtils.*;
import static nextstep.subway.acceptance.line.LineTestUtils.*;
import static nextstep.subway.acceptance.station.StationTestUtils.*;
import static org.assertj.core.api.Assertions.*;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    String 교대역_URL;
    String 강남역_URL;
    String 이호선_URL;
    String accessToken;
    String sourceStationId;
    String targetStationId;

    final String INVALID_TOKEN = "invalid_token";

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역_URL = 지하철역_생성(교대역_정보);
        강남역_URL = 지하철역_생성(강남역_정보);
        이호선_URL= 지하철_노선_생성(이호선_생성_요청, 교대역_URL, 강남역_URL, 10);

        회원_생성_요청(properUser.getEmail(), properUser.getPassword(), properUser.getAge());
        accessToken = 토큰_기반_로그인_요청(properUser.getEmail(), properUser.getPassword()).jsonPath().getString("accessToken");

        sourceStationId = String.valueOf(지하철_아이디_획득(교대역_URL));
        targetStationId = String.valueOf(지하철_아이디_획득(강남역_URL));
    }


    @DisplayName("즐겨찾기를 생성한다")
    @Nested
    class CreateFavorite {
        @Test
        void success() {
            // when
            즐겨찾기_등록_요청_성공(accessToken, sourceStationId, targetStationId);

            // then
            List<FavoriteResponse> favoriteResponses = 즐겨찾기_조회_요청_성공(accessToken);
            assertThat(favoriteResponses.size()).isEqualTo(1);
        }

        @DisplayName("실패한다")
        @Nested
        class Fail {
            @DisplayName("유효하지 않은 경로로 즐겨찾기 등록 요청")
            @Test
            void invalidPath() {
                // given
                String 익명역_URL = 지하철역_생성(익명역_정보);
                String 연결돼있지_않은_역_id = String.valueOf(지하철_아이디_획득(익명역_URL));

                // when
                ExtractableResponse<Response> response = 즐겨찾기_등록_요청(accessToken, sourceStationId, 연결돼있지_않은_역_id);

                // then
                checkHttpResponseCode(response, HttpStatus.BAD_REQUEST);
            }

            @DisplayName("미인증 요청")
            @Test
            void unauthenticated() {
                // given
                즐겨찾기_등록_요청_성공(accessToken, sourceStationId, targetStationId);

                // when
                ExtractableResponse<Response> createResponse = 즐겨찾기_등록_요청(INVALID_TOKEN, sourceStationId, targetStationId);

                // then
                checkHttpResponseCode(createResponse, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @DisplayName("즐겨찾기를 조회한다")
    @Nested
    class GetFavorite {
        @Test
        void success() {
            // given
            즐겨찾기_등록_요청_성공(accessToken, sourceStationId, targetStationId);

            // when
            List<FavoriteResponse> responses = 즐겨찾기_조회_요청_성공(accessToken);

            // then
            assertThat(responses.size()).isEqualTo(1);
        }

        @DisplayName("실패한다")
        @Nested
        class Fail {
            @DisplayName("미인증 요청")
            @Test
            void unauthenticated() {
                // given
                즐겨찾기_등록_요청_성공(accessToken, sourceStationId, targetStationId);

                // given
                ExtractableResponse<Response> getResponse = 즐겨찾기_조회_요청(INVALID_TOKEN);

                // then
                checkHttpResponseCode(getResponse, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @DisplayName("즐겨찾기를 삭제한다")
    @Nested
    class DeleteFavorite {
        @Test
        void success() {
            // given
            String 등록된_즐겨찾기_URL = 즐겨찾기_등록_요청_성공(accessToken, sourceStationId, targetStationId);

            // when
            즐겨찾기_삭제_요청_성공(accessToken, 등록된_즐겨찾기_URL);

            // then
            List<FavoriteResponse> favoriteResponses = 즐겨찾기_조회_요청_성공(accessToken);
            assertThat(favoriteResponses.size()).isEqualTo(0);
        }

        @DisplayName("실패한다")
        @Nested
        class Fail {
            @DisplayName("미인증 요청")
            @Test
            void unauthenticated() {
                // given
                String 등록된_즐겨찾기_URL = 즐겨찾기_등록_요청_성공(accessToken, sourceStationId, targetStationId);

                // when
                ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(INVALID_TOKEN, 등록된_즐겨찾기_URL);

                // then
                checkHttpResponseCode(deleteResponse, HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
