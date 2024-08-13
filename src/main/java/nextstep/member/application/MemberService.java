package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.MemberRole;
import nextstep.member.domain.UserInfoAuthentication;
import nextstep.security.domain.Authentication;
import nextstep.security.domain.UserInfo;
import nextstep.security.application.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService<Long, String> {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new);
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(MemberResponse::of)
                .orElseThrow(RuntimeException::new);
    }


    @Override
    public Authentication<Long, String> authenticateByUserInfo(UserInfo userInfo) {
        String email = userInfo.getCredentials();
        Member member = findMemberByEmail(email);
        String password = userInfo.getPrincipal();
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }
        return UserInfoAuthentication.authenticated(member.getId(), member.getEmail(), MemberRole.MEMBER);
    }

}
