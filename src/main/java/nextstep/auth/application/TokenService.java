package nextstep.auth.application;

import nextstep.favorite.application.GithubClient;
import nextstep.favorite.application.dto.GithubProfileResponse;
import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;
    private UserDetailsService userDetailsService;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient, UserDetailsService userDetailsService) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
        this.userDetailsService = userDetailsService;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createToken_new(String email, String password) {
        UserDetails userDetails = userDetailsService.findMemberByEmail(email);
        if (!userDetails.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(userDetails.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(final String code) {
        String githubToken = githubClient.requestGithubToken(code);
        if (StringUtils.isEmpty(githubToken)) {
            throw new AuthenticationException();
        }
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);
        Member member = memberService.findOrCreateMember(githubProfileResponse);
        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken_new(final String code) {
        String githubToken = githubClient.requestGithubToken(code);
        if (StringUtils.isEmpty(githubToken)) {
            throw new AuthenticationException();
        }
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);
        UserDetails userDetails = userDetailsService.findOrCreateMember(githubProfileResponse.getEmail(), githubProfileResponse.getAge());
        String token = jwtTokenProvider.createToken(userDetails.getEmail());

        return new TokenResponse(token);
    }
}
