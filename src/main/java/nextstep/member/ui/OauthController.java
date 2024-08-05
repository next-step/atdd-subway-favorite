package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberClientService;
import nextstep.member.application.MemberService;
import nextstep.member.application.OauthService;
import nextstep.member.application.dto.AccessTokenRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.ResourceResponse;
import nextstep.member.application.dto.TokenResponse;
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

    private final OauthService oauthService;
    private final MemberService memberService;
    private final MemberClientService memberClientService;

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> getAccessToken(@RequestBody AccessTokenRequest request) {
        ResourceResponse resourceResponse = oauthService.authenticate(request.getCode());

        Member member;
        try {
            member = memberService.findMemberByEmail(resourceResponse.getEmail());
        } catch (RuntimeException exception) {
            memberClientService.enrollMember(resourceResponse.getEmail(), resourceResponse.getAge());
            member = memberService.findMemberByEmail(resourceResponse.getEmail());
        }

        TokenResponse tokenResponse = memberClientService.getMemberToken(member.getEmail());
        return ResponseEntity.ok(tokenResponse);
    }
}
