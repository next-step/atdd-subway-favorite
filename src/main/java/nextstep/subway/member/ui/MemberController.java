package nextstep.subway.member.ui;

import nextstep.subway.member.application.MemberService;
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

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> getMemberOfMine() {
        Long id = 1L;
        MemberResponse member = memberService.findMember(id);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@RequestBody MemberRequest param) {
        Long id = 1L;
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine() {
        Long id = 1L;
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
