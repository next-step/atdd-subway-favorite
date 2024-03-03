package nextstep.favorite.acceptance.step;

import static nextstep.favorite.acceptance.step.FavoriteSteps.즐겨찾기_등록_본문;
import static nextstep.favorite.acceptance.step.FavoriteSteps.즐겨찾기_등록_요청;
import static nextstep.favorite.acceptance.step.FavoriteSteps.즐겨찾기_목록_조회_요청;
import static nextstep.favorite.acceptance.step.FavoriteSteps.즐겨찾기_목록_조회_응답에서_아이디_목록_추출;
import static nextstep.subway.acceptance.step.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.step.LineSteps.지하철_노선_응답에서_노선_아이디_추출;
import static nextstep.subway.acceptance.step.SectionSteps.지하철_구간_등록_요청;
import static nextstep.subway.acceptance.step.StationSteps.지하철_역_생성_요청;
import static nextstep.subway.acceptance.step.StationSteps.지하철역_응답에서_역_아이디_추출;
import static nextstep.subway.fixture.LineFixture.노선_생성_요청_본문;
import static nextstep.subway.fixture.SectionFixture.구간_등록_요청_본문;
import static nextstep.subway.fixture.StationFixture.강남역_생성_요청_본문;
import static nextstep.subway.fixture.StationFixture.교대역_생성_요청_본문;
import static nextstep.subway.fixture.StationFixture.지하철역_생성_요청_본문;
import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.acceptance.steps.MemberSteps;
import nextstep.member.acceptance.steps.TokenSteps;
import nextstep.subway.fixture.MemberFixture;
import nextstep.subway.fixture.TokenFixture;
import nextstep.utils.context.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
@AcceptanceTest
class FavoriteAcceptanceTest {

    private Long 교대역_아이디;
    private Long 강남역_아이디;
    private Long 양재역_아이디;
    private Long 남부터미널역_아이디;
    private Long 이호선_아이디;
    private Long 신분당선_아이디;
    private Long 삼호선_아이디;

    /**
     * given 지하철 노선과 구간, 사용자와 사용자 인증 토큰이 존재하고
     * when 출발역과 도착역을 기준으 경로를 즐겨찾기에 추가하면
     * then 즐겨찾기에 등록된다.
     */
    @DisplayName("즐겨찾기 생성하기")
    @Test
    void createFavorite() {
        // given
        이호선_삼호선_신분당선_노선의_구간_존재();
        String token = 사용자A_인증_토큰();

        // when
        ExtractableResponse<Response> 즐겨찾기_등록_응답 = 즐겨찾기_등록_요청(token, 즐겨찾기_등록_본문(교대역_아이디, 양재역_아이디));

        // then
        assertThat(즐겨찾기_등록_응답.statusCode()).isEqualTo(201);
    }


    /**
     * given 사용자의 즐겨찾기가 존재하고
     * when 즐겨찾기 목록을 조회하면
     * then 즐겨찾기가 조회된다.
     */
    @DisplayName("즐겨찾기 조회하기")
    @Test
    void getFavorite() {
        // given
        이호선_삼호선_신분당선_노선의_구간_존재();
        String token = 사용자A_인증_토큰();
        즐겨찾기_등록_요청(token, 즐겨찾기_등록_본문(교대역_아이디, 양재역_아이디));

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(token);

        // then
        assertThat(즐겨찾기_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(즐겨찾기_목록_조회_응답에서_아이디_목록_추출(즐겨찾기_목록_조회_응답)).hasSize(1);
    }


    @DisplayName("")
    @Test
    void deleteFavorite() {

    }

    /**
     * 교대역   --- 2호선, 10 ----    강남역
     * |                            |
     * 3호선, 2                   신분당선, 10
     * |                            |
     * 남부터미널역  --- 3호선, 3 ---   양재
     */
    private void 이호선_삼호선_신분당선_노선의_구간_존재() {
        교대역_아이디 = 지하철역_응답에서_역_아이디_추출(지하철_역_생성_요청(교대역_생성_요청_본문()));
        강남역_아이디 = 지하철역_응답에서_역_아이디_추출(지하철_역_생성_요청(강남역_생성_요청_본문()));
        양재역_아이디 = 지하철역_응답에서_역_아이디_추출(지하철_역_생성_요청(지하철역_생성_요청_본문("양재역")));
        남부터미널역_아이디 = 지하철역_응답에서_역_아이디_추출(지하철_역_생성_요청(지하철역_생성_요청_본문("남부터미널역")));

        이호선_아이디 = 지하철_노선_응답에서_노선_아이디_추출(지하철_노선_생성_요청(노선_생성_요청_본문("2호선", "green", 교대역_아이디, 강남역_아이디, 10L)));
        신분당선_아이디 = 지하철_노선_응답에서_노선_아이디_추출(지하철_노선_생성_요청(노선_생성_요청_본문("신분당선", "red", 강남역_아이디, 양재역_아이디, 10L)));
        삼호선_아이디 = 지하철_노선_응답에서_노선_아이디_추출(지하철_노선_생성_요청(노선_생성_요청_본문("삼호선", "orange", 교대역_아이디, 남부터미널역_아이디, 2L)));

        지하철_구간_등록_요청(삼호선_아이디, 구간_등록_요청_본문(남부터미널역_아이디, 양재역_아이디, 3L));
    }


    private String 사용자A_인증_토큰() {
        String email = "member_a@email.com";
        String password = "1234";
        int age = 10;
        MemberSteps.회원_생성_요청(MemberFixture.회원_생성_요청_본문(email, password, age));
        return TokenSteps.토큰_생성_응답에서_토큰값_추출(TokenSteps.토근_생성_요청(TokenFixture.토근_생성_요청_본문(email, password)));
    }

}
