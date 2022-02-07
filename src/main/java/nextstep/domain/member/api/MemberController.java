package nextstep.domain.member.api;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.domain.member.domain.LoginMemberImpl;
import nextstep.domain.member.dto.MemberRequest;
import nextstep.domain.member.dto.MemberResponse;
import nextstep.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

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
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMemberImpl member) {
        return ResponseEntity.ok().body(MemberResponse.of(member));
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal LoginMemberImpl member,
                                                             @RequestBody MemberRequest memberRequest) {
        memberService.updateMember(member.getId(), memberRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal LoginMemberImpl member) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }
}

