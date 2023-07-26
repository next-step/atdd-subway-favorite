package subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.auth.principal.AuthenticationPrincipal;
import subway.auth.principal.UserPrincipal;
import subway.member.application.MemberService;
import subway.member.application.dto.MemberRequest;
import subway.member.application.dto.MemberRetrieveResponse;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberRetrieveResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberRetrieveResponse> findMember(@PathVariable Long id) {
        MemberRetrieveResponse member = memberService.findMember(id);
        return ResponseEntity.ok().body(member);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberRetrieveResponse> findMemberByToken(@AuthenticationPrincipal UserPrincipal principal) {
        MemberRetrieveResponse member = memberService.findMember(principal);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberRetrieveResponse> updateMember(@PathVariable Long id,
                                                               @RequestBody MemberRequest param) {
        // TODO : 이거 DTO 쪼개야됨...
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberRetrieveResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}

