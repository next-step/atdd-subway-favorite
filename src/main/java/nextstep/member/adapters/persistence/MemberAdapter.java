package nextstep.member.adapters.persistence;

import lombok.RequiredArgsConstructor;
import nextstep.global.error.code.ErrorCode;
import nextstep.global.error.exception.NotEntityFoundException;
import nextstep.member.entity.Member;
import nextstep.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAdapter {

    private final MemberRepository memberRepository;

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotEntityFoundException(ErrorCode.NOT_EXIST_MEMBER));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotEntityFoundException(ErrorCode.NOT_EXIST_MEMBER));
    }

    @Transactional
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
