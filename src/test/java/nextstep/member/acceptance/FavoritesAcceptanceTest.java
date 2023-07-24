package nextstep.member.acceptance;

import nextstep.subway.acceptance.LineSteps;
import nextstep.subway.acceptance.StationSteps;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.member.acceptance.FavoritesSteps.즐겨찾기_생성;
import static nextstep.member.acceptance.FavoritesSteps.즐겨찾기_조회;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoritesAcceptanceTest extends AcceptanceTest {

    private static final String 마들역명 = "마들역";
    private static final String 중계역명 = "중계역";

    private Long 마들역_id;
    private Long 노원역_id;
    private Long 중계역_id;
    private Long 칠호선_id;

    private Long 동백역_id;
    private Long 해운대역_id;
    private Long 부산_이호선_id;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    private String accessToken;

    @BeforeEach
    void setUpFixture() {
        마들역_id = StationSteps.지하철역_생성_요청(마들역명);
        노원역_id = StationSteps.지하철역_생성_요청("노원역");
        중계역_id = StationSteps.지하철역_생성_요청(중계역명);
        칠호선_id = LineSteps.지하철_노선_생성_요청("7호선", 중계역_id, 마들역_id, 10);
        LineSteps.지하철_노선_구간_등록_요청(칠호선_id, new SectionRequest(중계역_id, 노원역_id, 3));

        동백역_id = StationSteps.지하철역_생성_요청("동백역");
        해운대역_id = StationSteps.지하철역_생성_요청("해운대역");
        부산_이호선_id = LineSteps.지하철_노선_생성_요청("신분당선", 동백역_id, 해운대역_id, 10);

        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, AGE);
        accessToken = TokenSteps.로그인(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 한다.
     * When: 즐겨찾기를 등록한다.
     * Then: 즐겨찾기를 조회 하면 즐겨찾기로 등록한 역이 조회된다.
     */
    @Test
    void createFavorites() {
        //when
        즐겨찾기_생성(중계역_id, 마들역_id, accessToken);

        //then
        var 즐겨찾기_조회 = 즐겨찾기_조회(accessToken);
        assertThat(즐겨찾기_조회.jsonPath().getString("[0].source.name")).isEqualTo(중계역명);
        assertThat(즐겨찾기_조회.jsonPath().getString("[0].target.name")).isEqualTo(마들역명);
    }

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 한다.
     * When: 이어져 있지 않은 역을 즐겨찾기로 등록한다.
     * Then: 예외가 발생한다.
     */

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 하지 않고 즐겨찾기를 등록한다.
     * Then: 예외가 발생한다.
     */

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 한다.
     * When: 즐겨찾기를 등록한다.
     * When: 즐겨찾기를 삭제한다.
     * Then: 즐겨찾기를 조회 하면 삭제한 즐겨찾기는 존재하지 않는다.
     */

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 한다.
     * When: 즐겨찾기를 등록한다.
     * When: 토큰 없이 즐겨찾기를 삭제한다.
     * Then: 예외가 발생한다.
     */
}
