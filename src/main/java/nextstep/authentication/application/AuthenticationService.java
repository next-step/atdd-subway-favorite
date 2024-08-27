package nextstep.authentication.application;

import nextstep.authentication.domain.AuthenticationInformation;
import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.authentication.domain.LoginMember;

public interface AuthenticationService {

    AuthenticationInformation findMemberByEmail(String email);

    LoginMember lookUpOrCreateMember(GithubProfileResponse profileResponse);
}
