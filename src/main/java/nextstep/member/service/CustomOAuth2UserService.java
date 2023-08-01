package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.oauth2.OAuth2User;
import nextstep.auth.oauth2.OAuth2UserRequest;
import nextstep.auth.oauth2.OAuth2UserService;
import nextstep.member.entity.CustomOAuth2User;
import nextstep.member.entity.Member;
import nextstep.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        Member member = memberRepository.findByEmail(oAuth2UserRequest.getUsername())
                .orElseGet(() -> memberRepository.save(Member.of(oAuth2UserRequest)));

        return CustomOAuth2User.of(member);
    }
}