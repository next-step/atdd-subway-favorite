package nextstep.member.ui;

import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

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
        MemberResponse memberResponse = memberService.updateMember(id, param);

        return ResponseEntity.ok().body(memberResponse);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine() {
        LoginMember loginMember = loginMember();
        MemberResponse member = memberService.findMemberOfMine(loginMember);

        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine() {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine() {


        return ResponseEntity.noContent().build();
    }

    private LoginMember loginMember() {
        SecurityContext context = SecurityContextHolder.getContext();

        return (LoginMember) context.getAuthentication().getPrincipal();
    }
}

