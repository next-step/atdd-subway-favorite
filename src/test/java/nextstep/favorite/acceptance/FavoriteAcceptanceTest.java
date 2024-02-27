package nextstep.favorite.acceptance;

import nextstep.utils.AcceptanceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static nextstep.utils.api.FavoriteApi.*;
import static nextstep.utils.api.FavoriteApi.즐겨찾기를_삭제한다;
import static nextstep.utils.api.LineApi.*;
import static nextstep.utils.api.MemberApi.*;
import static nextstep.utils.api.StationApi.지하철역_생성요청;
import static nextstep.utils.api.TokenApi.*;
import static nextstep.utils.fixture.LineFixture.신분당선_생성_바디;
import static nextstep.utils.fixture.SectionFixture.추가구간_생성_바디;
import static nextstep.utils.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    final String 회원_이메일 = "test@test.com";
    final String 회원_비밀번호 = "test1234!!";
    final Map<String, Long> 강남역에서_신논현역_즐겨찾기 = new HashMap<>();

    // XXX: dataLoader로 이동?
    @BeforeEach
    void setUpMember() {
        // given
        회원_생성_요청(회원_이메일, 회원_비밀번호, 18);
        Long 강남역ID = 지하철역_생성요청(강남역).getLong("id");
        Long 논현역ID = 지하철역_생성요청(논현역).getLong("id");
        Long 신논현역ID = 지하철역_생성요청(신논현역).getLong("id");
        Long 신분당선ID = 노선생성요청(신분당선_생성_바디(강남역ID, 논현역ID)).getLong("id");
        구간생성요청(신분당선ID, 추가구간_생성_바디(논현역ID, 신논현역ID));

        강남역에서_신논현역_즐겨찾기.put("source", 강남역ID);
        강남역에서_신논현역_즐겨찾기.put("target", 신논현역ID);
    }

    @Nested
    class AddFavorite {
      /**
       * Given 사용자가 로그인 되어있을 때
       * When 즐겨찾기를 추가하면
       * Then 즐겨찾기 조회 시 추가한 즐겨찾기가 조회된다.
       */
        @Test
        @DisplayName("로그인한 사용자는 즐겨찾기를 추가할 수 있다.")
        void succeed() {
            // Given
            ExtractableResponse<Response> response = 로그인을_시도한다(회원_이메일, 회원_비밀번호);
            String 인증_토큰 = response.jsonPath().getString("accessToken");

            // When
            assertThat(즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기, 인증_토큰).statusCode()).isEqualTo(201);

            // Then
            ExtractableResponse<Response> 즐겨찾기_조회_결과 = 즐겨찾기_목록을_조회한다(인증_토큰);

            assertThat(즐겨찾기_조회_결과.jsonPath().getString("[0].source.name")).isEqualTo("강남역");
            assertThat(즐겨찾기_조회_결과.jsonPath().getString("[0].target.name")).isEqualTo("신논현역");
        }

        /**
         * Given 사용자가 로그인 되어있지 않을 때
         * When 즐겨찾기를 추가하면
         * Then 즐겨찾기 조회 시 추가한 즐겨찾기가 조회되지 않는다.
         */
        @Test
        @DisplayName("로그인하지 않은 사용자는 즐겨찾기를 추가할 수 없다.")
        void failForNotLogin() {
          // When
          // Then
          assertThat(즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기).statusCode()).isEqualTo(401);
        }

        /**
         * Given 이미 즐겨찾기에 추가된 경로일 때
         * When 해당 즐겨찾기를 또 추가하면
         * Then 이미 추가된 경로이므로 추가되지 않는다.
         */
        @Test
        @DisplayName("이미 추가된 즐겨찾기는 중복해서 추가할 수 없다.")
        void failForDuplicated() {
            // Given
            ExtractableResponse<Response> response = 로그인을_시도한다(회원_이메일, 회원_비밀번호);
            String 인증_토큰 = response.jsonPath().getString("accessToken");
            즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기, 인증_토큰);

            // When
            assertThat(즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기, 인증_토큰).statusCode()).isEqualTo(409);

            // Then
            assertThat(즐겨찾기_목록을_조회한다(인증_토큰).jsonPath().getList(".")).hasSize(1);
        }

        /**
         * Given 존재하지 않는 경로일 때
         * When 해당 즐겨찾기를 추가하면
         * Then 즐겨찾기에 추가되지 않는다.
         */
        @Test
        @DisplayName("존재하지 않는 경로는 즐겨찾기에 추가할 수 없다.")
        void failForNotExist() {
            // Given
            ExtractableResponse<Response> response = 로그인을_시도한다(회원_이메일, 회원_비밀번호);
            String 인증_토큰 = response.jsonPath().getString("accessToken");

            Map<String, Long> 추가할_즐겨찾기 = Map.of("source", 1L, "target", 404L);

            // When
            assertThat(즐겨찾기를_추가한다(추가할_즐겨찾기, 인증_토큰).statusCode()).isEqualTo(404);

            // Then
            assertThat(즐겨찾기_목록을_조회한다(인증_토큰).jsonPath().getList(".")).hasSize(0);
        }
    }

    @Nested
    class FindFavorites {
        /**
         * Given 사용자가 로그인 되어있고, 즐겨찾기를 추가한 뒤
         * When 즐겨찾기 목록을 조회하면
         * Then 즐겨찾기 목록이 조회된다.
         */
        @Test
        @DisplayName("로그인한 사용자는 즐겨찾기 목록을 조회할 수 있다.")
        void succeed() {
            // given
            ExtractableResponse<Response> response = 로그인을_시도한다(회원_이메일, 회원_비밀번호);
            String 인증_토큰 = response.jsonPath().getString("accessToken");

            즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기, 인증_토큰);

            // when
            ExtractableResponse<Response> 즐겨찾기_목록_조회 = 즐겨찾기_목록을_조회한다(인증_토큰);
            
            // then
            assertThat(즐겨찾기_목록_조회.statusCode()).isEqualTo(200);
            assertThat(즐겨찾기_목록_조회.jsonPath().getString("[0].source.name")).isEqualTo("강남역");
            assertThat(즐겨찾기_목록_조회.jsonPath().getString("[0].target.name")).isEqualTo("신논현역");
        }

        /**
         * Given 사용자가 로그인 되어있지 않을 때
         * When 즐겨찾기 목록을 조회하면
         * Then 즐겨찾기 목록을 조회할 수 없다.
         */
        @Test
        @DisplayName("로그인하지 않은 사용자는 즐겨찾기 목록을 조회할 수 없다.")
        void failForNotLogin() {
            // when
            // then
            assertThat(즐겨찾기_목록을_조회한다().statusCode()).isEqualTo(401);
        }
    }

    @Nested
    class RemoveFavorite {
      /**
       * Given 사용자가 로그인 되어있고, 즐겨찾기를 추가한 뒤
       * When 즐겨찾기를 삭제하면
       * Then 즐겨찾기 목록에서 삭제된다.
       */
        @Test
        @DisplayName("로그인한 사용자는 즐겨찾기를 삭제할 수 있다.")
        void succeed() {
            // Given
            ExtractableResponse<Response> response = 로그인을_시도한다(회원_이메일, 회원_비밀번호);
            String 인증_토큰 = response.jsonPath().getString("accessToken");

            즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기, 인증_토큰);

            // When
            // Then
            assertThat(즐겨찾기를_삭제한다(1L, 인증_토큰).statusCode()).isEqualTo(204);
            assertThat(즐겨찾기_목록을_조회한다(인증_토큰).jsonPath().getList(".")).isEmpty();
        }

        /**
         * Given 사용자가 로그인 되어있지 않을 때
         * When 즐겨찾기를 삭제하면
         * Then 즐겨찾기를 삭제할 수 없다.
         */
        @Test
        @DisplayName("로그인하지 않은 사용자는 즐겨찾기를 삭제할 수 없다.")
        void failForNotLogin() {
            // Given
            ExtractableResponse<Response> response = 로그인을_시도한다(회원_이메일, 회원_비밀번호);
            String 인증_토큰 = response.jsonPath().getString("accessToken");

            즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기, 인증_토큰);

            // When
            // Then
            assertThat(즐겨찾기를_삭제한다(1L).statusCode()).isEqualTo(401);

            ExtractableResponse<Response> 즐겨찾기_목록을_조회_확인 = 즐겨찾기_목록을_조회한다(인증_토큰);
            assertThat(즐겨찾기_목록을_조회_확인.jsonPath().getList("source.name")).contains("강남역");
            assertThat(즐겨찾기_목록을_조회_확인.jsonPath().getList("target.name")).contains("신논현역");
        }

        /**
         * Given 존재하지 않는 즐겨찾기일 때
         * When 즐겨찾기를 삭제하면
         * Then 즐겨찾기를 삭제할 수 없다.
         */
        @Test
        @DisplayName("존재하지 않는 즐겨찾기는 삭제할 수 없다.")
        void failForNotExist() {
            // Given
            ExtractableResponse<Response> response = 로그인을_시도한다(회원_이메일, 회원_비밀번호);
            String 인증_토큰 = response.jsonPath().getString("accessToken");

            즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기, 인증_토큰);

            // When
            // Then
            assertThat(즐겨찾기를_삭제한다(404L, 인증_토큰).statusCode()).isEqualTo(404);
        }

        /**
         * Given 사용자가 로그인 되어 있을 때
         * When 다른 사용자의 즐겨찾기를 삭제하면
         * Then 즐겨찾기를 삭제할 수 없다.
         */
        @Test
        @DisplayName("다른 사용자의 즐겨찾기는 삭제할 수 없다.")
        void failForDifferentUser() {
            // Given
            ExtractableResponse<Response> response = 로그인을_시도한다(회원_이메일, 회원_비밀번호);
            String 인증_토큰 = response.jsonPath().getString("accessToken");

            즐겨찾기를_추가한다(강남역에서_신논현역_즐겨찾기, 인증_토큰);

            회원_생성_요청("sabotage@test.com", "test1234!!", 18);
            ExtractableResponse<Response> 다른_사용자_로그인_요청 = 로그인을_시도한다(
                    "sabotage@test.com", "test1234!!"
            );
            String 다른_사용자_인증_토큰 = 다른_사용자_로그인_요청.jsonPath().getString("accessToken");

            // When

            assertThat(즐겨찾기를_삭제한다(1L, 다른_사용자_인증_토큰).statusCode()).isEqualTo(403);

            // Then
            ExtractableResponse<Response> 즐겨찾기_목록을_조회_확인 = 즐겨찾기_목록을_조회한다(인증_토큰);
            assertThat(즐겨찾기_목록을_조회_확인.jsonPath().getList("source.name")).contains("강남역");
            assertThat(즐겨찾기_목록을_조회_확인.jsonPath().getList("target.name")).contains("신논현역");
        }
    }


}
