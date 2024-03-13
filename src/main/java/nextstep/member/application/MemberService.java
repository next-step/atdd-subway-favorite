package nextstep.member.application;

import nextstep.auth.application.UserDetails;
import nextstep.auth.application.UserDetailsService;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.exception.BadRequestException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService {
    private MemberRepository memberRepository;

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

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(it -> MemberResponse.of(it))
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public UserDetails findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException("존재하지 않는 회원입니다.")
        );
    }

    @Override
    public UserDetails findMemberOrCreate(GithubProfileResponse githubProfileResponse) {
        UserDetails member = memberRepository.findByEmail(githubProfileResponse.getEmail()).orElse(null);

        if(member == null) {
            member = memberRepository.save(new Member(githubProfileResponse.getEmail(), "", githubProfileResponse.getAge()));
        }

        return member;
    }
}