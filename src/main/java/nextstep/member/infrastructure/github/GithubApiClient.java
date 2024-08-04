package nextstep.member.infrastructure.github;

import nextstep.member.infrastructure.github.dto.GithubProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "github-api-client", url = "${github.api.url}", configuration = {GithubFeignClientConfig.class})
public interface GithubApiClient {
    @RequestMapping(method = RequestMethod.GET, value = "/user")
    GithubProfileResponse getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
}