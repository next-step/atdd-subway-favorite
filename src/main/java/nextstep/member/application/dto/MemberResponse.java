package nextstep.member.application.dto;

import lombok.Getter;
import nextstep.member.domain.Member;

@Getter
public class MemberResponse {
  private final Long id;
  private final String email;
  private final Integer age;

  public MemberResponse() {
    this(null, null, null);
  }

  public MemberResponse(Long id, String email, Integer age) {
    this.id = id;
    this.email = email;
    this.age = age;
  }

  public static MemberResponse of(Member member) {
    return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
  }
}
