package nextstep.member.application;

import nextstep.exception.BadRequestException;
import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email)
                .orElseThrow(() -> new BadRequestException("요청하신 사용자 정보는 올바르지 않은 정보입니다."));
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String accessToken = githubClient.requestGithubToken(code);

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(accessToken);

        Optional<Member> memberByEmail = memberService.findMemberByEmail(githubProfileResponse.getEmail());

        if (memberByEmail.isPresent()) {
            return TokenResponse.builder()
                    .accessToken(jwtTokenProvider.createToken(memberByEmail.get().getEmail()))
                    .build();
        } else {
            MemberResponse member = memberService.createMember(MemberRequest.builder()
                    .email(githubProfileResponse.getEmail())
                    .age(githubProfileResponse.getAge())
                    .password(githubProfileResponse.getPassword())
                    .build());

            return TokenResponse.builder()
                    .accessToken(jwtTokenProvider.createToken(member.getEmail()))
                    .build();
        }
    }
}
