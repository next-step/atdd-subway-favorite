package nextstep.subway.auth.application;

import nextstep.subway.auth.application.dto.MemberTokenRequest;
import nextstep.subway.auth.application.dto.UserTokenRequest;
import nextstep.subway.auth.application.dto.GithubTokenRequest;
import nextstep.subway.auth.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private UserTokenServiceProvider userTokenService;

    public TokenService(UserTokenServiceProvider userTokenService) {
        this.userTokenService = userTokenService;
    }

    public TokenResponse createToken(MemberTokenRequest request) {
        UserTokenRequest userTokenRequest = UserTokenRequest.fromMemberTokenRequest(request);
        return new TokenResponse(userTokenService.createToken(userTokenRequest));
    }

    public TokenResponse createGithubToken(GithubTokenRequest request) {
        UserTokenRequest userTokenRequest = UserTokenRequest.fromGithubTokenRequest(request);
        return new TokenResponse(userTokenService.createToken(userTokenRequest));
    }
}
