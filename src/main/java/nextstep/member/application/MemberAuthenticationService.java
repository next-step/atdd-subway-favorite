package nextstep.member.application;

import nextstep.authentication.application.AuthenticationService;
import nextstep.authentication.domain.AuthenticationInformation;
import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.authentication.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberAuthenticationService implements AuthenticationService {

    private final MemberService memberService;

    public MemberAuthenticationService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public AuthenticationInformation findMemberByEmail(String email) {
        Member member = memberService.findMemberByEmail(email);
        return new AuthenticationInformation(member.getEmail(), member.getId(), member.getPassword());
    }

    @Override
    @Transactional
    public LoginMember lookUpOrCreateMember(GithubProfileResponse profileResponse) {
        Member member = memberService.lookUpOrCreateMember(profileResponse);
        return new LoginMember(member.getEmail(), member.getId());
    }
}