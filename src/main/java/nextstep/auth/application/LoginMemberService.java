package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.OAuth2User;
import nextstep.auth.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.auth.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginMemberService {
    private final MemberService memberService;

    public LoginMember loadMember(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
        return new LoginMember(member.getId(), member.getEmail());
    }

    public LoginMember loadMember(OAuth2User oAuth2User) {
        Member member = memberService.findOrCreateMember(oAuth2User.toMember());
        return new LoginMember(member.getId(), member.getEmail());
    }
}
