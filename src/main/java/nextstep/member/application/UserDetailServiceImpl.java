package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.domain.UserDetails;
import nextstep.common.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails findByEmail(String email) {
        var member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));
        return UserDetails.from(member);
    }

    @Override
    public UserDetails findOrElseGet(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email)));
        return UserDetails.from(member);
    }
}
