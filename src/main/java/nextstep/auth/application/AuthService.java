package nextstep.auth.application;

import nextstep.auth.application.dto.*;
import nextstep.auth.config.exception.MissingTokenException;
import nextstep.auth.domain.LoginUserInfo;
import nextstep.auth.infra.GithubClient;
import nextstep.auth.infra.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.auth.config.message.AuthError.NOT_MISSING_TOKEN;
import static nextstep.auth.config.message.AuthError.NOT_VALID_TOKEN;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public AuthService(final MemberService memberService, final JwtTokenProvider jwtTokenProvider, final GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse login(final TokenRequest request) {
        final Member member = memberService.findByEmail(request.getEmail());
        member.validatePassword(request.getPassword());

        final String token = createToken(request.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public GithubAccessTokenResponse loginByGithub(final GithubTokenRequest request) {
        final String accessToken = githubClient.getAccessTokenFromGithub(request.getCode());
        final GithubProfileResponse response = githubClient.getGithubProfileFromGithub(accessToken);
        final Member findMember = memberService.findByEmail(response.getEmail());
        return new GithubAccessTokenResponse(createToken(findMember));
    }

    public LoginUserInfo findMemberByToken(final String credentials) {
        if (credentials.isEmpty()) {
            throw new MissingTokenException(NOT_MISSING_TOKEN);
        }
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new MissingTokenException(NOT_VALID_TOKEN);
        }
        final String email = jwtTokenProvider.getPrincipal(credentials);
        final Member member = memberService.findByEmail(email);
        return LoginUserInfo.from(member);
    }

    private String createToken(final Member member) {
        return jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
    }

    private String createToken(final String email, final List<String> roles) {
        return jwtTokenProvider.createToken(email, roles);
    }
}
