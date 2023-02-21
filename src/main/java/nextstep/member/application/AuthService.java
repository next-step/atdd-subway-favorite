package nextstep.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.exception.member.AuthTokenIsExpiredException;
import nextstep.exception.member.PasswordNotEqualException;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.utils.ObjectStringMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse loginMember(TokenRequest tokenRequest) {
        Member member = memberService.findMemberByEmail(tokenRequest.getEmail());
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new PasswordNotEqualException();
        }
        String token = jwtTokenProvider.createToken(ObjectStringMapper.convertObjectAsString(MemberResponse.of(member)), member.getRoles());
        return new TokenResponse(token);
    }
}
