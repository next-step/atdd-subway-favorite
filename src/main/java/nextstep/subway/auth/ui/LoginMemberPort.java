package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.LoginMember;

public interface LoginMemberPort {
    LoginMember getLoginMember(String principal);
}
