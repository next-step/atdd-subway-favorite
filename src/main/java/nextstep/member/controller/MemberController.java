package nextstep.member.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.service.MemberService;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        URI uri = URI.create(String.format("/members/%d", member.getId()));

        return ResponseEntity
                .created(uri)
                .build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = memberService.findMember(id);

        return ResponseEntity.ok(member);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);

        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        MemberResponse member = memberService.findMemberByEmail(userPrincipal.getEmail());

        return ResponseEntity.ok(member);
    }
}

