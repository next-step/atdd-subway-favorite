package nextstep.auth.unit;


import nextstep.auth.AuthenticationException;
import nextstep.auth.ForbiddenException;
import nextstep.auth.token.oauth2.github.GithubClient;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.auth.util.VirtualUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @Autowired
    GithubClient githubClient;

    VirtualUser validUser = VirtualUser.사용자1;

    /**
     * Given 깃허브로 부터 코드를 받음.
     * When 받은 코드로 토큰 요청 API를 호출하면
     * Then accessToken이 Json body에 담겨 응답이 온다.
     * Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
     */
    @DisplayName("토큰 요청 API 호출 성공")
    @Test
    void getAccessTokenFromGithubSuccess() {
        // when
        String token = githubClient.getAccessTokenFromGithub(validUser.getCode());

        // then
        assertThat(token).isNotBlank();
    }

    /**
     * Given 깃허브로 부터 코드를 받음.
     * When 깃허브가 인식할 수 없는 코드로 토큰 요청 API를 호출하면
     * Then accessToken이 Json body에 담겨 오지 않아, Authentication 발생한다.
     * Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
     * */
    @DisplayName("토큰 요청 API 호출 실패, 깃허브가 인식할 수 없는 코드 전송")
    @Test
    void getAccessTokenFromGithubFailedByInvalidToken() {
        // when
        assertThatThrownBy(() -> githubClient.getAccessTokenFromGithub("wrong_token"))
                .isInstanceOf(AuthenticationException.class);
    }

    /**
     * Given 깃허브로 부터 토큰을 받음.
     * When 받은 토큰으로 리소스 조회 API를 호출하면
     * Then 리소스응답 템플릿에 맞춰 Json body를 받는다.
     * Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
     * */
    @DisplayName("리소스 조회 API 호출 성공")
    @Test
    void getGithubProfileFromGithubSuccess() {
        // when
        GithubProfileResponse profileResponse = githubClient.getGithubProfileFromGithub(validUser.getToken());

        // then
        assertThat(profileResponse).isNotNull();
        assertThat(profileResponse.getEmail()).isNotBlank();
    }

    /**
     * Given 깃허브로 부터 토큰을 받음.
     * When 깃허브가 인식할 수 없는 토큰으로 리소스 조회 API를 호출하면
     * Then 401 에러를 반환한다.
     * Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
     * */
    @DisplayName("리소스 조회 API 호출 실패, 깃허브가 인식할 수 없는 토큰 전송")
    @Test
    void getGithubProfileFromGithubFailedByInvalidToken() {
        assertThatThrownBy(() -> githubClient.getGithubProfileFromGithub("wrong_token"))
                .isInstanceOf(AuthenticationException.class);
    }

    /**
     * Given 깃허브로 부터 토큰을 받음.
     * When 만료기한이 지난 토큰으로 리소스 조회 API를 호출하면
     * Then 403 에러를 반환한다.
     * Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
     * */
    @DisplayName("리소스 조회 API 호출 실패, 만료기한 지난 토큰 전송")
    @Test
    void getGithubProfileFromGithubFailedByExpiredToken() {
        assertThatThrownBy(() -> githubClient.getGithubProfileFromGithub("expired_token"))
                .isInstanceOf(ForbiddenException.class);
    }
}
