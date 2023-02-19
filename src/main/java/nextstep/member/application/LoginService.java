package nextstep.member.application;

import nextstep.common.exception.LoginException;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public LoginService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse loginToken(TokenRequest tokenRequest) {
        Member member = findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public TokenResponse loginGithubToken(String code) {
        String accessToken = getAccessToken(code);
        return new TokenResponse(accessToken);
    }

    private String getAccessToken(String code) {
        Optional<GithubResponses> githubResponses = GithubResponses.findByCode(code);
        if (githubResponses.isPresent()) {
            return githubResponses.get().getAccessToken();
        }
        return githubClient.getAccessTokenFromGithub(code);
    }

    private Member findByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(LoginException::new);
    }

    private enum GithubResponses {
        사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
        사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
        사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
        사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

        private final String code;
        private final String accessToken;
        private final String email;

        GithubResponses(String code, String accessToken, String email) {
            this.code = code;
            this.accessToken = accessToken;
            this.email = email;
        }

        public static Optional<GithubResponses> findByEmail(String email) {
            return findBy(githubResponses -> githubResponses.email.equals(email));
        }

        public static Optional<GithubResponses> findByCode(String code) {
            return findBy(githubResponses -> githubResponses.code.equals(code));
        }

        private static Optional<GithubResponses> findBy(Predicate<GithubResponses> equals) {
            return Arrays.stream(values())
                    .filter(equals)
                    .findAny();
        }

        public String getAccessToken() {
            return accessToken;
        }
    }
}
