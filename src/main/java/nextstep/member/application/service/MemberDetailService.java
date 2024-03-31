package nextstep.member.application.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import nextstep.auth.application.domain.CustomUserDetail;
import nextstep.auth.application.exception.AuthenticationException;
import nextstep.auth.application.service.UserDetailService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberDetailCustom;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberDetailService implements UserDetailService {

    public static final String DEFAULT_PASSWORD = UUID.randomUUID().toString();
    private final MemberRepository memberRepository;

    @Override
    public Optional<CustomUserDetail> findById(String id) {
        Member member = memberRepository.findByEmail(id).orElseThrow(AuthenticationException::new);
        return Optional.of(new MemberDetailCustom(member.getEmail(), member.getPassword()));
    }


    @Transactional
    @Override
    public CustomUserDetail loadUserDetail(String id) {
        Member member = memberRepository.findByEmail(id)
            .orElseGet(() -> memberRepository.save(new Member(id, DEFAULT_PASSWORD, null)));
        return new MemberDetailCustom(member.getEmail(), member.getPassword());
    }

}
