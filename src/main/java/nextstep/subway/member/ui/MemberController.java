package nextstep.subway.member.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

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
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(new MemberResponse(loginMember.getId(), loginMember.getEmail(), loginMember.getAge()));
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember,
        @RequestBody MemberRequest paramsMap) {
        checkValidationLoginMember(loginMember);
        memberService.updateMember(loginMember.getId(), paramsMap);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        checkValidationLoginMember(loginMember);
        memberService.deleteMember(loginMember.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    private void checkValidationLoginMember(@AuthenticationPrincipal LoginMember loginMember) {
        if (loginMember == null) {
            throw new RuntimeException("invalid authorization");
        }
    }
}
