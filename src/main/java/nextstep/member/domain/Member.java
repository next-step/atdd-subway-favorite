package nextstep.member.domain;

import java.util.Objects;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;
  private Integer age;

  @Builder
  public Member(Long id, String email, String password, Integer age) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.age = age;
  }

  public Member(String email, String password, Integer age) {
    this(null, email, password, age);
  }

  public void update(Member member) {
    this.email = member.email;
    this.password = member.password;
    this.age = member.age;
  }

  public boolean checkPassword(String password) {
    return Objects.equals(this.password, password);
  }
}
