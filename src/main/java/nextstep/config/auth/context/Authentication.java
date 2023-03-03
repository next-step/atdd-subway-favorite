package nextstep.config.auth.context;

import nextstep.member.application.dto.MemberResponse;

public class Authentication {
    private String accessToken;
    private MemberResponse memberResponse;

    public Authentication() {
    }

    public Authentication(String accessToken, MemberResponse memberResponse) {
        this.accessToken = accessToken;
        this.memberResponse = memberResponse;
    }

    public static Authentication of(String accessToken, MemberResponse memberResponse) {
        return new Authentication(accessToken, memberResponse);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public MemberResponse getMemberResponse() {
        return memberResponse;
    }
}
