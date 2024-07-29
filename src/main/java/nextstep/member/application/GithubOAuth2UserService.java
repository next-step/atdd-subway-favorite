package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.GithubProfileClient;
import nextstep.auth.application.OAuth2UserService;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.dto.OAuth2UserRequest;
import nextstep.auth.domain.OAuth2User;
import nextstep.auth.exception.AuthenticationException;
import nextstep.member.domain.GithubOAuth2User;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubOAuth2UserService implements OAuth2UserService {
  private final GithubProfileClient githubProfileClient;
  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    String accessToken = userRequest.getAccessToken();
    GithubProfileResponse profile = githubProfileClient.getProfile(accessToken);
    Member member =
        memberRepository.findByEmail(profile.getEmail()).orElseThrow(AuthenticationException::new);
    return new GithubOAuth2User(member.getEmail());
  }
}
