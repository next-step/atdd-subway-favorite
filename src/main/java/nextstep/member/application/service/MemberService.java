package nextstep.member.application.service;

import nextstep.auth.application.service.UserDetailService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class MemberService implements UserDetailService {
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

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보가 존재하지 않습니다."));
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(MemberResponse::of)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public boolean isNotMember(String email, String password) {
        return !findMemberByEmail(email).checkPassword(password);
    }

    @Override
    public void createMemberIfNotExist(String email, String password, int age) {
        try {
            findMemberByEmail(email);
        } catch (EntityNotFoundException e) {
            createMember(new MemberRequest(email, password, age));
        }
    }
}
