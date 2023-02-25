package nextstep.member.fake;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login/github")
@RestController
public class GithubFakeController {

    @PostMapping
    public GithubResponse fakeLogin(@RequestBody String code) {
        return GithubResponse.getByCode(code);
    }
}
