package atdd.member.web;

import atdd.member.application.MemberService;
import atdd.member.application.dto.JwtTokenResponse;
import atdd.member.application.dto.LoginMemberRequestView;
import atdd.member.security.JwtAuthenticationProvider;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class JwtAuthenticationController {

    private final JwtAuthenticationProvider provider;
    private final MemberService memberService;

    public JwtAuthenticationController(JwtAuthenticationProvider provider,
        MemberService memberService) {
        this.provider = provider;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<JwtTokenResponse> login(@Valid @RequestBody LoginMemberRequestView view) {
        memberService.authenticate(view.getEmail(), view.getPassword());
        return ResponseEntity.ok(new JwtTokenResponse(provider.create(view.getEmail())));
    }
}
