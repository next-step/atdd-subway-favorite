package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.GithubResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TokenServiceMockTest {

    @Mock
    private MemberRepository memberRepository;
    private MemberService memberService;
    private TokenService tokenService;
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
    private RestTemplate restTemplate = new RestTemplate();
    private GithubClient githubClient = new GithubClient(restTemplate);
    private GithubProfileResponse 사용자1;
    private Member member_사용자1;

    @BeforeEach
    void setup() {
        memberService = new MemberService(memberRepository);
        tokenService = new TokenService(memberService, jwtTokenProvider, githubClient);

        사용자1 = GithubProfileResponse.of(GithubResponse.사용자1.getEmail(), GithubResponse.사용자1.getAge());
        member_사용자1 = Member.of(1L, GithubResponse.사용자1.getEmail(), "password", GithubResponse.사용자1.getAge());

    }

    @DisplayName("[createOrSaveMember] 사용자가 조회되지 않으면, 사용자를 저장한 다음 저장한 값을 반환한다.")
    @Test
    public void memberDoesNotExist() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.ofNullable(null));
        when(memberRepository.save(any())).thenReturn(member_사용자1);
        // when
        var memberResponse = tokenService.createOrSaveMember(GithubProfileResponse.of(사용자1.getEmail(), 사용자1.getAge()));

        // then
        assertAll(
                () -> assertThat(memberResponse.getEmail()).isEqualTo(사용자1.getEmail()),
                () -> assertThat(memberResponse.getAge()).isEqualTo(사용자1.getAge()),
                () -> assertThat(memberResponse.getId()).isNotNull()
        );
    }

    @DisplayName("[createOrSaveMember] 사용자를 조회한 다음, 조회된 사용자를 반환한다.")
    @Test
    public void memberExist() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.ofNullable(member_사용자1));

        // when
        var memberResponse = tokenService.createOrSaveMember(사용자1);

        // then
        assertAll(
                () -> assertThat(memberResponse.getEmail()).isEqualTo(사용자1.getEmail()),
                () -> assertThat(memberResponse.getAge()).isEqualTo(사용자1.getAge()),
                () -> assertThat(memberResponse.getId()).isNotNull()
        );
    }

}

