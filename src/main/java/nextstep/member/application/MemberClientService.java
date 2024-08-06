package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.ApplicationTokenRequest;
import nextstep.member.application.dto.ApplicationTokenResponse;
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

    public ApplicationTokenResponse getMemberToken(String email) {
        ApplicationTokenRequest applicationTokenRequest = new ApplicationTokenRequest(email, OAUTH_DEFAULT_PASSWORD);
        return memberClient.getMemberToken(applicationTokenRequest);
    }

}
