package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.acceptance.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.member.acceptance.AuthSteps.로그인_토큰_요청;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;
    private final Long 시작역_ID = 1L;
    private final Long 종착역_ID = 3L;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Given 구간과 역이 등록되어 있고,
     * And 회원 가입한 사용자가 있고,
     * And 회원 가입한 사용자가 로그인을 한 상태일 떄,
     * When 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 자신의 즐겨찾기가 등록된다.
     */
    @Test
    @DisplayName("사용자는 시작 역과 종착 역을 가지고 즐겨찾기를 등록할 수 있다.")
    void createFavorite() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        ExtractableResponse<Response> 로그인_토큰_응답 = 로그인_토큰_요청(EMAIL, PASSWORD);
        String accessToken = 로그인_토큰_응답.jsonPath().getString("accessToken");

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 시작역_ID, 종착역_ID);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 구간과 역이 등록되어 있고,
     * And 회원 가입한 사용자가 있고,
     * And 회원 가입한 사용자가 로그인을 하지 않은 상태일 떄,
     * When 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 등록에 실패하고 인증 오류가 발생한다.
     */
    @Test
    @DisplayName("로그인 하지 않은 사용자가 즐겨찾기 등록을 시도하면 인증 오류가 발생한다.")
    void invalidUserCreateFavorite() {
        // given
        String accessToken = "invalid token";

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 시작역_ID, 종착역_ID);

        // then
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 구간과 역이 등록되어 있고,
     * And 회원 가입한 사용자가 있고,
     * And 회원 가입한 사용자가 로그인을 한 상태일 떄,
     * When 시작 역과 종착역을 즐겨찾기로 등록하면
     * Then 자신의 즐겨찾기가 등록된다.
     */
}
