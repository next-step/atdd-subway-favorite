package nextstep.subway.member.ui;

import nextstep.subway.auth.domain.User;
import nextstep.subway.auth.infrastructure.AuthUser;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
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
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthUser User user) {
        return ResponseEntity.ok(memberService.findMemberByEmail(user.getUsername()));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@AuthUser User user, @PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.checkPermission(user, id);
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@AuthUser User user, @PathVariable Long id) {
        memberService.checkPermission(user, id);
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
