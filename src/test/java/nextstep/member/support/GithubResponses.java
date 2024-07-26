package nextstep.member.support;

import static nextstep.Fixtures.aMember;

import lombok.Getter;
import nextstep.member.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
@Getter
public enum GithubResponses {
  사용자("aofijeowifjaoief", "access_token", aMember().build()),
  어드민("fau3nfin93dmn", "admin_access_token", aMember().id(2L).email("admin@example.com").build());

  GithubResponses(String code, String accessToken, Member member) {
    this.code = code;
    this.accessToken = accessToken;
    this.member = member;
  }

  private final String code;
  private final String accessToken;
  private final Member member;
}
