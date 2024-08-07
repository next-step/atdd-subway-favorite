package nextstep.auth.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.auth.application.OauthClientService;
import nextstep.auth.application.TokenService;
import nextstep.auth.application.dto.OauthTokenRequest;
import nextstep.auth.application.dto.ResourceResponse;
import nextstep.member.application.dto.ApplicationTokenResponse;
import nextstep.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/login")
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthClientService oauthClientService;
    private final MemberService memberService;
    private final TokenService tokenService;

    @PostMapping("/github")
    public ResponseEntity<ApplicationTokenResponse> getAccessToken(@RequestBody OauthTokenRequest request) {
        ResourceResponse resourceResponse = oauthClientService.authenticate(request.getCode());
        Member member = memberService.findMemberByUserResource(resourceResponse);
        ApplicationTokenResponse applicationTokenResponse = tokenService.createToken(member.getEmail(), member.getPassword());
        return ResponseEntity.ok(applicationTokenResponse);
    }
}
