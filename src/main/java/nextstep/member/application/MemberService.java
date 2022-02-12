package nextstep.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.common.domain.model.exception.EntityNotFoundException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {
    private static final String ENTITY_NAME_FOR_EXCEPTION = "사용자";
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member findById(long id) {
        return memberRepository.findById(id)
                               .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME_FOR_EXCEPTION));
    }

    @Transactional(readOnly = true)
    public void verifyExists(long id) {
        if (!memberRepository.existsById(id)) {
            throw new EntityNotFoundException(ENTITY_NAME_FOR_EXCEPTION);
        }
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = Member.builder()
            .email(request.getEmail())
            .password(request.getPassword())
            .age(request.getAge())
            .build();
        memberRepository.save(member);

        return MemberResponse.of(member);
    }

    public MemberResponse findMember(long id) {
        Member member = findById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(long id, MemberRequest param) {
        Member member = findById(id);
        member.update(param.getEmail(), param.getPassword(), param.getAge());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
