package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.MemberClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberClientService {

    private static final String OAUTH_DEFAULT_PASSWORD = "defaultPassword";

    private final MemberClient memberClient;

    public MemberResponse enrollMember(String email, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, OAUTH_DEFAULT_PASSWORD, age);
        return memberClient.enrollMember(memberRequest);
    }

    public TokenResponse getMemberToken(String email) {
        TokenRequest tokenRequest = new TokenRequest(email, OAUTH_DEFAULT_PASSWORD);
        return memberClient.getMemberToken(tokenRequest);
    }

}
