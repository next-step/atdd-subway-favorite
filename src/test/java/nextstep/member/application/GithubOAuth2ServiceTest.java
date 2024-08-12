package nextstep.member.application;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.member.application.FakeGithubOAuth2Client.GithubResponses;
import nextstep.member.domain.GithubOAuth2Client;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.security.service.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GithubOAuth2ServiceTest {

    private GithubOAuth2Service githubOAuth2Service;

    private MemberRepository memberRepository;
    private GithubOAuth2Client githubOAuth2Client;
    private JwtTokenProvider jwtTokenProvider;


    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        githubOAuth2Client = new FakeGithubOAuth2Client();
        jwtTokenProvider = new JwtTokenProvider("test-secret-key", 360000);
        githubOAuth2Service = new GithubOAuth2Service(memberRepository, githubOAuth2Client, jwtTokenProvider);
    }


    @DisplayName("기존에 회원이 있으면 email이 일치하는 회원을 찾아 accessToken을 발행한다")
    @Test
    void whenMemberExistThenReturnAccessToken() {
        //Given 기존에 회원이 있으면
        var 회원1 = GithubResponses.사용자1;
        when(memberRepository.findByEmail(회원1.getEmail()))
                .thenReturn(Optional.of(new Member(회원1.getEmail())));

        //Then email이 일치하는 회원을 찾아 accessToken을 발행한다
        githubOAuth2Service.createToken(회원1.getCode()).getAccessToken();

        //Then 회원 생성 로직은 실행되지 않는다
        verify(memberRepository, times(0)).save(new Member(회원1.getEmail()));

    }

    @DisplayName("기존에 회원이 없으면 member 생성후 accessToken을 발행한다")
    @Test
    void whenMemberNonExistThenCreateMemberThenReturnAccessToken() {
        //Given 기존에 회원이 없으면
        var 회원1 = GithubResponses.사용자1;
        when(memberRepository.findByEmail(회원1.getEmail()))
                .thenReturn(Optional.empty());

        when(memberRepository.save(any(Member.class)))
                .thenReturn(new Member(회원1.getEmail()));

        //When 요청시
        githubOAuth2Service.createToken(회원1.getCode());

        //Then 회원 생성 로직이 실행된다
        verify(memberRepository, times(1)).save(any(Member.class));
    }

}
