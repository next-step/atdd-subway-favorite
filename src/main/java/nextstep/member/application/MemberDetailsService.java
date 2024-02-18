package nextstep.member.application;

import nextstep.auth.application.UserDetail;
import nextstep.auth.application.UserDetailsService;
import nextstep.auth.application.dto.OAuth2Response;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail loadUserByEmail(final String email) {
        final Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));
        return new UserDetail(member.getId(), member.getEmail(), member.getPassword());
    }

    @Override
    public UserDetail loadOrCreateUser(final OAuth2Response oAuth2Response) {
        final Member member = memberRepository.findByEmail(oAuth2Response.getEmail())
                .orElseGet(() -> memberRepository.save(new Member(oAuth2Response.getEmail(), UUID.randomUUID().toString(), oAuth2Response.getAge())));
        return new UserDetail(member.getId(), member.getEmail(), member.getPassword());
    }
}
