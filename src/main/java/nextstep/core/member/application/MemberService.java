package nextstep.core.member.application;

import nextstep.core.member.application.dto.MemberRequest;
import nextstep.core.member.application.dto.MemberResponse;
import nextstep.core.auth.domain.LoginMember;
import nextstep.core.member.domain.Member;
import nextstep.core.member.domain.MemberRepository;
import nextstep.core.member.exception.NotFoundMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        return MemberResponse.of(memberRepository.save(request.toMember()));
    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findMemberById(id));
    }

    public void updateMember(Long id, MemberRequest param) {
        findMemberById(id).update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NotFoundMemberException("회원 정보가 없습니다."));
    }

    public MemberResponse findMe(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(MemberResponse::of)
                .orElseThrow(() -> new NotFoundMemberException("회원 정보가 없습니다."));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException("회원 정보가 없습니다."));
    }

    public Member findOrCreate(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElseGet(() -> memberRepository.save(new Member(email, UUID.randomUUID().toString())));
    }
}