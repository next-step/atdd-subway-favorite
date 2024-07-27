package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "github-profile-api", url = "${github.url.profile}")
interface GithubProfileApi {
  @RequestMapping(
      method = RequestMethod.GET,
      value = "/user",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  GithubProfileResponse getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}
