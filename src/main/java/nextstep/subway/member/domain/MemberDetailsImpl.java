package nextstep.subway.member.domain;

import nextstep.subway.auth.domain.MemberDetails;

public class MemberDetailsImpl implements MemberDetails {
    private String email;
    private String password;

    public MemberDetailsImpl(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
