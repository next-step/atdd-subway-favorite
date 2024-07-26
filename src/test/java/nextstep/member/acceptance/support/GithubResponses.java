package nextstep.member.acceptance.support;

import static nextstep.Fixtures.aMember;

import lombok.Getter;
import nextstep.member.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
@Getter
public enum GithubResponses {
  사용자("aofijeowifjaoief", aMember().build()),
  어드민("fau3nfin93dmn", aMember().id(2L).email("admin@example.com").build());

  GithubResponses(String code, Member member) {
    this.code = code;
    this.member = member;
  }

  private final String code;
  private final Member member;
}
