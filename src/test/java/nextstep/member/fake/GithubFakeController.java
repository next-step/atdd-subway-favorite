package nextstep.member.fake;

import nextstep.member.application.dto.MemberResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Profile("test")
@RequestMapping("/github")
public class GithubFakeController {

    @PostMapping("/login")
    public GithubResponse fakeLogin(@RequestBody Map<String, String> map) {
        String code = map.get("code");

        return GithubResponse.getByCode(code);
    }

    @GetMapping("/members/me")
    public GithubResponse findMemberOfMine(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        return GithubResponse.getByAccessToken(accessToken);
    }

}
