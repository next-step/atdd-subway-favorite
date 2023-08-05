package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.adapters.persistence.MemberJpaAdapter;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import nextstep.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberJpaAdapter memberJpaAdapter;

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberJpaAdapter.save(request.toEntity());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberJpaAdapter.findById(id);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberJpaAdapter.findById(id);
        member.update(param.toEntity());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberJpaAdapter.deleteById(id);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member =  memberJpaAdapter.findByEmail(email);
        return MemberResponse.of(member);
    }
}