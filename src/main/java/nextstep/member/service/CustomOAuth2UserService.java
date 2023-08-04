package nextstep.member.service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.oauth2.dto.OAuth2User;
import nextstep.auth.oauth2.dto.OAuth2UserRequest;
import nextstep.auth.oauth2.service.OAuth2UserService;
import nextstep.auth.oauth2.vo.CustomOAuth2User;
import nextstep.member.adapters.persistence.MemberAdapter;
import nextstep.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {

    private final MemberAdapter memberAdapter;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        String email = oAuth2UserRequest.getUsername();
        boolean exists = memberAdapter.existsByEmail(email);

        Member member = exists ? memberAdapter.findByEmail(email) :  memberAdapter.save(Member.of(oAuth2UserRequest));

        return CustomOAuth2User.of(member);
    }
}