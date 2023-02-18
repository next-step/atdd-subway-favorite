package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.config.exception.MissingTokenException;
import nextstep.auth.domain.LoginUserInfo;
import nextstep.auth.infra.GithubResponses;
import nextstep.auth.infra.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static nextstep.auth.config.message.AuthError.NOT_MISSING_TOKEN;
import static nextstep.auth.config.message.AuthError.NOT_VALID_TOKEN;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberService memberService, final JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(final TokenRequest request) {
        final Member member = memberService.findByEmail(request.getEmail());
        member.validatePassword(request.getPassword());

        final String token = jwtTokenProvider.createToken(request.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public GithubAccessTokenResponse loginByGithub(final GithubAccessTokenRequest request) {
        final String code = request.getCode();
        try {
            final GithubResponses githubResponses = GithubResponses.find(code);
            return new GithubAccessTokenResponse(githubResponses.getAccessToken());
        } catch (IllegalArgumentException ie) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
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
}
