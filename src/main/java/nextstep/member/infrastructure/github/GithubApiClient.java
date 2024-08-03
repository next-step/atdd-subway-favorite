package nextstep.member.infrastructure.github;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;

@FeignClient(name = "github-api-client", url = "${github.api.url}")
public interface GithubApiClient {
// curl --request GET \
//--url "https://api.github.com/user" \
//--header "Accept: application/vnd.github+json" \
//--header "Authorization: Bearer USER_ACCESS_TOKEN" \
//--header "X-GitHub-Api-Version: 2022-11-28"
}