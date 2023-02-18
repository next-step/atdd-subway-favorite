package nextstep.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.exception.member.PasswordNotEqualException;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

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
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new PasswordNotEqualException();
        }
        String token = jwtTokenProvider.createToken(convertObjectAsString(MemberResponse.of(member)), member.getRoles());
        return new TokenResponse(token);
    }

    public MemberResponse findMemberOfMine(String accessToken) {
        jwtTokenProvider.validateToken(accessToken);
        String principal = jwtTokenProvider.getPrincipal(accessToken);
        return convertStringToMemberResponse(principal);
    }


    private MemberResponse convertStringToMemberResponse(String principal) {
        try {
            return objectMapper.readValue(principal, MemberResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertObjectAsString(MemberResponse memberResponse) {
        try {
            return objectMapper.writeValueAsString(memberResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
