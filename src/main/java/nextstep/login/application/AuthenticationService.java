package nextstep.login.application;

import nextstep.login.application.dto.response.GithubProfileResponse;
import nextstep.login.application.dto.response.LoginResponse;
import nextstep.login.infra.JwtTokenProvider;
import nextstep.login.infra.SocialClient;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialClient socialClient;

    public AuthenticationService(
            final MemberRepository memberRepository,
            final JwtTokenProvider jwtTokenProvider,
            final SocialClient socialClient) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.socialClient = socialClient;
    }

    /**
     * 사용자 이메일과 비밀번호를 사용하여 로그인 인증을 진행합니다.
     *
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호
     * @return 사용자 인증 토큰
     */
    public LoginResponse login(final String email, final String password) {
        Member findMember = findMemberByEmail(email);
        findMember.validatePassword(password);

        return new LoginResponse(jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles()));
    }

    private Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 사용자 권한 증서를 사용하여 로그인 인증을 진행합니다.
     *
     * @param code 사용자 권한 증서
     * @return 사용자 인증 토큰
     */
    @Transactional
    public LoginResponse login(final String code) {
        String accessToken = socialClient.getAccessTokenFromGithub(code);
        GithubProfileResponse githubProfile = socialClient.getGithubProfileFromGithub(accessToken);

        if (isNotRegisteredMember(githubProfile.getEmail())) {
            Member member = saveNewOauthMember(githubProfile);
            return new LoginResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
        }

        Member findMember = findMemberByEmail(githubProfile.getEmail());
        findMember.validatePassword(findMember.getPassword());
        return new LoginResponse(jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles()));
    }

    private boolean isNotRegisteredMember(final String email) {
        return !memberRepository.existsByEmail(email);
    }

    private Member saveNewOauthMember(final GithubProfileResponse githubProfileResponse) {
        return memberRepository.save(Member.createMemberThroughOauth(githubProfileResponse.getEmail()));
    }
}
