package nextstep.favorite.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.acceptance.StationSteps;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기가 생성된다.
     */
    @Test
    void 즐겨_찾기_생성() {
        // given
        final StationResponse 강남역 = StationSteps.지하철역_생성_요청("강남역").jsonPath().getObject(".", StationResponse.class);
        final StationResponse 역삼역 = StationSteps.지하철역_생성_요청("역삼역").jsonPath().getObject(".", StationResponse.class);

        LineSteps.지하철_노선_생성_요청("2호선", "green", 강남역.getId(), 역삼역.getId(), 10);

        // when
        FavoriteSteps.지하철_좋아요_생성(강남역.getId(), 역삼역.getId(), "code");

        // then
        final List<FavoriteResponse> favoriteResponse = FavoriteSteps.지하철_좋아요_조회("code").jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favoriteResponse).isNotNull();
    }

    /**
     * given: 경로가 존재하지 않는 두 역이 주어 졌을 때
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기 생성에 실패한다.
     */
    @Test
    void 즐겨_찾기_생성_시_비_정상_경로이면_등록이_불가하다() {
        // when
        // then
    }

    /**
     * given: 유효하지 않은 bearer token에 대해서
     * when: 즐겨 찾기를 생성하면
     * then: 즐겨 찾기 생성에 실패한다.
     */
    @Test
    void  즐겨_찾기_요청_시_bearer_token이_유효하지_않으면_안된다() {

    }
}