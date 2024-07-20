package nextstep.member.application.dto;

import lombok.Getter;
import nextstep.member.domain.Member;

@Getter
public class MemberRequest {
  private final String email;
  private final String password;
  private final Integer age;

  public MemberRequest() {
    this(null, null, null);
  }

  public MemberRequest(String email, String password, Integer age) {
    this.email = email;
    this.password = password;
    this.age = age;
  }

  public Member toMember() {
    return new Member(email, password, age);
  }
}
