package nextstep.subway.ui;

import nextstep.auth.model.authorization.AuthenticationPrincipal;
import nextstep.subway.application.MemberService;
import nextstep.subway.application.dto.member.MemberRequest;
import nextstep.subway.application.dto.member.MemberResponse;
import nextstep.subway.domain.member.MemberAdaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;
    private static final String SPRING_SECURITY_CONTEXT_KEY = "SECURITY_CONTEXT";

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + response.getId())).body(response);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        return ResponseEntity.ok().body(memberService.findMember(id));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        return ResponseEntity.ok().body(memberService.updateMember(id, param));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal MemberAdaptor memberAdaptor) {
        return ResponseEntity.ok().body(memberService.findMember(memberAdaptor.getId()));
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine() {
        return ResponseEntity.noContent().build();
    }
}

