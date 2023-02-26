package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.utils.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_삭제_요청;
import static nextstep.subway.acceptance.FavoriteSteps.즐겨찾기_추가_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static nextstep.utils.DataLoader.MEMBER_EMAIL;
import static nextstep.utils.DataLoader.MEMBER_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 즐겨찾기 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DataLoader dataLoader;

    private String 사용자;

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // 테스트를 위한 사용자 데이터 로드
        dataLoader.loadData();

        // 사용자 accessToken 발급
        사용자 = 베어러_인증_로그인_요청(MEMBER_EMAIL, MEMBER_PASSWORD).jsonPath().getString("accessToken");

        // 역 생성
        교대역 = 지하철역_생성_요청("교대역").jsonPath().getLong("id");
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
    }

    /**
     * Given 로그인한 사용자가
     * When 지하철 경로를 즐겨찾기 추가 요청을 하면
     * Then 즐겨찾기 추가에 성공한다
     */
    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        // When
        ExtractableResponse<Response> response = 즐겨찾기_추가_요청(사용자, 교대역, 양재역);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 로그인한 사용자가
     *  And 지하철 경로를 즐겨찾기 추가하고
     * When 지하철 경로 즐겨찾기 목록 조회 요청을 하면
     * Then 자신이 추가한 즐겨찾기한 지하철 경로 목록이 조회된다
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findFavorite() {
        // Given
        즐겨찾기_추가_요청(사용자, 교대역, 양재역);

        // When
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(사용자);

        // Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("")).hasSize(1),
                () -> assertThat(response.jsonPath().getLong("source.id")).isEqualTo(교대역),
                () -> assertThat(response.jsonPath().getLong("target.id")).isEqualTo(양재역)
        );
    }

    /**
     * Given 로그인한 사용자가
     *  And 지하철 경로를 즐겨찾기 추가하고
     * When 해당 지하철 경로를 즐겨찾기에서 삭제 요청을 하면
     * Then 즐겨찾기 삭제에 성공한다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void removeFavorite() {
        // Given
        ExtractableResponse<Response> responseOf즐겨찾기_추가_요청 = 즐겨찾기_추가_요청(사용자, 교대역, 양재역);

        // When
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(사용자, responseOf즐겨찾기_추가_요청);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
