package nextstep.member.domain;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;

public interface MemberClient {

    MemberResponse enrollMember(MemberRequest memberRequest);

    TokenResponse getMemberToken(TokenRequest tokenRequest);
}
