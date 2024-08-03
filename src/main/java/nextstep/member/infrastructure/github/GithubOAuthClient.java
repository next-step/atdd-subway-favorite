package nextstep.member.infrastructure.github;

import nextstep.member.infrastructure.github.dto.GithubAccessTokenRequest;
import nextstep.member.infrastructure.github.dto.GithubAccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "github-oauth-client", url = "${github.oauth.url}")
public interface GithubOAuthClient {
    @RequestMapping(method = RequestMethod.POST, value = "/login/oauth/access_token", produces = "application/json")
    GithubAccessTokenResponse getAccessToken(@RequestBody() GithubAccessTokenRequest request);
}