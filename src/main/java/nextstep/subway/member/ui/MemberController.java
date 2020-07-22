package nextstep.subway.member.ui;

import nextstep.subway.auth.application.AuthenticationService;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
public class MemberController {
    private final MemberService memberService;
    // TODO 마음에 들지 않는다............ ArgumentResolver로 해결하자
    private final AuthenticationService authenticationService;

    public MemberController(MemberService memberService, AuthenticationService authenticationService) {
        this.memberService = memberService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = memberService.findMember(id);
        return ResponseEntity.ok().body(member);
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine() {
        Optional<LoginMember> loginMemberOptional = authenticationService.getLoginMember();
        if (!loginMemberOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MemberResponse member = memberService.findMember(loginMemberOptional.get().getId());
        return ResponseEntity.ok(member);

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
}
