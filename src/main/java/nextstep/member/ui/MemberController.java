package nextstep.member.ui;

import nextstep.auth.AuthMember;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.auth.Auth;
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
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<AuthMember> findMemberOfMine(@Auth AuthMember authMember) {
        return ResponseEntity.ok().body(authMember);
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@Auth AuthMember authMember, @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = memberService.createFavorite(authMember, request);

        return ResponseEntity.created(URI.create("/favorites/" + favorite.getMemberId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<FavoriteResponse> getFavoriteOfMine(@Auth AuthMember authMember) {
        FavoriteResponse favoriteResponseOfMine = memberService.findFavoriteOfMine(authMember);
        return ResponseEntity.ok().body(favoriteResponseOfMine);
    }
}
