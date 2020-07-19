package nextstep.subway.member.ui;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

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
    public ResponseEntity<MemberResponse> findMemberOfMine() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        return ResponseEntity.ok().body(memberService.findMember(loginMember.getId()));
    }

    @Deprecated
    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @Deprecated
    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/members/{id}/token")
    public ResponseEntity<MemberResponse> updateMemberWithToken(@PathVariable Long id, @RequestBody MemberRequest param) {
        // TODO parameter로 변경
        LoginMember loginMember = (LoginMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.equals(id, loginMember.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        memberService.updateMember(loginMember.getId(), param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}/token")
    public ResponseEntity<MemberResponse> deleteMemberWithToken(@PathVariable Long id) {
        // TODO parameter로 변경
        LoginMember loginMember = (LoginMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.equals(id, loginMember.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        memberService.deleteMember(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
