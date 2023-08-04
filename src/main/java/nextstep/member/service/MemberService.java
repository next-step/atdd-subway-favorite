package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.adapters.persistence.MemberAdapter;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import nextstep.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberAdapter memberAdapter;

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberAdapter.save(request.toEntity());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberAdapter.findById(id);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberAdapter.findById(id);
        member.update(param.toEntity());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberAdapter.deleteById(id);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member =  memberAdapter.findByEmail(email);
        return MemberResponse.of(member);
    }
}