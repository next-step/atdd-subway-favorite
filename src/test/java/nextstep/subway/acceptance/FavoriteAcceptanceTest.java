package nextstep.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private Long 강남역;
    private Long 양재역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_생성_요청("강남역").jsonPath().getLong("id");
        양재역 = 지하철역_생성_요청("양재역").jsonPath().getLong("id");
    }

    /**
     * When 로그인한 상태로 즐겨찾기를 추가하면
     * Then 성공한다.
     */
    @DisplayName("로그인 상태로 즐겨찾기를 추가한다")
    @Test
    void saveFavorite() {
        // when
        var response = 로그인_상태에서_즐겨찾기_추가_요청(관리자_토큰, 강남역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    /**
     * When 로그인을 안한 상태로 즐겨찾기를 추가하면
     * Then 오류가 발생한다.
     */
    @DisplayName("로그아웃 상태로 즐겨찾기를 추가한다")
    @Test
    void saveFavorite_WithoutLogin() {
        // when
        var response = 로그아웃_상태에서_즐겨찾기_추가(강남역, 양재역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기가 추가되고
     * When 즐겨찾기를 조회하면
     * Then 즐겨찾기가 조회된다.
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void getFavorite() {
        // given
        로그인_상태에서_즐겨찾기_추가_요청(관리자_토큰, 강남역, 양재역);

        // when
        var response = 즐겨찾기_목록_조회_요청(관리자_토큰);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getList("source.id", Long.class).get(0)).isEqualTo(강남역),
                () -> assertThat(response.jsonPath().getList("target.id", Long.class).get(0)).isEqualTo(양재역)
        );
    }

    /**
     * Given 즐겨찾기가 추가된다
     * When 로그인을 한 채로 즐겨찾기를 삭제를 요청하면
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("로그인 상태로 즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {
        // given
        Long 즐겨찾기_Id = 로그인_상태에서_즐겨찾기_추가_요청(관리자_토큰, 강남역, 양재역).jsonPath().getLong("id");

        // when
        var response = 로그인_상태에서_즐겨찾기_삭제_요청(관리자_토큰, 즐겨찾기_Id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 즐겨찾기가 추가된다
     * When 로그아웃을 한 채로 즐겨찾기를 삭제를 요청하면
     * Then 오류가 발생한다.
     */
    @DisplayName("로그아웃 상태로 즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite_WithoutLogin() {
        // given
        Long 즐겨찾기_Id = 로그인_상태에서_즐겨찾기_추가_요청(관리자_토큰, 강남역, 양재역).jsonPath().getLong("id");

        // when
        var response = 로그아웃_상태에서_즐겨찾기_삭제_요청(즐겨찾기_Id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
