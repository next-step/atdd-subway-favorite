package nextstep.favorite.acceptance;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.subway.line.LineRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성;
import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_조회;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.member.acceptance.MemberSteps.회원_토큰_로그인;
import static nextstep.subway.SubwaySteps.지하철_노선_생성;
import static nextstep.subway.SubwaySteps.지하철_역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private String 회원;
    private Long 교대역;
    private Long 강남역;
    private Long 이호선;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원 = 회원_토큰_로그인(EMAIL, PASSWORD).jsonPath().getString("accessToken");
        교대역 = 지하철_역_생성("교대역").jsonPath().getLong("id");
        강남역 = 지하철_역_생성("강남역").jsonPath().getLong("id");
        이호선 = 지하철_노선_생성(new LineRequest("이호선", "green", 교대역, 강남역, 10L)).jsonPath().getLong("id");
    }

    /**
     * when 회원이 출발역과 도착역 정보로 즐겨찾기를 생성하면
     * then 201 Created 코드로 응답받는다.
     */
    @DisplayName("즐겨찾기 생성")
    @Test
    void save_favorite() {
        // when
        var response = 즐겨찾기_생성(회원, new FavoriteRequest(교대역, 강남역));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * given 회원이 출발역과 도착역 정보로 즐겨찾기를 생성하고
     * when 회원의 즐겨찾기 목록을 조회하면
     * then 해당 즐겨찾기가 포함되어 있다.
     */
    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void find_favorites() {
        // given
        즐겨찾기_생성(회원, new FavoriteRequest(교대역, 강남역));

        // when
        var response = 즐겨찾기_조회(회원);

        // then
        assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(교대역);
        assertThat(response.jsonPath().getList("target.id", Long.class)).containsExactly(강남역);
    }
}
