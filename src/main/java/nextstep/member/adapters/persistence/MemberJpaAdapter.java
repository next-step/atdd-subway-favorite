package nextstep.member.adapters.persistence;

import lombok.RequiredArgsConstructor;
import nextstep.global.error.code.ErrorCode;
import nextstep.global.error.exception.AuthenticationException;
import nextstep.global.error.exception.NotEntityFoundException;
import nextstep.member.entity.Member;
import nextstep.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberJpaAdapter {

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

    public Member loginByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.LOGIN_ERROR));
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
