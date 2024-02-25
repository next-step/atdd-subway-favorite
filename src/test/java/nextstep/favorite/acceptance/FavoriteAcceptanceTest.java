package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.controller.dto.LineCreateRequest;
import nextstep.subway.line.controller.dto.SectionAddRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.acceptance.FavoriteApiRequester.*;
import static nextstep.helper.JsonPathUtils.getListPath;
import static nextstep.helper.JsonPathUtils.getLongPath;
import static nextstep.member.acceptance.MemberSteps.회원_로그인_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.line.acceptance.LineApiRequester.createLine;
import static nextstep.subway.line.acceptance.SectionApiRequester.addSectionToLineSuccess;
import static nextstep.subway.station.acceptance.StationApiRequester.createStation;
import static nextstep.subway.station.acceptance.StationApiRequester.deleteStation;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
@AcceptanceTest
public class FavoriteAcceptanceTest {

    long 출발역;
    long 도착역;
    String email = "gildong@email.com";
    String password = "rlfehdWKd101!";
    String accessToken;

    // 2호선
    long 이호선;
    long 강남역;
    long 역삼역;
    long 선릉역;

    // 신분당선
    long 신분당선;
    long 신논현역;  // 강남-신논현 연결
    long 논현역;

    // 1호선
    long 일호선;
    long 수원역;
    long 화서역;

    long 없는역;

    @BeforeEach
    void setup() {
        // 역 생성
        강남역 = getLongPath(createStation("강남역"), "id");
        역삼역 = getLongPath(createStation("역삼역"), "id");
        선릉역 = getLongPath(createStation("선릉역"), "id");

        신논현역 = getLongPath(createStation("신논현역"), "id");
        논현역 = getLongPath(createStation("논현역"), "id");

        수원역 = getLongPath(createStation("수원역"), "id");
        화서역 = getLongPath(createStation("화서역"), "id");

        없는역 = getLongPath(createStation("없는역"), "id");
        deleteStation(없는역);

        // 노선생성
        이호선 = getLongPath(
            createLine(new LineCreateRequest("2호선", "green", 강남역, 역삼역, 3)),
            "id"
        );

        신분당선 = getLongPath(
            createLine(new LineCreateRequest("신분당선", "red", 신논현역, 논현역, 5)),
            "id"
        );

        일호선 = getLongPath(
            createLine(new LineCreateRequest("1호선", "blue", 수원역, 화서역, 5)),
            "id"
        );

        // 구간추가
        addSectionToLineSuccess(이호선, new SectionAddRequest(역삼역, 선릉역, 7));
        addSectionToLineSuccess(신분당선, new SectionAddRequest(강남역, 신논현역, 2));

        // 로그인 & 토큰생성
        accessToken = 토큰_생성(email, password);
    }

    private String 토큰_생성(String email, String password) {
        회원_생성_요청(email, password, 20);
        return "Bearer " + 회원_로그인_요청(email,password).jsonPath().getString("accessToken");
    }

    /**
     * given 출발역과 도착역이 주어지면
     * when 해당 경로를 즐겨찾는 경로로 추가할 수 있다
     * then 즐겨찾기 조회에서 추가된 즐겨찾기를 확인할 수 있다
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavoritePaths() {
        ExtractableResponse<Response> response = createFavorite(accessToken, 강남역, 논현역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
    }

    /**
     * when 즐겨찾기 조회하면
     * then 즐겨찾기로 등록한 경로들을 모두 조회할 수 있다
     */
    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavoritePaths() {
        createFavoriteWithSuccess(accessToken, 강남역, 논현역);

        ExtractableResponse<Response> response = getFavorites(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getListPath(response.body(), "source.id", Long.class)).contains(강남역);
        assertThat(getListPath(response.body(), "target.id", Long.class)).contains(논현역);
    }

    /**
     * given 삭제하려는 즐겨찾기 식별자로
     * when 즐겨찾기 삭제하면
     * then 해당 즐겨찾기는 삭제되고, 더이상 조회할 수 없다
     */
    @DisplayName("즐겨찾기 삭제")
    @Nested
    class 즐겨찾기_삭제 {

        @DisplayName("주어진 즐겨찾기 식별자 정보가 유효하면 즐겨찾기 삭제에 성공한다")
        @Test
        void deleteFavoritePaths() {
            String location = createFavoriteWithSuccess(accessToken, 출발역, 도착역).header(HttpHeaders.LOCATION);

            ExtractableResponse<Response> response = deleteFavorite(accessToken, location);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @DisplayName("주어진 즐겨찾기 식별자로 즐겨찾기 정보를 찾을 수 없으면 삭제에 실패한다")
        @Test
        void deleteFailWithNotExistFavoriteId() {
            String location = createFavoriteWithSuccess(accessToken, 출발역, 도착역).header(HttpHeaders.LOCATION);

            deleteFavorite(accessToken, location);

            ExtractableResponse<Response> response = deleteFavorite(accessToken, location);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }
    }
}
