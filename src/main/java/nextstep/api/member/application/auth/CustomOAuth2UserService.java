package nextstep.api.member.application.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.application.token.oauth2.OAuth2User;
import nextstep.api.auth.application.token.oauth2.OAuth2UserRequest;
import nextstep.api.auth.application.token.oauth2.OAuth2UserService;
import nextstep.api.member.domain.Member;
import nextstep.api.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest oAuth2UserRequest) {
        final var member = memberRepository.findByEmail(oAuth2UserRequest.getUsername())
                .orElseGet(() -> save(oAuth2UserRequest));

        return new CustomOAuth2User(member.getEmail(), member.getRole());
    }

    private Member save(final OAuth2UserRequest request) {
        return memberRepository.save(Member.oAuth2User(request.getUsername(), request.getAge()));
    }
}
