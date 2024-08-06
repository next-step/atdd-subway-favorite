package nextstep.member.controller;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationPrincipal;
import nextstep.member.domain.command.MemberService;
import nextstep.member.controller.dto.MemberRequest;
import nextstep.member.controller.dto.MemberResponse;
import nextstep.auth.LoginMember;
import nextstep.member.domain.query.MemberReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberReader memberReader;

    @PostMapping("")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = MemberResponse.of(memberService.createMember(request.toCreateCommand()));;
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = MemberResponse.of(memberReader.getMember(id));
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest request) {
        memberService.updateMember(request.toUpdateCommand(id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse memberResponse = MemberResponse.of(memberReader.getMe(loginMember.getEmail()));
        return ResponseEntity.ok().body(memberResponse);
    }
}

