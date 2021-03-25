package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.favorite.acceptance.FavoriteRequestSteps.지하철_즐겨찾기_추가_요청;
import static nextstep.subway.favorite.acceptance.FavoriteVerificationSteps.지하철_즐겨찾기_개수_확인;
import static nextstep.subway.favorite.acceptance.FavoriteVerificationSteps.지하철_즐겨찾기_추가_됨;
import static nextstep.subway.line.acceptance.LineRequestSteps.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionRequestSteps.노선_요청;
import static nextstep.subway.member.MemberRequestSteps.로그인_되어_있음;
import static nextstep.subway.member.MemberRequestSteps.회원_생성_요청;
import static nextstep.subway.station.acceptance.StationRequestSteps.지하철_역_등록_됨;

@DisplayName("지하철 즐겨찾기 관련 기능 인수 테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private StationResponse 강남역;
    private StationResponse 청계산입구역;

    private LineResponse 신분당선;

    @BeforeEach
    void init() {
        super.setUp();

        // given
        강남역 = 지하철_역_등록_됨("강남역").as(StationResponse.class);
        청계산입구역 = 지하철_역_등록_됨("청계산입구역").as(StationResponse.class);

        신분당선 = 지하철_노선_생성_요청(노선_요청("신분당선", "bg-red-600", 강남역.getId(), 청계산입구역.getId(), 10))
                .as(LineResponse.class);

        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    @Test
    @DisplayName("즐겨찾기 추가")
    void createFavorite() {
        // given
        TokenResponse 로그인_멤버_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 지하철_즐겨찾기_추가_요청(로그인_멤버_토큰, 강남역, 청계산입구역);

        // then
        지하철_즐겨찾기_추가_됨(response);
        지하철_즐겨찾기_개수_확인(response, 1);
    }
}
