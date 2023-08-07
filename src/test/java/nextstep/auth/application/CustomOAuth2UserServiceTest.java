package nextstep.auth.application;

import nextstep.auth.token.oauth2.OAuth2UserRequest;
import nextstep.auth.token.oauth2.OAuth2UserService;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import nextstep.member.application.CustomOAuth2UserService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomOAuth2UserServiceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "";
    private static final int AGE = 20;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @DisplayName("회원이 존재하지 않을경우 저장 후 리턴한다.")
    @Test
    void notExistMemberThenSave() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(new Member(EMAIL, PASSWORD, AGE));

        // when
        customOAuth2UserService.loadUser(new GithubProfileResponse(EMAIL, AGE));

        // then
        verify(memberRepository, atLeastOnce()).save(any());
    }

    @DisplayName("회원이 존재하는경우 저장하지 않고 리턴한다.")
    @Test
    void existMemberThenReturn() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));

        // when
        customOAuth2UserService.loadUser(new GithubProfileResponse(EMAIL, AGE));

        // then
        verify(memberRepository, never()).save(any());
    }

}
