package nextstep.member.ui;

import nextstep.common.auth.LoginRequired;
import nextstep.common.auth.MemberPayload;
import nextstep.common.auth.VerifiedMember;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody final MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable final Long id) {
        MemberResponse member = memberService.findMemberById(id);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable final Long id,
            @RequestBody final MemberRequest param
    ) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable final Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @LoginRequired
    public ResponseEntity<MemberResponse> findMemberOfMine(
            @VerifiedMember final MemberPayload memberPayload
    ) {
        MemberResponse member = memberService.findMemberResponseByEmail(memberPayload.getEmail());
        return ResponseEntity.ok().body(member);
    }
}
