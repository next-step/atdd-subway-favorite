package nextstep.favorite.acceptance;

import nextstep.favorite.domain.Favorite;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.acceptance.LineCommonApi;
import nextstep.subway.acceptance.StationCommonApi;
import nextstep.subway.domain.line.dto.LineCreateRequest;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.dto.SectionCreateRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 역삼역;
    private Long 남부터미널역;
    private Long 석남역;

    private Long 이호선;
    private Long 신분당선;
    private Long 삼호선;

    private Long 회원_1;
    private String 회원_1_토큰;
    private Long 즐겨찾기_1;
    private Long 즐겨찾기_2;
    private Long 즐겨찾기_3;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = StationCommonApi.createStation("교대역").jsonPath().getLong("id");
        강남역 = StationCommonApi.createStation("강남역").jsonPath().getLong("id");
        역삼역 = StationCommonApi.createStation("역삼역").jsonPath().getLong("id");
        양재역 = StationCommonApi.createStation("양재역").jsonPath().getLong("id");
        남부터미널역 = StationCommonApi.createStation("남부터미널역").jsonPath().getLong("id");
        석남역 = StationCommonApi.createStation("석남역").jsonPath().getLong("id");

        이호선 = LineCommonApi.createLine(new LineCreateRequest("2호선", "green", 교대역, 강남역, 10)).jsonPath().getLong("id");
        신분당선 = LineCommonApi.createLine(new LineCreateRequest("신분당선", "red", 강남역, 양재역, 4)).jsonPath().getLong("id");
        삼호선 = LineCommonApi.createLine(new LineCreateRequest("3호선", "orange", 교대역, 남부터미널역, 6)).jsonPath().getLong("id");

        LineCommonApi.addSection(이호선, new SectionCreateRequest(강남역, 역삼역, 2));
        LineCommonApi.addSection(삼호선, new SectionCreateRequest(남부터미널역, 양재역, 3));

        var 회원_생성_결과 = MemberSteps.회원_생성_요청("testemail@test.com", "abc", 20);
        회원_1 = 회원_생성_결과.body().as(MemberResponse.class).getId();

        var 회원_1_로그인_결과 = MemberSteps.회원_로그인_요청(Map.of(
                "email", "testemail@test.com",
                "password", "abc"
        ));

        회원_1_토큰 = "Bearer " + 회원_1_로그인_결과.jsonPath().getString("accessToken");

        즐겨찾기_1 = FavoriteSteps.즐겨찾기_생성_요청(Map.of(
                "source", 교대역,
                "target", 강남역,
                "authorization", 회원_1_토큰
        )).jsonPath().getLong("id");

        즐겨찾기_2 = FavoriteSteps.즐겨찾기_생성_요청(Map.of(
                "source", 교대역,
                "target", 역삼역,
                "authorization", 회원_1_토큰
        )).jsonPath().getLong("id");

        즐겨찾기_3 = FavoriteSteps.즐겨찾기_생성_요청(Map.of(
                "source", 교대역,
                "target", 양재역,
                "authorization", 회원_1_토큰
        )).jsonPath().getLong("id");
    }

    /**
     * Given: 등록되어 있는 시작역과 등록역을 입력하고
     * When: 즐겨찾기를 생성하면
     * Then: 새로운 즐겨찾기가 등록된다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("source", 강남역);
        params.put("target", 남부터미널역);
        params.put("authorization", 회원_1_토큰);

        //when
        var response = FavoriteSteps.즐겨찾기_생성_요청(params);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given: 회원이 존재하고
     * When: 즐겨찾기를 조회하면
     * Then: 해당 회원이 등록한 즐겨찾기가 조회된다.
     */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("authorization", 회원_1_토큰);

        //when
        var response = FavoriteSteps.즐겨찾기_조회_요청(params);

        //then
        assertThat(response.jsonPath().getList("favorites", Favorite.class).size()).isEqualTo(3);
    }

    /**
     * Given: 회원이 등록한 즐겨찾기가 있고
     * When: 특정 즐겨찾기를 삭제하면
     * Then: 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("id", 즐겨찾기_1);
        params.put("authorization", 회원_1_토큰);

        //when
        var deleteResponse = FavoriteSteps.즐겨찾기_삭제_요청(params);
        var findResponse = FavoriteSteps.즐겨찾기_조회_요청(params);

        //then
        assertThat(findResponse.jsonPath().getList("favorites.id", Long.class)).doesNotContain(즐겨찾기_1);
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
