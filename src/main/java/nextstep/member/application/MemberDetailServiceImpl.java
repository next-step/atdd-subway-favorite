package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.MemberDetailService;
import nextstep.common.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDetailServiceImpl implements MemberDetailService {
    private final MemberRepository memberRepository;
    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));
    }

    @Override
    public Member findOrElseGet(String email) {
        return memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email)));
    }
}
