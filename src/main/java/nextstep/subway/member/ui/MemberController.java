package nextstep.subway.member.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
public class MemberController {
    private final MemberService memberService;

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
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok().body(memberService.findMember(principal.getId()));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMemberWithToken(@PathVariable Long id, @RequestBody MemberRequest param,
                                                                @AuthenticationPrincipal UserDetails principal) {
        if (!Objects.equals(id, principal.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMemberWithToken(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        if (!Objects.equals(id, principal.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
