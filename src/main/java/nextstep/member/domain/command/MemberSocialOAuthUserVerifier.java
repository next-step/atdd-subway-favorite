package nextstep.member.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.command.SocialOAuthUserVerifier;
import nextstep.auth.domain.entity.SocialOAuthUser;
import nextstep.auth.domain.entity.TokenPrincipal;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberSocialOAuthUserVerifier implements SocialOAuthUserVerifier {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public TokenPrincipal verify(SocialOAuthUser socialOAuthUser) {
        Member member = memberRepository.findByEmail(socialOAuthUser.getEmail())
                .orElseGet(() -> memberRepository.save(Member.ofEmail(socialOAuthUser.getEmail())));
        return new TokenPrincipal(member.getId(), member.getEmail());
    }
}
