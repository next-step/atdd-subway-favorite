package nextstep.member.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.RoleType;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    public TokenResponse login(TokenRequest request) {
        MemberResponse member = memberService.findMember(request.getEmail(), request.getPassword());
        String token = tokenProvider.createToken(member.getEmail(), List.of(RoleType.ROLE_MEMBER.name()));
        return TokenResponse.of(token);
    }
}
