package nextstep.auth.unit;


public class GithubClientTest {

    /**
     * Given 깃허브로 부터 코드를 받음.
     * When 받은 코드로 토큰 요청 API를 호출하면
     * Then accessToken이 Json body에 담겨 응답이 온다.
     * Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
     * */

    /**
     * Given 깃허브로 부터 코드를 받음.
     * When 깃허브가 인식할 수 없는 코드로 토큰 요청 API를 호출하면
     * Then accessToken이 Json body에 담겨 오지 않아, HttpClientErrorException가 발생한다.
     * Ref. https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github
     * */

    /**
     * Given 깃허브로 부터 토큰을 받음.
     * When 받은 토큰으로 리소스 조회 API를 호출하면
     * Then 리소스응답 템플릿에 맞춰 Json body를 받는다.
     * Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
     * */

    /**
     * Given 깃허브로 부터 토큰을 받음.
     * When 깃허브가 인식할 수 없는 토큰으로 리소스 조회 API를 호출하면
     * Then 401 에러를 반환한다.
     * Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
     * */

    /**
     * Given 깃허브로 부터 토큰을 받음.
     * When 만료기한이 지난 토큰으로 리소스 조회 API를 호출하면
     * Then 403 에러를 반환한다.
     * Ref. https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-the-authenticated-user
     * */
}
