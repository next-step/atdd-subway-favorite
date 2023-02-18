package nextstep.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    public TokenResponse loginMember(TokenRequest tokenRequest) {
        Member member = memberService.findMemberByEmail(tokenRequest.getEmail());
        member.checkPassword(tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(convertObjectAsString(MemberResponse.of(member)), member.getRoles());
        return new TokenResponse(token);
    }

    private String convertObjectAsString(MemberResponse memberResponse) {
        try {
            return objectMapper.writeValueAsString(memberResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
