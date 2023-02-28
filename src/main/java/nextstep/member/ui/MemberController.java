package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.auth.annotation.AuthHeader;
import nextstep.auth.domain.AuthService;
import nextstep.auth.domain.AuthServices;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.*;
import nextstep.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final AuthServices authServices;

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = memberService.findMember(id);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthHeader String header) {
        AuthService auth = authServices.findAuth(header);
        Member member = auth.findMember(header);
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PostMapping("/login/token")
    public TokenResponse login(@RequestBody @Valid TokenRequest tokenRequest) {
        String token = memberService.jwtLogin(tokenRequest.getEmail(), tokenRequest.getPassword());
        return new TokenResponse(token);
    }

    @PostMapping("/login/github")
    public TokenResponse login(@RequestBody @Valid GithubTokenRequest request) {
        String accessToken = memberService.githubLogin(request.getCode());
        return new TokenResponse(accessToken);
    }
}

