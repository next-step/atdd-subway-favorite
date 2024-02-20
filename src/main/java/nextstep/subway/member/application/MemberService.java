package nextstep.subway.member.application;

import nextstep.subway.member.application.dto.MemberRequest;
import nextstep.subway.member.application.dto.MemberResponse;
import nextstep.auth.client.dto.ProfileResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(LoginMember loginMember,
                             Long id,
                             MemberRequest param) {
        Member accessMember = findMemberByEmail(loginMember.getEmail());
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        accessMember.update(member, param.toMember());
    }

    @Transactional
    public void deleteMember(LoginMember loginMember,
                             Long id) {
        Member accessMember = findMemberByEmail(loginMember.getEmail());
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        accessMember.validAccess(member);
        memberRepository.delete(accessMember);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(it -> MemberResponse.of(it))
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void findMemberByEmailNotExistSave(ProfileResponse response) {
        Member member = memberRepository.findByEmail(response.getEmail()).orElse(new Member(response.getEmail(), "", response.getAge()));
        memberRepository.save(member);
    }
}
