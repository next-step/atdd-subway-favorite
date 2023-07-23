package subway.member.application;

import lombok.RequiredArgsConstructor;
import subway.auth.token.oauth2.OAuth2User;
import subway.auth.token.oauth2.OAuth2UserRequest;
import subway.auth.token.oauth2.OAuth2UserService;
import subway.member.domain.CustomOAuth2User;
import subway.member.domain.Member;
import subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        Member memberRequest = Member.builder().email(oAuth2UserRequest.getUsername()).password("").age(oAuth2UserRequest.getAge()).build();
        Member member = memberRepository.findByEmail(oAuth2UserRequest.getUsername())
                .orElseGet(() -> memberRepository.save(memberRequest));

        return CustomOAuth2User.from(member);
    }
}