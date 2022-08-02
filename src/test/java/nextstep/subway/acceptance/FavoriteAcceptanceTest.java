package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private Long 강남역;
    private Long 양재역;
    
    private Long 정자역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
        정자역 = 지하철역_생성_요청("정자역").jsonPath().getLong("id");
    }

    /**
     * Given Token 인증을 통해 로그인한다.
     * When 경로(출발역, 도착역)을 즐겨찾기로 추가하면
     * Then 즐겨찾기 목록 조회 시 추가 된 경로를 확인할 수 있다.
     */
    @DisplayName("즐겨찾기 경로를 추가한다.")
    @Test
    void addFavoritePath() {
        // given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        즐겨찾기_추가(accessToken, 강남역, 양재역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회(accessToken);

        // then
        List<Long> ids = response.jsonPath().getList("id", Long.class);
        List<String> sourceStationNames = response.jsonPath().getList("source.name", String.class);
        List<String> targetStationNames = response.jsonPath().getList("target.name", String.class);

        assertAll(
                () -> assertThat(ids).hasSize(1),
                () -> assertThat(sourceStationNames).containsExactly("강남역"),
                () -> assertThat(targetStationNames).containsExactly("양재역")
        );
    }

    /**
     * Given 로그인하지 않고
     * When 경로(출발역, 도착역)을 즐겨찾기로 추가하면
     * Then 401 Unauthorized Exception 을 만난다.
     */
    @DisplayName("비로그인 회원이 즐겨찾기 경로를 추가하면 예외")
    @Test
    void anonymousUserAddFavoritePathException() {
        // given
        // when
        ExtractableResponse<Response> response = 비로그인_즐겨찾기_추가(강남역, 양재역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given Token 인증을 통해 로그인한다.
     * Given 2개의 경로(출발역, 도착역)을 즐겨찾기로 추가하고
     * When 나의 즐겨찾기 목록을 조회하면
     * Then 즐겨찾기 목록에 2개의 경로를 확인할 수 있다.
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        // given
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
        
        // given
        즐겨찾기_추가(accessToken, 강남역, 양재역);
        즐겨찾기_추가(accessToken, 양재역, 정자역);
        
        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회(accessToken);

        // then
        List<Long> ids = response.jsonPath().getList("id", Long.class);
        List<String> sourceStationNames = response.jsonPath().getList("source.name", String.class);
        List<String> targetStationNames = response.jsonPath().getList("target.name", String.class);

        assertAll(
                () -> assertThat(ids).hasSize(2),
                () -> assertThat(sourceStationNames).containsExactly("강남역", "양재역"),
                () -> assertThat(targetStationNames).containsExactly("양재역", "정자역")
        );
    }

     /**
     * Given Token 인증을 통해 로그인한다.
     * Given 경로(출발역, 도착역)을 즐겨찾기로 추가하고
     * When 즐겨찾기를 삭제하면
     * Then 해당 즐겨찾기 정보는 삭제된다.
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {

    }


}
