package nextstep.subway.member.application;

import nextstep.subway.member.domain.LoginMember;

public interface UserDetailService {
    LoginMember loadUserByUserName(String userName);
}
