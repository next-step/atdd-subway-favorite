package nextstep.member.application;

import nextstep.auth.token.oauth2.OAuth2User;
import nextstep.auth.token.oauth2.OAuth2UserRequest;
import nextstep.auth.token.oauth2.OAuth2UserService;
import nextstep.member.domain.CustomOAuth2User;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService {
    private MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        Member member = memberRepository.findByEmail(oAuth2UserRequest.getUsername())
                .orElseGet(() -> memberRepository.save(new Member(oAuth2UserRequest.getUsername(), "", oAuth2UserRequest.getAge())));

        return new CustomOAuth2User(member.getEmail(), member.getRole());
    }
}