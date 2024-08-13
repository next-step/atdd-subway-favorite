package nextstep.favorite.acceptance;

import static nextstep.line.acceptance.LineSteps.분당선_생성;
import static nextstep.line.acceptance.LineSteps.신분당선_생성;
import static nextstep.line.acceptance.LineSteps.지하철_노선_생성;
import static nextstep.line.acceptance.LineSteps.지하철_역_생성;
import static nextstep.member.acceptance.AuthSteps.토큰_획득;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 관련 기능")

public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    private String adminAccessToken;
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String ADMIN_PASSWORD = "password";
    private static final Integer ADMIN_AGE = 20;

    private String userAccessToken;
    private static final String USER_EMAIL = "user@email.com";
    private static final String USER_PASSWORD = "password";
    private static final Integer USER_AGE = 30;

    protected Long 신사역;
    protected Long 강남역;
    protected Long 청량리;
    protected Long 서울숲 ;
    protected Long 논현역;
    protected Long 양재역;

    protected Long 신분당선;
    protected Long 분당선;

    @BeforeEach
    void init() {
        신사역 = 지하철_역_생성("신사역")
                .jsonPath()
                .getObject("id", Long.class);

        강남역 = 지하철_역_생성("강남역")
                .jsonPath()
                .getObject("id", Long.class);

        청량리 = 지하철_역_생성("청량리")
                .jsonPath()
                .getObject("id", Long.class);
        서울숲 = 지하철_역_생성("서울숲")
                .jsonPath()
                .getObject("id", Long.class);

        논현역 = 지하철_역_생성("논현역")
                .jsonPath()
                .getObject("id", Long.class);

        양재역 = 지하철_역_생성("양재역")
                .jsonPath()
                .getObject("id", Long.class);

        신분당선 = 지하철_노선_생성(신분당선_생성(신사역, 논현역)).jsonPath().getObject("id", Long.class);
        분당선 = 지하철_노선_생성(분당선_생성(청량리, 서울숲)).jsonPath().getObject("id", Long.class);

        adminAccessToken = "Bearer " + 사용자_설정_및_로그인(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE);
        userAccessToken = "Bearer " + 사용자_설정_및_로그인(USER_EMAIL, USER_PASSWORD, USER_AGE);
    }

    /**
     * When 즐겨찾기를 생성하고
     * Then Http status code 는 201이다
     */
    @Test
    void 즐겨찾기_생성() {
        // given & when
        ExtractableResponse<Response> createResponse = FavoriteSteps.즐겨찾기_생성(adminAccessToken, 신사역, 논현역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 2개의 즐겨찾기를 생성하고
     * When 즐겨찾기 목록을 조회하면
     * Then 2개의 즐겨찾기를 조회할 수 있다
     */
    @Test
    void 즐겨찾기_조회() {
        // given
        FavoriteSteps.즐겨찾기_생성(adminAccessToken, 신사역, 논현역);
        FavoriteSteps.즐겨찾기_생성(adminAccessToken, 청량리, 서울숲);

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_조회(adminAccessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id")).hasSize(2);
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 생성한 즐겨찾기를 삭제하면
     * Then 해당 즐겨찾기는 삭제된다.
     */
    @Test
    void 즐겨찾기_삭제() {
        // given
        ExtractableResponse<Response> createResponse = FavoriteSteps.즐겨찾기_생성(adminAccessToken, 신사역, 논현역);
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_조회(adminAccessToken);
        Long favoriteId = response.jsonPath().getList("id", Long.class).get(0);

        // when
        ExtractableResponse<Response> deleteResponse = FavoriteSteps.즐겨찾기_삭제(adminAccessToken, favoriteId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * When 비정산 경로를 즐겨찾기를 생성하면
     * Then 예외가 발생한다.
     */
    @Test
    void 비정상_경로를_즐겨찾기_생성하면_예외발생() {
        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성(adminAccessToken, 신사역, 서울숲);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 비정상 유저가 즐겨찾기를 생성하면
     * Then 예외가 발생한다.
     */
    @Test
    void 비정상_유저가_즐겨찾기_생성하면_예외발생() {
        // given & when
        ExtractableResponse<Response> createResponse = FavoriteSteps.즐겨찾기_생성("", 신사역, 논현역);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 즐겨찾기를 생성하고
     * When 다른 사람이 해당 즐겨찾기를 삭제를 요청하면
     * Then 예외가 발생한다.
     */
    @Test
    void 다른사람이_본인_즐겨찾기가_아닌것을_삭제요청하면_예외가_발생() {
        // given
        FavoriteSteps.즐겨찾기_생성(adminAccessToken, 신사역, 논현역);
        ExtractableResponse<Response> readResponse = FavoriteSteps.즐겨찾기_조회(adminAccessToken);
        Long favoriteId = readResponse.jsonPath().getList("id", Long.class).get(0);

        // when
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_삭제(userAccessToken, favoriteId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


    private String 사용자_설정_및_로그인(String email, String password, Integer age) {
        memberRepository.save(new Member(email, password, age));
        var 로그인_토큰_응답 = 토큰_획득(email, password);
        return 로그인_토큰_응답.jsonPath().getString("accessToken");
    }
}