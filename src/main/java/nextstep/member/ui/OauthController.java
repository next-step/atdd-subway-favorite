package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberClientService;
import nextstep.member.application.MemberService;
import nextstep.member.application.OauthClientService;
import nextstep.member.application.dto.OauthTokenRequest;
import nextstep.member.application.dto.ResourceResponse;
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
    private final MemberClientService memberClientService;

    @PostMapping("/github")
    public ResponseEntity<ApplicationTokenResponse> getAccessToken(@RequestBody OauthTokenRequest request) {
        ResourceResponse resourceResponse = oauthClientService.authenticate(request.getCode());

        Member member;
        try {
            member = memberService.findMemberByEmail(resourceResponse.getEmail());
        } catch (RuntimeException exception) {
            memberClientService.enrollMember(resourceResponse.getEmail(), resourceResponse.getAge());
            member = memberService.findMemberByEmail(resourceResponse.getEmail());
        }

        ApplicationTokenResponse applicationTokenResponse = memberClientService.getMemberToken(member.getEmail());
        return ResponseEntity.ok(applicationTokenResponse);
    }
}
