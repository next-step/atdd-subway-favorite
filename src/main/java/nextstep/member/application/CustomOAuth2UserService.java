package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.oauth2.OAuth2User;
import nextstep.auth.oauth2.OAuth2UserRequest;
import nextstep.auth.oauth2.OAuth2UserService;
import nextstep.member.domain.CustomOAuth2User;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUserByOAuth2User(OAuth2UserRequest oAuth2UserRequest) {
        Member member = memberRepository.findByEmail(oAuth2UserRequest.getUsername())
                .orElseGet(() -> memberRepository.save(new Member(oAuth2UserRequest.getUsername(), "", oAuth2UserRequest.getAge())));

        return new CustomOAuth2User(member.getId(), member.getEmail());
    }
}
