package nextstep.login.application;

import nextstep.login.application.dto.response.GithubProfileResponse;
import nextstep.login.application.dto.response.LoginResponse;
import nextstep.login.infra.JwtTokenProvider;
import nextstep.login.infra.SocialClient;
import nextstep.member.application.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

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

    public LoginResponse login(final String email, final String password) {
        Member findMember = findMemberByEmail(email);
        findMember.validatePassword(password);

        return new LoginResponse(jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles()));
    }

    private Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }


    /*
        1. Github에 code(권한 증서)를 가지고 accessToken 발급
        2. 발급받은 accessToken에서 email을 사용해서 member 테이블 조회
        3. member 테이블에서 조회한 권한 목록(roles)와 email을 사용하여 JWT Token 발급
        4. 발급한 JWT Token 반환
     */
    public LoginResponse login(final String code) {
        String accessToken = socialClient.getAccessTokenFromGithub(code);
        GithubProfileResponse githubProfileResponse = socialClient.getGithubProfileFromGithub(accessToken);
        Member findMember = findMemberByEmail(githubProfileResponse.getEmail());
        findMember.validatePassword(findMember.getPassword());

        return new LoginResponse(jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles()));
    }
}
