package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

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
    public ResponseEntity<MemberResponse> findMemberOfMine(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        authorization = authorization.replace("Bearer ", "");

        if (!jwtTokenProvider.validateToken(authorization)) {
            throw new IllegalArgumentException("인증 헤더 정보가 유효하지 않습니다. " + authorization);
        }

        String principal = jwtTokenProvider.getPrincipal(authorization);
        MemberResponse member = memberService.findByEmail(principal);
        return ResponseEntity.ok().body(member);
    }

    @PostMapping("/login/token")
    public TokenResponse login(@RequestBody @Valid TokenRequest tokenRequest) {
        // tokenRequest 를 직접 넘기는게 좋을까? 직접 넘기게되면 가독성은 좋아보이지만 tokenRequest 에대한 의존성이 생긴다
        Member member = memberService.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }
}

