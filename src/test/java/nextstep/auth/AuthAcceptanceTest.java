package nextstep.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static nextstep.auth.AuthSteps.*;

class AuthAcceptanceTest extends AcceptanceTest {
    private final String EMAIL = "admin@email.com";
    private final String PASSWORD = "password";
    private final Integer AGE = 20;
    private final String LOCAL_ADDRESS = "http://localhost:8080";


    @Value("${github.url.access-token}")
    private String tokenUrl;

    @Value("${github.url.profile}")
    private String profileUrl;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
        tokenUrl = tokenUrl.replace(LOCAL_ADDRESS, "");
        profileUrl = profileUrl.replace(LOCAL_ADDRESS, "");

    }

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

        // when
        ExtractableResponse<Response> response = 토큰_로그인_요청(EMAIL, PASSWORD);

        // then
        access_token_응답을_받음(response);
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        // when
        ExtractableResponse<Response> response = 깃허브_로그인_요청();

        // then
        access_token_응답을_받음(response);
    }

    /**
     * Given {code, cliend_id, client_secret} json body 값과 함께
     * When Accept: application/json 형식으로
     * And POST 요청을 https://github.com/login/oauth/access_token에 보내면
     * Then access_token, scope, token_type이 json으로 body에 담겨 응답이 온다.
     * Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
     */
    @DisplayName("Github Auth, post for access_token")
    @Test
    void githubAuthPostForAccessToken() {
        // when
        ExtractableResponse<Response> response = oauth2_깃허브에_토큰_요청(tokenUrl);

        // then
        oauth2_깃허브_토큰_응답_받음(response);
    }

    /**
     * Given POST 요청 https://github.com/login/oauth/access_token으로 깃허브로 부터 받은 accessToken이 있을 때
     * When GET 요청을 https://api.github.com/user에 아래 헤더와 함께 보내면,
     *   -H "Accept: application/vnd.github+json" \
     *   -H "Authorization: Bearer <YOUR-TOKEN>" \
     *   -H "X-GitHub-Api-Version: 2022-11-28"
     * Then email 값이 json으로 body에 담겨 응답이 온다.
     * Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
     * */
    @DisplayName("Github Auth, get user info")
    @Test
    void githubAuthGetUserInfo() {
        // when
        ExtractableResponse<Response> response = oauth2_깃허브_리소스_조회_요청(profileUrl);

        // then
        이메일_응답_받음(response, "email");
    }
}