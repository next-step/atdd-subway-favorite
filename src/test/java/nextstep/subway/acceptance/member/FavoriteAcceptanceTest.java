package nextstep.subway.acceptance.member;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.fake.DataLoader;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.nio.file.Path;
import java.nio.file.Paths;

import static nextstep.fake.GithubResponses.사용자1;
import static nextstep.fake.GithubResponses.사용자2;
import static nextstep.subway.acceptance.member.FavoriteSteps.즐겨찾기_등록;
import static nextstep.subway.acceptance.member.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.member.FavoriteSteps.즐겨찾기_삭제;
import static nextstep.subway.acceptance.member.MemberSteps.깃허브_로그인;
import static nextstep.subway.acceptance.subway.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관리 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DataLoader dataLoader;

    private String 사용자1_토큰;
    private Long 강남역;
    private Long 양재역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        dataLoader.loadData();

        // Given 역을 등록하고
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");

        // Given 로그인을 하고
        사용자1_토큰 = 깃허브_로그인(사용자1.getCode());
    }

    /**
     * When 즐겨찾기를 등록하면
     * Then 등록된 즐겨찾기 목록을 확인할 수 있다.
     */
    @DisplayName("즐겨찾기 생성 및 조회")
    @Test
    void createFavorite() {
        var 첫_번째_즐겨찾기_응답 = 즐겨찾기_등록(사용자1_토큰, 강남역, 양재역);
        var 두_번째_즐겨찾기_응답 = 즐겨찾기_등록(사용자1_토큰, 강남역, 양재역);

        long 첫_번째_즐겨찾기 = 즐겨찾기_ID_가져오기(첫_번째_즐겨찾기_응답);
        long 두_번째_즐겨찾기 = 즐겨찾기_ID_가져오기(두_번째_즐겨찾기_응답);

        var favorites = 즐겨찾기_목록_조회_요청(사용자1_토큰);
        assertAll(() -> {
            assertThat(favorites.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(favorites.jsonPath().getList("id", Long.class)).containsExactly(첫_번째_즐겨찾기, 두_번째_즐겨찾기);
        });
    }

    /**
     * Given 즐겨찾기를 등록하고
     * When 해당 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제된다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        var createResponse = 즐겨찾기_등록(사용자1_토큰, 강남역, 양재역);
        long id = 즐겨찾기_ID_가져오기(createResponse);

        var deleteResponse = 즐겨찾기_삭제(사용자1_토큰, id);

        var favorites = 즐겨찾기_목록_조회_요청(사용자1_토큰);
        assertAll(() -> {
            assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(favorites.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(favorites.jsonPath().getList("id", Long.class)).isEmpty();
        });
    }

    /**
     * When 유효하지 않은 토큰으로 즐겨찾기를 등록하면
     * Then 즐겨찾기를 삭제할 수 없다.
     */
    @DisplayName("즐겨찾기 등록 실패")
    @Test
    void createFavoriteWithInvalidToken() {
        var createResponse = 즐겨찾기_등록(사용자1_토큰 + "abcd", 강남역, 양재역);

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 사용자2의 즐겨찾기가 등록되고
     * When 사용자1이 사용자2의 즐겨찾기를 제거하는 경우
     * Then 즐겨찾기를 삭제할 수 없다.
     */
    @DisplayName("다른 사용자의 즐겨찾기 삭제")
    @Test
    void deleteOtherFavorite() {
        String 사용자2_토큰 = 깃허브_로그인(사용자2.getCode());
        var createResponse = 즐겨찾기_등록(사용자2_토큰, 강남역, 양재역);

        var deleteResponse = 즐겨찾기_삭제(사용자1_토큰, 즐겨찾기_ID_가져오기(createResponse));

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    private long 즐겨찾기_ID_가져오기(ExtractableResponse<Response> response) {
        String location = response.header("location");
        Path path = Paths.get(location);
        return Long.parseLong(path.getFileName().toString());
    }
}
