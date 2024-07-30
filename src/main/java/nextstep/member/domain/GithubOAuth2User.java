package nextstep.member.domain;

import nextstep.auth.domain.OAuth2User;

public class GithubOAuth2User implements OAuth2User {
  private final String email;

  public GithubOAuth2User(String email) {
    this.email = email;
  }

  @Override
  public String getName() {
    return email;
  }
}
