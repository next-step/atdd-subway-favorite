package nextstep.member.acceptance;

import static nextstep.member.acceptance.MemberSteps.에러코드_검증;
import static nextstep.member.acceptance.MemberSteps.회원_경로_즐겨찾기_등록_요청;
import static nextstep.member.acceptance.MemberSteps.회원_경로_즐겨찾기_삭제_요청;
import static nextstep.member.acceptance.MemberSteps.회원_경로_즐겨찾기_조회_요청;
import static nextstep.member.acceptance.MemberSteps.회원_경로_즐겨찾기_조회_요청_응답_리스트_반환;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.study.AuthSteps.로그인_후_엑세스토큰_획득;
import static nextstep.subway.acceptance.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.LineSteps.지하철_노선에_지하철_구간_생성_요청;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_요청_응답값_반환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private Long 노원역;
    private Long 하계역;
    private Long 논현역;
    private Long 판교역;


    @BeforeEach
    public void setUp() {
        super.setUp();

        노원역 = 지하철역_생성_요청_응답값_반환("노원역");
        하계역 = 지하철역_생성_요청_응답값_반환("하계역");
        논현역 = 지하철역_생성_요청_응답값_반환("논현역");
        판교역 = 지하철역_생성_요청_응답값_반환("판교역");

        // 7호선
        var seven = 지하철_노선_생성_요청("7호선", 노원역, 하계역);
        지하철_노선에_지하철_구간_생성_요청(seven.getId(), 하계역, 논현역);
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 경로를 즐겨찾기 한다면
     * Then 경로 찾기가 등록된다
     */
    @DisplayName("즐겨 찾기 생성")
    @Test
    void createFavorite() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_후_엑세스토큰_획득(EMAIL, PASSWORD);

        // when
        var response = 회원_경로_즐겨찾기_등록_요청(노원역, 논현역, accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        List<FavoriteResponse> favoriteResponse = 회원_경로_즐겨찾기_조회_요청_응답_리스트_반환(accessToken);
        즐겨찾기_응답값_검증(favoriteResponse, 1, 노원역, 논현역);
    }

    /**
     * When 로그인 없이 경로를 즐겨찾기 한다면
     * Then 401 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 생성 - 권한 없음")
    @Test
    void createFavoriteThrowUnAuthorizeException() {
        // when
        var response = 회원_경로_즐겨찾기_등록_요청(노원역, 논현역, "");

        //then
        에러코드_검증(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * When 잘못된 경로를 즐겨찾기 한다면
     * Then 400 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 생성 - 잘못된 경로")
    @Test
    void createFavoriteThrowInvalidPath() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_후_엑세스토큰_획득(EMAIL, PASSWORD);

        // when
        var response = 회원_경로_즐겨찾기_등록_요청(노원역, 판교역, accessToken);

        //then
        에러코드_검증(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * And 경로를 즐겨찾기 하고
     * When 즐겨찾기를 조회하면
     * Then 등록한 즐겨찾기를 반환한다
     */
    @DisplayName("즐겨 찾기 조회")
    @Test
    void getFavorites() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_후_엑세스토큰_획득(EMAIL, PASSWORD);
        회원_경로_즐겨찾기_등록_요청(노원역, 논현역, accessToken);

        // when
        var responses = 회원_경로_즐겨찾기_조회_요청(accessToken);

        // then
        assertThat(responses.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favoriteResponse = 회원_경로_즐겨찾기_조회_요청_응답_리스트_반환(responses);
        즐겨찾기_응답값_검증(favoriteResponse, 1, 노원역, 논현역);
    }

    /**
     * When 로그인 없이 즐겨찾기를 조회하면
     * Then 401 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 조회 - 권한 없음")
    @Test
    void getFavoritesThrowUnAuthorizeException() {
        // when
        var response = 회원_경로_즐겨찾기_조회_요청("");

        //then
        에러코드_검증(response, HttpStatus.UNAUTHORIZED);
    }


    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * And 경로를 즐겨찾기 하고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기가 삭제된다.
     */
    @DisplayName("즐겨 찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_후_엑세스토큰_획득(EMAIL, PASSWORD);
        var createResponse = 회원_경로_즐겨찾기_등록_요청(노원역, 논현역, accessToken);

        //when
        var response = 회원_경로_즐겨찾기_삭제_요청(createResponse, accessToken);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<FavoriteResponse> favoriteResponse = 회원_경로_즐겨찾기_조회_요청_응답_리스트_반환(accessToken);
        assertThat(favoriteResponse).isEmpty();
    }


    /**
     * Given 즐겨 찾기를 생성하고
     * When 로그인 없이 즐겨찾기를 삭제하면
     * Then 401 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 삭제 - 권한 없음")
    @Test
    void deleteFavoriteThrowUnAuthorizeException() {
        //given
        var createResponse = 회원_경로_즐겨찾기_등록_요청(노원역, 논현역, "");

        //when
        var response = 회원_경로_즐겨찾기_삭제_요청(createResponse, "");

        //then
        에러코드_검증(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Given 회원 가입을 생성하고
     * And 로그인을 하고
     * And 경로를 즐겨찾기 하고
     * When 잘못된 즐겨찾기를 삭제하면
     * Then 403 에러를 반환한다.
     */
    @DisplayName("즐겨 찾기 삭제 - 잘못된 요청")
    @Test
    void deleteFavoriteThrowInvalidParameterException() {
        // given
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        String accessToken = 로그인_후_엑세스토큰_획득(EMAIL, PASSWORD);
        회원_경로_즐겨찾기_등록_요청(노원역, 논현역, "");

        //when
        var response = 회원_경로_즐겨찾기_삭제_요청(-1L, accessToken);

        //then
        에러코드_검증(response, HttpStatus.BAD_REQUEST);
    }

    private void 즐겨찾기_응답값_검증(List<FavoriteResponse> responses, int size, Long sourceId, Long targetId) {
        assertAll(
                () -> assertThat(responses).hasSize(size),
                () -> assertThat(responses.get(0).getSource().getId()).isEqualTo(sourceId),
                () -> assertThat(responses.get(0).getTarget().getId()).isEqualTo(targetId)
        );
    }
}
