package nextstep.member.domain;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.ApplicationTokenRequest;
import nextstep.member.application.dto.ApplicationTokenResponse;

public interface MemberClient {

    MemberResponse enrollMember(MemberRequest memberRequest);

    ApplicationTokenResponse getMemberToken(ApplicationTokenRequest applicationTokenRequest);
}
