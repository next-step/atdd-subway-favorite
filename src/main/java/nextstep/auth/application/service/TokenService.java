package nextstep.auth.application.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.domain.CustomUserDetail;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.application.exception.AuthenticationException;
import nextstep.auth.oauth.github.GithubAccessTokenResponse;
import nextstep.auth.oauth.github.GithubClient;
import nextstep.auth.oauth.github.GithubProfileResponse;
import nextstep.common.error.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TokenService {

    private final UserDetailService userDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;


    public TokenResponse createToken(String email, String password) {
        Optional<CustomUserDetail> userDetailOptional = userDetailService.findById(email);
        if (userDetailOptional.isEmpty()) {
            throw new NotFoundException();
        }
        CustomUserDetail customUserDetail = userDetailOptional.get();
        if (!customUserDetail.checkPassword(password)) {
            throw new AuthenticationException();
        }
        String token = jwtTokenProvider.createToken(customUserDetail.getId());
        return new TokenResponse(token);
    }


    @Transactional
    public TokenResponse createTokenByGithub(String code) {
        GithubAccessTokenResponse githubAccessTokenResponse = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfileResponse = githubClient
            .requestGithubProfile(githubAccessTokenResponse.getAccessToken());
        CustomUserDetail customUserDetail = userDetailService.findById(githubProfileResponse.getEmail())
            .orElseGet(() -> userDetailService.loadUserDetail(githubProfileResponse.getEmail()));
        return createToken(customUserDetail.getId(), customUserDetail.getPassword());
    }
}
